package org.baoxdev.hotelbooking_test.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.baoxdev.hotelbooking_test.dto.ApiResponse;
import org.baoxdev.hotelbooking_test.dto.request.PermissionRequest;
import org.baoxdev.hotelbooking_test.dto.response.PermissionResponse;
import org.baoxdev.hotelbooking_test.service.impl.PermissionServiceImpl;
import org.baoxdev.hotelbooking_test.service.interfaces.IPermissionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permission")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE , makeFinal = true)
public class PermissionController {
    IPermissionService permissionService;

    @GetMapping("/get/{permissionId}")
    public ApiResponse<PermissionResponse> getPermissionById(@PathVariable String permissionId){
         return ApiResponse.<PermissionResponse>builder()
                 .result(permissionService.getPermission(permissionId))
                 .code(1000)
                 .build();
    }

    @PostMapping("/create")
    public ApiResponse<PermissionResponse> createPermission(@RequestBody PermissionRequest request){
        return ApiResponse.<PermissionResponse>builder()
                .code(1000)
                .result(permissionService.createPermission(request))
                .build();
    }

    @GetMapping("/getAll")
    public ApiResponse<List<PermissionResponse>> getAllPermission(){
        return ApiResponse.<List<PermissionResponse>>builder()
                .code(1000)
                .result(permissionService.getAllPermission())
                .build();
    }

    @DeleteMapping("/delete/{permissionId}")
    public ApiResponse<Void> deletePermission(@PathVariable String permissionId){
        permissionService.deletePermission(permissionId);
        return ApiResponse.<Void>builder()
                .build();
    }

}
