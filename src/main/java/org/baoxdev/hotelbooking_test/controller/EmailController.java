package org.baoxdev.hotelbooking_test.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.baoxdev.hotelbooking_test.dto.ApiResponse;
import org.baoxdev.hotelbooking_test.service.impl.EmailService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Slf4j(topic = "EMAIL-CONTROLLER")
public class EmailController {
    private final EmailService emailService;

    @GetMapping("/send-email")
    public ApiResponse<Void> sendEmailToUser(@RequestParam String to , @RequestParam String subject ,@RequestParam String text ){
        emailService.send(to , subject , text);
        return ApiResponse.<Void>
                builder().build();
    }

    @GetMapping("/verify-email")
    public ApiResponse<Void> verifyEmailFromUser(@RequestParam String userEmail , @RequestParam String userName) throws IOException {
        //emailService.emailVerification(userEmail , userName , token);
        //Ham duoc goi trong userService
        return ApiResponse.<Void>
                builder().build();
    }
}

