package org.baoxdev.hotelbooking_test.mapper;

import org.baoxdev.hotelbooking_test.dto.response.BookingResponse;
import org.baoxdev.hotelbooking_test.model.entity.Booking;
import org.springframework.stereotype.Component;

import java.time.ZoneId;

@Component
public class BookingMapper {

    public BookingResponse convertResponseFromBooking(Booking booking){
        return BookingResponse.builder()
                .hotelName(booking.getHotel().getHotelName())
                .roomTypeName(booking.getRoomType().getRoomTypeName())
                .guestName(booking.getGuestName())
                .guestPhone(booking.getGuestPhone())
                .guestEmail(booking.getGuestEmail())
                .specialRequest(booking.getSpecialRequest())
                .bookingId(booking.getBookingId())
                .bookingCode(booking.getBookingCode())
                .status(booking.getBookingStatus())
                .checkInDate(booking.getCheckInDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                .checkOutDate(booking.getCheckOutDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                .build();
    }
}
