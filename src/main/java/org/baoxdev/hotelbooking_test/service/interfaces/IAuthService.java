package org.baoxdev.hotelbooking_test.service.interfaces;

import org.baoxdev.hotelbooking_test.dto.request.AuthRequest;
import org.baoxdev.hotelbooking_test.dto.response.AuthResponse;

public interface IAuthService {
    AuthResponse checkAuthenticationUser(AuthRequest request);

}
