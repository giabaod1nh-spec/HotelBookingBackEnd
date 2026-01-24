package org.baoxdev.hotelbooking_test.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingRoomItemResponse {
    String roomTypeId;
    String roomTypeName;
    Integer quantity;
    BigDecimal pricePerNight;
    BigDecimal subTotal; //pricePerNight * quantity * nights
}
