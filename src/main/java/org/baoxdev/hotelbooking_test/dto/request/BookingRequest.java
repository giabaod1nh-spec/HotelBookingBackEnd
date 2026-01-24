package org.baoxdev.hotelbooking_test.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingRequest {
    LocalDate checkInDate;
    LocalDate checkOutDate;
    Integer quantity;  //number of rooms
    Integer numGuests;  //mumber of guests
    String guestName;
    String guestPhone;
    String guestEmail;
    String specialRequest;

    List<BookingRoomItemRequest> rooms; //changed multiples rooms
}
