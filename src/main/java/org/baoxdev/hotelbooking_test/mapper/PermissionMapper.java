package org.baoxdev.hotelbooking_test.mapper;


import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.baoxdev.hotelbooking_test.dto.request.PermissionRequest;
import org.baoxdev.hotelbooking_test.dto.response.PermissionResponse;
import org.baoxdev.hotelbooking_test.model.entity.Permission;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE , makeFinal = true)
public class PermissionMapper {
    public Permission createPermissionFromResponse(PermissionRequest request){
        return Permission.builder()
                .permissionName(request.getPermission_name())
                .description(request.getDescription())
                .build();
    }

    public PermissionResponse convertPermissionToResponse(Permission permission){
        return PermissionResponse.builder()
                .permission_name(permission.getPermissionName())
                .description(permission.getDescription())
                .build();
    }

}
