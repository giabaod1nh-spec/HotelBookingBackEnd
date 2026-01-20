package org.baoxdev.hotelbooking_test.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.baoxdev.hotelbooking_test.dto.request.ChangePassRequest;
import org.baoxdev.hotelbooking_test.dto.request.UserCreationRequest;
import org.baoxdev.hotelbooking_test.dto.request.UserUpdateRequest;
import org.baoxdev.hotelbooking_test.dto.response.UserCreationResponse;
import org.baoxdev.hotelbooking_test.exception.AppException;
import org.baoxdev.hotelbooking_test.mapper.UserMapper;
import org.baoxdev.hotelbooking_test.model.entity.Hotel;
import org.baoxdev.hotelbooking_test.model.entity.User;
import org.baoxdev.hotelbooking_test.model.enums.ErrorCode;
import org.baoxdev.hotelbooking_test.model.enums.UserStatus;
import org.baoxdev.hotelbooking_test.repository.UserRepository;
import org.baoxdev.hotelbooking_test.service.interfaces.IUserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE , makeFinal = true )
public class UserServiceImpl implements IUserService {
    UserMapper userMapper;
    UserRepository userRepository;

    @Override
    public UserCreationResponse createUser(UserCreationRequest request) {
        User user = userMapper.convertUserFromRequest(request);

        if(userRepository.existsUserByUserName(user.getUserName()) || userRepository.existsUserByEmail(user.getEmail())) {
            throw new RuntimeException("Email hoac ten dang nhap da ton tai");
        }

        return userMapper.convertResponseFromUser(userRepository.save(user));
    }

    @Override
    public UserCreationResponse getUser(String userId) {
        User user = userRepository.findUserByUserId(userId).orElseThrow(() -> new RuntimeException("Ko tim thay user"));
        return userMapper.convertResponseFromUser(user);
    }

    @Override
    public UserCreationResponse updateUser(String userId , UserUpdateRequest request) {
        User user = userRepository.findUserByUserId(userId)
                .orElseThrow(() ->new AppException(ErrorCode.USER_NOT_FOUND));

        if(request.getEmail() != null){
            user.setEmail(request.getEmail());
        }
        if(request.getFirstName() != null){
            user.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null){
            user.setLastName(request.getLastName());
        }
        return userMapper.convertResponseFromUser(userRepository.save(user));
    }

    @Override
    public List<UserCreationResponse> getAllUser() {
        return userRepository.findAll().stream().map(user -> userMapper.convertResponseFromUser(user)).toList();
    }

    @Override
    public void deleteUser(String userId) {
        User user = userRepository.findUserByUserId(userId).orElseThrow(() ->new AppException(ErrorCode.USER_NOT_FOUND));

        user.setUserStatus(UserStatus.DELETED);

        userRepository.save(user);
    }

    @Override
    public void changePassword(ChangePassRequest request) {
        User user = userRepository.findUserByUserId(request.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        if(request.getPassword().equals(request.getConfirmPassword())){
            user.setPassword(request.getPassword());
        }

        userRepository.save(user);
    }

    @Override
    public List<UserCreationResponse> getAllUserPagination(int page, int size, String sortBy, String direction
            , String firstName , String lastName , String email , UserStatus userStatus ) {
        Sort sort = Sort.by(Sort.Direction.fromString(direction) , sortBy);
        Pageable pageable = PageRequest.of(page , size , sort);

        Specification<Hotel> spec = ((root, query, cb) ->
              cb.conjunction());

        if(StringUtils.hasText(firstName)){
            spec = spec.and((root, query, cb) ->
                    cb.equal(cb.lower(root.get("firstName")), firstName.toLowerCase()));

        }

        if(StringUtils.hasText(lastName)){
            spec = spec.and((root, query, cb)->
                    cb.equal(cb.lower(root.get("lastName")) , lastName.toLowerCase()));
        }

        if(StringUtils.hasText(email)){
            spec = spec.and((root, query, cb) ->
                    cb.equal(cb.lower(root.get("email")) , email.toLowerCase()));
        }

        if(StringUtils.hasText(String.valueOf(userStatus))){
            spec = spec.and((root, query, cb) ->
                    cb.equal(cb.lower(root.get("userStatus")) , userStatus));
        }
        return userRepository.findAll(pageable).stream().map(user -> userMapper.convertResponseFromUser(user)).toList() ;
    }
}
