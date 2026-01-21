package org.baoxdev.hotelbooking_test.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.baoxdev.hotelbooking_test.dto.ApiResponse;
import org.baoxdev.hotelbooking_test.dto.request.HotelRequest;
import org.baoxdev.hotelbooking_test.dto.response.HotelResponse;
import org.baoxdev.hotelbooking_test.model.entity.Hotel;
import org.baoxdev.hotelbooking_test.service.impl.HotelServiceImpl;
import org.baoxdev.hotelbooking_test.service.interfaces.IHotelService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hotel")
@FieldDefaults(level = AccessLevel.PRIVATE , makeFinal = true)
@RequiredArgsConstructor
public class HotelController {
    IHotelService hotelService;

    @PostMapping("/create")
    ApiResponse<HotelResponse> createHotel(@RequestBody HotelRequest request){
        return ApiResponse.<HotelResponse>builder()
                .code(1000)
                .result(hotelService.createHotel(request))
                .build();
    }

    @GetMapping("/get/{hotelId}")
    ApiResponse<HotelResponse> getHotelById(@PathVariable String hotelId){
        return ApiResponse.<HotelResponse>builder()
                .code(1000)
                .result(hotelService.getHotelInfo(hotelId))
                .build();
    }

    @DeleteMapping
    ApiResponse<Void>  deleteHotel(@PathVariable String hotelId){
        hotelService.deleteHotel(hotelId);
        return ApiResponse.<Void>builder()
                .build();
    }


    @GetMapping("sortHotel")
    ApiResponse<List<HotelResponse>> getAllHotelPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) Integer starRating)
    {
        return ApiResponse.<List<HotelResponse>>builder()
                .code(1000)
                .result(hotelService.getAllHotelFromPagination(page , size , sortBy , direction, city, country, starRating))
                .build();
    }
}
