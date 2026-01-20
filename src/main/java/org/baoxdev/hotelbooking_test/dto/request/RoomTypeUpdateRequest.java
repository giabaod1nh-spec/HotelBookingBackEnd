package org.baoxdev.hotelbooking_test.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.baoxdev.hotelbooking_test.model.enums.RoomTypeStatus;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomTypeUpdateRequest {
    String roomTypeName;
    String roomTypeDesc;
    BigDecimal basePrice;
    Integer maxOccupy;
    Integer totalRooms;
}
