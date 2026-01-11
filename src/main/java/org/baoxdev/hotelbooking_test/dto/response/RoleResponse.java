package org.baoxdev.hotelbooking_test.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.baoxdev.hotelbooking_test.model.entity.Permission;

import java.util.List;
import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class RoleResponse {
    String roleName;
    String description;
    Set<Permission> permissions;
}
