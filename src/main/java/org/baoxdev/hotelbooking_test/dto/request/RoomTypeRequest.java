package org.baoxdev.hotelbooking_test.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.baoxdev.hotelbooking_test.model.enums.RoomTypeStatus;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoomTypeRequest {
    String roomTypeName;
    String roomTypeDesc;
    BigDecimal basePrice;
    Integer maxOccupy;
    Integer totalRooms;
    RoomTypeStatus roomTypeStatus;

}
