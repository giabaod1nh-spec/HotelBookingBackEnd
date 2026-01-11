package org.baoxdev.hotelbooking_test.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.baoxdev.hotelbooking_test.model.entity.Role;

import java.util.List;
import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class UserCreationResponse {
    String userId;
    String email;
    String userName;
    String firstName;
    String lastName;
    Set<RoleResponse> roles;
    //tra ve ca roles va permission cua cac roles
}
