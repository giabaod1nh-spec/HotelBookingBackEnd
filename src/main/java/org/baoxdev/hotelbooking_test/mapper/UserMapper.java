package org.baoxdev.hotelbooking_test.mapper;

import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.baoxdev.hotelbooking_test.dto.request.UserCreationRequest;
import org.baoxdev.hotelbooking_test.dto.response.UserCreationResponse;
import org.baoxdev.hotelbooking_test.model.entity.User;
import org.baoxdev.hotelbooking_test.repository.PermissionRepository;
import org.baoxdev.hotelbooking_test.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@Data
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE , makeFinal = true)
public class UserMapper {
    UserRepository userRepository;
    PermissionRepository permissionRepository;

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
    private final RoleMapper roleMapper;

    public User convertUserFromRequest(UserCreationRequest request){
        User user = User.builder()
                .userName(request.getUserName())
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        return user;
    }

    public UserCreationResponse convertResponseFromUser(User user){
        var roles = user.getRoles().stream().map(role -> roleMapper.convertResponseFromRole(role)).collect(Collectors.toSet());

         UserCreationResponse response = UserCreationResponse.builder()
                 .userId(user.getUserId())
                 .userName(user.getUserName())
                 .email(user.getEmail())
                 .firstName(user.getFirstName()).
                 lastName(user.getLastName())
                 .roles(roles)
                 .build();
         return  response;
    }

}
