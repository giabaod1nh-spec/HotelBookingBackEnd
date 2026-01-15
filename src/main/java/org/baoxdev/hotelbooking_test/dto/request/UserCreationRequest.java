package org.baoxdev.hotelbooking_test.dto.request;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.baoxdev.hotelbooking_test.model.entity.Role;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.util.List;
import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {
    private String userName;

    private String email;

    private String password;

    private String firstName;

    private String lastName;

    private Set<String> roles;
    //private List<Role> roles;
    //tao user , sau do quyen gan roles cho admin
}
