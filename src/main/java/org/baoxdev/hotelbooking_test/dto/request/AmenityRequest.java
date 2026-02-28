package org.baoxdev.hotelbooking_test.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.baoxdev.hotelbooking_test.model.enums.AmenityType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AmenityRequest {
    String amenityName;

    String amenityIcon ;

    AmenityType amenityType;

    String amenityDesc;

}
