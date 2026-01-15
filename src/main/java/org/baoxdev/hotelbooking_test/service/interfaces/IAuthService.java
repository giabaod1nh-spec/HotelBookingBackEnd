package org.baoxdev.hotelbooking_test.service.interfaces;

import com.nimbusds.jose.JOSEException;
import org.baoxdev.hotelbooking_test.dto.request.AuthRequest;
import org.baoxdev.hotelbooking_test.dto.request.IntroSpectRequest;
import org.baoxdev.hotelbooking_test.dto.request.LogOutRequest;
import org.baoxdev.hotelbooking_test.dto.request.RefreshTokenRequest;
import org.baoxdev.hotelbooking_test.dto.response.AuthResponse;
import org.baoxdev.hotelbooking_test.dto.response.IntroSpectResponse;

import java.text.ParseException;

public interface IAuthService {
    AuthResponse checkAuthenticationUser(AuthRequest request);
    IntroSpectResponse introspectToken(IntroSpectRequest request);
    AuthResponse refreshTokenAfterTimeOut(RefreshTokenRequest request) throws ParseException, JOSEException;
    void logOut(LogOutRequest request) throws ParseException, JOSEException;
}
