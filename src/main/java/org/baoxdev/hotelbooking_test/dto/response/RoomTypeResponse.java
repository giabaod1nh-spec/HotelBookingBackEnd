package org.baoxdev.hotelbooking_test.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class RoomTypeResponse {
    String roomTypeId;
    String roomTypeName;
    String roomTypeDesc;
    BigDecimal basePrice;
    Integer maxOccupy;
    Integer totalRooms;

}
