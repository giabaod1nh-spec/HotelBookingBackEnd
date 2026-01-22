package org.baoxdev.hotelbooking_test.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.baoxdev.hotelbooking_test.model.enums.BookingStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingResponse {
    String hotelName;
    String roomTypeName;
    String guestName;
    String guestEmail;
    String guestPhone;
    Integer numGuest;
    String specialRequest;
    String bookingId;
    String bookingCode;
    BookingStatus status;
    LocalDate checkInDate;
    LocalDate checkOutDate;
    Integer quantity ;
    BigDecimal totalPrice;
}
