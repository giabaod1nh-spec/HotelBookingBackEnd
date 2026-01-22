package org.baoxdev.hotelbooking_test.service.interfaces;

import org.baoxdev.hotelbooking_test.dto.request.BookingRequest;
import org.baoxdev.hotelbooking_test.dto.response.BookingResponse;
import org.springframework.stereotype.Service;

import java.util.List;

public interface IBookingService {
    BookingResponse createBooking(String hotelId , String roomTypeId , BookingRequest request , String currentUserName);
    BookingResponse getById(String bookingId);
    List<BookingResponse> listMyBookings(String currentUserName);
    void cancel(String bookingId, String currentUserName);
}
