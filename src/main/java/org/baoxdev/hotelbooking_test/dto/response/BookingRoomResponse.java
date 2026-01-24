package org.baoxdev.hotelbooking_test.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class BookingRoomResponse {
    String bookingRoomId;
    String roomId;
    String roomNumber;
    BigDecimal pricePerNight;
    LocalDateTime actualCheckInTime;
    LocalDateTime actualCheckOutTime;
}
