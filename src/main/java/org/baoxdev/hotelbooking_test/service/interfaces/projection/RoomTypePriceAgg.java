package org.baoxdev.hotelbooking_test.service.interfaces.projection;

import java.math.BigDecimal;

public interface RoomTypePriceAgg {
    String getHotelId();
    String getRoomTypeId();
    String getRoomTypeName();
    String getBedSummary();
    BigDecimal getTotalPrice();
}
