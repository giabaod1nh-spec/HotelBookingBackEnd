package org.baoxdev.hotelbooking_test.service.interfaces;

import org.baoxdev.hotelbooking_test.dto.request.PermissionRequest;
import org.baoxdev.hotelbooking_test.dto.response.PermissionResponse;

import java.util.List;

public interface IPermissionService {
    PermissionResponse createPermission(PermissionRequest request);
    PermissionResponse getPermission(String permissionId);
    List<PermissionResponse> getAllPermission();
    void deletePermission(String permissionId);
}
