package org.baoxdev.hotelbooking_test.service.interfaces;

import org.baoxdev.hotelbooking_test.dto.request.RoleRequest;
import org.baoxdev.hotelbooking_test.dto.response.RoleResponse;
import org.baoxdev.hotelbooking_test.model.entity.Role;

import java.util.List;

public interface IRoleService {
    RoleResponse createRole(RoleRequest request);
    RoleResponse getRole(String roleId);
    List<RoleResponse> getAllRole();
    void deleteRole(String roleId);
}
