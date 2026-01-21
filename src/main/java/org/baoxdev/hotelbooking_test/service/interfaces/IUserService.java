package org.baoxdev.hotelbooking_test.service.interfaces;

import org.baoxdev.hotelbooking_test.dto.request.ChangePassRequest;
import org.baoxdev.hotelbooking_test.dto.request.UserCreationRequest;
import org.baoxdev.hotelbooking_test.dto.request.UserUpdateRequest;
import org.baoxdev.hotelbooking_test.dto.response.UserCreationResponse;
import org.baoxdev.hotelbooking_test.model.entity.User;
import org.baoxdev.hotelbooking_test.model.enums.UserStatus;
import org.hibernate.usertype.UserVersionType;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IUserService {
    UserCreationResponse createUser(UserCreationRequest request);

    UserCreationResponse getUser(String userId);

    UserCreationResponse updateUser(String userId, UserUpdateRequest request);

    List<UserCreationResponse> getAllUser();

    void deleteUser(String userId);

    void changePassword(ChangePassRequest request);

    List<UserCreationResponse> getAllUserPagination(int page, int size, String sortBy, String direction
            , String firstName, String lastName, String email, UserStatus userStatus);
}
