package org.baoxdev.hotelbooking_test.service.interfaces;

import org.baoxdev.hotelbooking_test.dto.request.UserCreationRequest;
import org.baoxdev.hotelbooking_test.dto.response.UserCreationResponse;

import java.util.List;

public interface IUserService {
    UserCreationResponse createUser(UserCreationRequest request);
    UserCreationResponse getUser(String userId);
    List<UserCreationResponse> getAllUser();
    void deleteUser(String userId);
}
