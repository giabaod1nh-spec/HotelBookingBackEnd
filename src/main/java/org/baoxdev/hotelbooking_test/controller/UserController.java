package org.baoxdev.hotelbooking_test.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.baoxdev.hotelbooking_test.dto.ApiResponse;
import org.baoxdev.hotelbooking_test.dto.request.UserCreationRequest;
import org.baoxdev.hotelbooking_test.dto.response.UserCreationResponse;
import org.baoxdev.hotelbooking_test.service.impl.UserServiceImpl;
import org.springframework.web.bind.annotation.*;
import tools.jackson.databind.json.JsonMapper;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE , makeFinal = true)
public class UserController {
    UserServiceImpl userService;
    private final JsonMapper.Builder builder;

    @PostMapping("/create")
    public ApiResponse<UserCreationResponse> createUser(@RequestBody UserCreationRequest request){

        return ApiResponse.<UserCreationResponse>builder()
                .result(userService.createUser(request))
                .build();
    }


    @GetMapping("/get/{userId}")
    public ApiResponse<UserCreationResponse> getUser(@PathVariable String userId){
         return  ApiResponse.<UserCreationResponse>builder()
                 .result(userService.getUser(userId))
                 .build();
    }

    @GetMapping("/getAll")
    public ApiResponse<List<UserCreationResponse>> getAllUser(){
        return ApiResponse.<List<UserCreationResponse>>builder()
                .result(userService.getAllUser())
                .build();
    }
}
