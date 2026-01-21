package org.baoxdev.hotelbooking_test.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.baoxdev.hotelbooking_test.dto.ApiResponse;
import org.baoxdev.hotelbooking_test.dto.request.ChangePassRequest;
import org.baoxdev.hotelbooking_test.dto.request.UserCreationRequest;
import org.baoxdev.hotelbooking_test.dto.request.UserUpdateRequest;
import org.baoxdev.hotelbooking_test.dto.response.UserCreationResponse;
import org.baoxdev.hotelbooking_test.model.enums.UserStatus;
import org.baoxdev.hotelbooking_test.service.impl.UserServiceImpl;
import org.baoxdev.hotelbooking_test.service.interfaces.IUserService;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import tools.jackson.databind.json.JsonMapper;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE , makeFinal = true)
@Slf4j
public class UserController {
    IUserService userService;
    private final JsonMapper.Builder builder;

    @PostMapping("/register")
    public ApiResponse<UserCreationResponse> createUser(@RequestBody UserCreationRequest request){

        return ApiResponse.<UserCreationResponse>builder()
                .result(userService.createUser(request))
                .build();
    }


    @GetMapping("/get/{userId}")
    public ApiResponse<UserCreationResponse> getUser(@PathVariable String userId){
        //Test log thử xem authorities của mình
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        log.info(auth.getAuthorities().toString());

         return  ApiResponse.<UserCreationResponse>builder()
                 .result(userService.getUser(userId))
                 .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getAll")
    public ApiResponse<List<UserCreationResponse>> getAllUser(){
        return ApiResponse.<List<UserCreationResponse>>builder()
                .result(userService.getAllUser())
                .build();
    }

    @PutMapping("/update/{userId}")
    public ApiResponse<UserCreationResponse> updateUser(@PathVariable String userId , @RequestBody UserUpdateRequest request){
        return ApiResponse.<UserCreationResponse>builder()
                .code(1000)
                .result(userService.updateUser(userId ,request))
                .build();
    }


    @DeleteMapping("/delete/{userId}")
    public ApiResponse<Void> deleteUser(@PathVariable String userId){
        userService.deleteUser(userId);
        return ApiResponse.<Void>builder()
                .code(1000)
                .build();
    }


    @PutMapping("/changePass")
    public ApiResponse<Void> changePassword(@RequestBody ChangePassRequest request){
        userService.changePassword(request);
        return  ApiResponse.<Void> builder()
                .code(1000)
                .build();
    }

    @GetMapping("/sort")
    public ApiResponse<List<UserCreationResponse>> getAllUserPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction,
            @RequestParam(required = false) String firstName ,
            @RequestParam(required = false) String lastName ,
            @RequestParam(required = false) String email ,
            @RequestParam(required = false)UserStatus userStatus
            ){

        return ApiResponse.<List<UserCreationResponse>>builder()
                .code(1000)
                .result(userService.getAllUserPagination(page , size , sortBy , direction, firstName , lastName , email , userStatus ))
                .build();
    }

}
