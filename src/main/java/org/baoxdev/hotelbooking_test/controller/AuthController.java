package org.baoxdev.hotelbooking_test.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.baoxdev.hotelbooking_test.dto.ApiResponse;
import org.baoxdev.hotelbooking_test.dto.request.AuthRequest;
import org.baoxdev.hotelbooking_test.dto.response.AuthResponse;
import org.baoxdev.hotelbooking_test.service.impl.AuthServiceImpl;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@FieldDefaults(level = AccessLevel.PRIVATE , makeFinal = true)
@RequiredArgsConstructor
public class AuthController {
    AuthServiceImpl authService;

    @PostMapping("/login")
    public ApiResponse<AuthResponse> checkPassword(@RequestBody AuthRequest request){
        return  ApiResponse.<AuthResponse>builder()
                .code(1000)
                .result(authService.checkAuthenticationUser(request))
                .build();
    }


}
