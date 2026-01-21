package org.baoxdev.hotelbooking_test.controller;

import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.baoxdev.hotelbooking_test.dto.ApiResponse;
import org.baoxdev.hotelbooking_test.dto.request.AuthRequest;
import org.baoxdev.hotelbooking_test.dto.request.IntroSpectRequest;
import org.baoxdev.hotelbooking_test.dto.request.LogOutRequest;
import org.baoxdev.hotelbooking_test.dto.request.RefreshTokenRequest;
import org.baoxdev.hotelbooking_test.dto.response.AuthResponse;
import org.baoxdev.hotelbooking_test.dto.response.IntroSpectResponse;
import org.baoxdev.hotelbooking_test.service.impl.AuthServiceImpl;
import org.baoxdev.hotelbooking_test.service.interfaces.IAuthService;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@FieldDefaults(level = AccessLevel.PRIVATE , makeFinal = true)
@RequiredArgsConstructor
public class AuthController {
    IAuthService authService;

    @PostMapping("/login")
    public ApiResponse<AuthResponse> checkPassword(@RequestBody AuthRequest request){
        return  ApiResponse.<AuthResponse>builder()
                .code(1000)
                .result(authService.checkAuthenticationUser(request))
                .build();
    }

    @PostMapping("/introspect")
    public ApiResponse<IntroSpectResponse> introspectToken(@RequestBody IntroSpectRequest request){
        return ApiResponse.<IntroSpectResponse>builder()
                .code(1000)
                .result(authService.introspectToken(request))
                .build();
    }

    @PostMapping("/refresh")
    public ApiResponse<AuthResponse> refreshToken(@RequestBody RefreshTokenRequest request) throws ParseException, JOSEException {
        return ApiResponse.<AuthResponse>builder()
                .result(authService.refreshTokenAfterTimeOut(request))
                .build();
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logoutToken(@RequestBody LogOutRequest request) throws ParseException, JOSEException {
        authService.logOut(request);
        return ApiResponse.<Void>builder()
                .code(1000)
                .build();
    }
}
