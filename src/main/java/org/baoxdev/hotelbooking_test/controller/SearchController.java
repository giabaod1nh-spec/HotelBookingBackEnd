package org.baoxdev.hotelbooking_test.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.baoxdev.hotelbooking_test.dto.ApiResponse;
import org.baoxdev.hotelbooking_test.dto.request.HotelSearchPageRequest;
import org.baoxdev.hotelbooking_test.dto.response.HotelSearchResponse;
import org.baoxdev.hotelbooking_test.dto.response.PageResponse;
import org.baoxdev.hotelbooking_test.service.interfaces.ISearchService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/search")
@FieldDefaults(level = AccessLevel.PRIVATE , makeFinal = true)
@RequiredArgsConstructor
public class SearchController {
    ISearchService searchService;

    @PostMapping("/first")
    ApiResponse<List<HotelSearchResponse>> getHotelSearch(
            @RequestParam String city ,
            @RequestParam LocalDate checkIn ,
            @RequestParam LocalDate checkOut ,
            @RequestParam int totalGuest ,
            @RequestParam int totalRoom
    ){
        return ApiResponse.<List<HotelSearchResponse>>builder()
                .code(1000)
                .message("First search thanh cong ")
                .result(searchService.searchHotels(city , checkIn ,checkOut , totalGuest , totalRoom))
                .build();
    }

    @PostMapping("/second")
    ApiResponse<PageResponse<HotelSearchResponse>> getHotelSearchV2(@RequestBody HotelSearchPageRequest request) {
        return ApiResponse.<PageResponse<HotelSearchResponse>>builder()
                .code(1000)
                .message("Search v2 success")
                .result(searchService.searchHotelsV2(request))
                .build();
    }

}
