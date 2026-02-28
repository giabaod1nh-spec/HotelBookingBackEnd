package org.baoxdev.hotelbooking_test.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.baoxdev.hotelbooking_test.model.enums.AmenityType;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AmenityUpdateRequest {
    String amenityId;

    String amenityName;

    String amenityIcon ;

    String amenityDesc;

    AmenityType amenityType;
}
