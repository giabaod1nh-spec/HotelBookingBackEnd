package org.baoxdev.hotelbooking_test.mapper;

import org.baoxdev.hotelbooking_test.dto.response.BookingResponse;
import org.baoxdev.hotelbooking_test.dto.response.BookingRoomItemResponse;
import org.baoxdev.hotelbooking_test.model.entity.Booking;
import org.baoxdev.hotelbooking_test.model.entity.BookingRooms;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Component
public class BookingMapper {

    public BookingResponse buildBookingResponse (Booking booking , List<BookingRooms> bookingRooms , long nights){
        List<BookingRoomItemResponse> roomTypes = bookingRooms.stream()
                .map(br -> BookingRoomItemResponse
                        .builder()
                        .roomTypeId(br.getBookingRoomId())
                        .roomTypeName(br.getRoomType().getRoomTypeName())
                        .quantity(br.getQuantity())
                        .pricePerNight(br.getPricePerNight())
                        .subTotal(br.getPricePerNight().multiply(BigDecimal.valueOf(nights))
                                .multiply(BigDecimal.valueOf(br.getQuantity()))
                        )
                        .build()).toList();

        int totalRoom = bookingRooms.stream().mapToInt(br -> br.getQuantity()).sum();

        return BookingResponse.builder()
                .hotelId(booking.getHotel().getHotelId())
                .hotelName(booking.getHotel().getHotelName())
                .guestName(booking.getGuestName())
                .guestPhone(booking.getGuestPhone())
                .numGuest(booking.getNumGuest())
                .guestEmail(booking.getGuestEmail())
                .specialRequest(booking.getSpecialRequest())
                .bookingId(booking.getBookingId())
                .bookingCode(booking.getBookingCode())
                .status(booking.getBookingStatus())
                .totalPrice(booking.getTotalPrice())
                .totalRooms(totalRoom)
                .bookingRoomItemResponses(roomTypes)
                .checkInDate(booking.getCheckInDate())
                .checkOutDate(booking.getCheckOutDate())
                .build();
    }
}
