package org.baoxdev.hotelbooking_test.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.baoxdev.hotelbooking_test.model.enums.BookingStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class BookingResponse {
    String hotelId;
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
    Integer totalRooms ;
    BigDecimal totalPrice;

    List<BookingRoomItemResponse> bookingRoomItemResponses;

    List<BookingRoomResponse> assignedRooms; //See assigned rooms detail
}
