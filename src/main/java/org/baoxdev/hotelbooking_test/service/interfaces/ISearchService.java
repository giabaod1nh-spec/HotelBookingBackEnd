package org.baoxdev.hotelbooking_test.service.interfaces;

import org.baoxdev.hotelbooking_test.dto.request.HotelSearchPageRequest;
import org.baoxdev.hotelbooking_test.dto.response.HotelSearchResponse;
import org.baoxdev.hotelbooking_test.dto.response.PageResponse;

import java.time.LocalDate;
import java.util.List;

public interface ISearchService {
    List<HotelSearchResponse> searchHotels(String city , LocalDate checkIn , LocalDate checkOut , int totalGuest , int totalRoom);
    HotelSearchResponse getHotelDetail(String hotelId);
    PageResponse<HotelSearchResponse> searchHotelsV2(HotelSearchPageRequest request);

}
