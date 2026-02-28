package org.baoxdev.hotelbooking_test.service.interfaces;

import org.baoxdev.hotelbooking_test.dto.request.BookingRequest;
import org.baoxdev.hotelbooking_test.dto.request.CheckInRequest;
import org.baoxdev.hotelbooking_test.dto.response.BookingResponse;
import org.springframework.stereotype.Service;

import java.util.List;

public interface IBookingService {
    BookingResponse createBooking(String hotelId , BookingRequest request , String currentUserName);
    BookingResponse getById(String bookingId);
    BookingResponse getByBookingCode(String bookingCode);
    List<BookingResponse> listMyBookings(String currentUserName);
    List<BookingResponse> getReviewableBookings(String currentUserName);
    void cancel(String bookingId, String currentUserName);
    void checkIn(String bookingId, CheckInRequest request);
    void checkOut(String bookingId);
}
