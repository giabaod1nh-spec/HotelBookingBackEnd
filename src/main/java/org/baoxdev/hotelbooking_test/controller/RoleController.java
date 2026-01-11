package org.baoxdev.hotelbooking_test.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.baoxdev.hotelbooking_test.dto.ApiResponse;
import org.baoxdev.hotelbooking_test.dto.request.RoleRequest;
import org.baoxdev.hotelbooking_test.dto.response.RoleResponse;
import org.baoxdev.hotelbooking_test.model.entity.Role;
import org.baoxdev.hotelbooking_test.service.impl.RoleServiceImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE , makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("/role")
public class RoleController {
    RoleServiceImpl roleService;

    @PostMapping("/create")
    public ApiResponse<RoleResponse> createRole(@RequestBody RoleRequest request){
        return ApiResponse.<RoleResponse>builder()
                .result(roleService.createRole(request))
                .build();
    }


    @GetMapping("get/{roleId}")
    public ApiResponse<RoleResponse> getRoleById(@PathVariable String roleId){
        return ApiResponse.<RoleResponse>builder()
                .result(roleService.getRole(roleId))
                .build();
    }

    @GetMapping("/getAll")
    public ApiResponse<List<RoleResponse>> getAllRole(){
        return ApiResponse.<List<RoleResponse>>builder()
                .result(roleService.getAllRole())
                .build();
    }

    @DeleteMapping("/delete/{roleId}")
    public ApiResponse<Void> deleteRoleById(@PathVariable String roleId){
        roleService.deleteRole(roleId);
        return ApiResponse.<Void>builder()
                .build();
    }
}
