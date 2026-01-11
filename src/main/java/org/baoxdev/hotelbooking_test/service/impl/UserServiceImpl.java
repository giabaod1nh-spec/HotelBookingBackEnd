package org.baoxdev.hotelbooking_test.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.baoxdev.hotelbooking_test.dto.request.UserCreationRequest;
import org.baoxdev.hotelbooking_test.dto.response.UserCreationResponse;
import org.baoxdev.hotelbooking_test.mapper.UserMapper;
import org.baoxdev.hotelbooking_test.model.entity.User;
import org.baoxdev.hotelbooking_test.repository.UserRepository;
import org.baoxdev.hotelbooking_test.service.interfaces.IUserService;
import org.springframework.stereotype.Service;

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
    public List<UserCreationResponse> getAllUser() {
        return userRepository.findAll().stream().map(user -> userMapper.convertResponseFromUser(user)).toList();
    }
}
