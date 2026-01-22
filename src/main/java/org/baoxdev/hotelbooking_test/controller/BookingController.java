package org.baoxdev.hotelbooking_test.controller;

import com.cloudinary.Api;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.baoxdev.hotelbooking_test.dto.ApiResponse;
import org.baoxdev.hotelbooking_test.dto.request.BookingRequest;
import org.baoxdev.hotelbooking_test.dto.response.BookingResponse;
import org.baoxdev.hotelbooking_test.service.interfaces.IBookingService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/booking")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE , makeFinal = true)

public class BookingController {
    IBookingService bookingService;

    private String getCurrentUserName(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    @PostMapping("/create/{hotelId}/{roomTypeId}")
    public ApiResponse<BookingResponse> createBooking(
            @PathVariable String hotelId,
            @PathVariable String roomTypeId,
            @RequestBody BookingRequest request
            ){
        return ApiResponse.<BookingResponse>builder()
                .code(1000)
                .result(bookingService.createBooking(hotelId , roomTypeId , request, getCurrentUserName()))
                .build();
    }


    @GetMapping("/get/{bookingId}")
    public ApiResponse<BookingResponse> getBookingById(@PathVariable String bookingId ){
        return ApiResponse.<BookingResponse>builder()
                .code(1000)
                .result(bookingService.getById(bookingId))
                .build();
    }

    @GetMapping("/getBookingHistory")
    public ApiResponse<List<BookingResponse>> getAllBooking(){
        return ApiResponse.<List<BookingResponse>>builder()
                .code(1000)
                .result(bookingService.listMyBookings(getCurrentUserName()))
                .build();
    }

    @PutMapping("cancel/{bookingId}")
    public ApiResponse<Void> cancelBooking(@PathVariable String bookingId){
        bookingService.cancel(bookingId , getCurrentUserName());
        return ApiResponse.<Void>builder()
                .code(1000)
                .build();
    }


}
