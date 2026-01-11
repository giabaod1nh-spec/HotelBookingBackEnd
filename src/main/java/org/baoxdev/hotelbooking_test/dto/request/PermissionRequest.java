package org.baoxdev.hotelbooking_test.dto.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.baoxdev.hotelbooking_test.model.entity.Permission;

import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PermissionRequest {
    String permission_name;
    String description;
    Set<Permission> permissions;
}
