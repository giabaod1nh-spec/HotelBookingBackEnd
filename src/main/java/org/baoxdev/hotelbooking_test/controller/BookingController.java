package org.baoxdev.hotelbooking_test.controller;

import com.cloudinary.Api;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.baoxdev.hotelbooking_test.dto.ApiResponse;
import org.baoxdev.hotelbooking_test.dto.request.BookingRequest;
import org.baoxdev.hotelbooking_test.dto.request.CheckInRequest;
import org.baoxdev.hotelbooking_test.dto.response.BookingResponse;
import org.baoxdev.hotelbooking_test.service.impl.BookingServiceImpl;
import org.baoxdev.hotelbooking_test.service.interfaces.IBookingService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/booking")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE , makeFinal = true)
@Slf4j(topic = "BOOKING CONTROLLER")
public class BookingController {
    BookingServiceImpl bookingService;

    private String getCurrentUserName(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("Current name" + authentication.getName());
        return authentication.getName();
    }

    @PostMapping("/create/{hotelId}")
    public ApiResponse<BookingResponse> createBooking(
            @PathVariable String hotelId,
            @RequestBody BookingRequest request
            ){
        return ApiResponse.<BookingResponse>builder()
                .code(1000)
                .result(bookingService.createBooking(hotelId , request, getCurrentUserName()))
                .build();
    }


    @GetMapping("/get/{bookingId}")
    public ApiResponse<BookingResponse> getBookingById(@PathVariable String bookingId ){
        return ApiResponse.<BookingResponse>builder()
                .code(1000)
                .result(bookingService.getById(bookingId))
                .build();
    }

    @GetMapping("/code/{bookingCode}")
    public ApiResponse<BookingResponse> getBookingByCode(@PathVariable String bookingCode){
        return ApiResponse.<BookingResponse>builder()
                .code(1000)
                .result(bookingService.getByBookingCode(bookingCode))
                .build();
    }

    @GetMapping("/getBookingHistory")
    public ApiResponse<List<BookingResponse>> getAllBooking(){
        return ApiResponse.<List<BookingResponse>>builder()
                .code(1000)
                .result(bookingService.listMyBookings(getCurrentUserName()))
                .build();
    }


    @GetMapping("/reviewable")
    public ApiResponse<List<BookingResponse>> getReviewableBookings(){
        return ApiResponse.<List<BookingResponse>>builder()
                .code(1000)
                .result(bookingService.getReviewableBookings(getCurrentUserName()))
                .build();
    }

    @PutMapping("cancel/{bookingId}")
    public ApiResponse<Void> cancelBooking(@PathVariable String bookingId){
        bookingService.cancel(bookingId , getCurrentUserName());
        return ApiResponse.<Void>builder()
                .code(1000)
                .build();
    }

    @PutMapping("/checkIn/{bookingId}")
    public ApiResponse<Void> checkInHotel(@PathVariable String bookingId ,
                                          @RequestBody CheckInRequest request
                                          ) {
        bookingService.checkIn(bookingId, request);
        return ApiResponse.<Void>builder()
                .code(1000)
                .message("Check in thanh cong")
                .build();
    }

    @PutMapping("checkOut/{bookingId}")
    public ApiResponse<Void>  checkOutHotel(@PathVariable String bookingId){

        return ApiResponse.<Void>builder()
                .code(1000)
                .message("Check out thanh cong ")
                .build();
    }
}
