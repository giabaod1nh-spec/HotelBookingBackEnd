package org.baoxdev.hotelbooking_test.mapper;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.baoxdev.hotelbooking_test.dto.request.RoleRequest;
import org.baoxdev.hotelbooking_test.dto.response.RoleResponse;
import org.baoxdev.hotelbooking_test.exception.AppException;
import org.baoxdev.hotelbooking_test.model.entity.Role;
import org.baoxdev.hotelbooking_test.model.enums.ErrorCode;
import org.baoxdev.hotelbooking_test.repository.PermissionRepository;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.time.Instant;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE , makeFinal = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoleMapper {
    PermissionRepository permissionRepository;

    public Role convertRoleFromRequest(RoleRequest request){
        //Set trong request chi la permissionName [UPDATE , CREATE] , sau do dung de search -> permission obj roi add
        var permission = request.getPermissions().stream()
                .map(obj -> permissionRepository.findById(obj)
                        .orElseThrow(() ->new AppException(ErrorCode.USER_EXISTED)))
                .collect(Collectors.toSet());
        return Role.builder()
                .roleName(request.getRoleName())
                .description(request.getDescription())
                .permissions(permission)
                .created_at(Instant.now())
                .build();
    }


    public RoleResponse convertResponseFromRole(Role role){
        return RoleResponse.builder()
                .roleName(role.getRoleName())
                .description(role.getDescription())
                .permissions(role.getPermissions())
                .build();
    }

}
