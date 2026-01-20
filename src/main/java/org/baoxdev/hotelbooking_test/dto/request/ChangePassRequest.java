package org.baoxdev.hotelbooking_test.dto.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChangePassRequest {
    String userId;
    String password;
    String confirmPassword;
}
