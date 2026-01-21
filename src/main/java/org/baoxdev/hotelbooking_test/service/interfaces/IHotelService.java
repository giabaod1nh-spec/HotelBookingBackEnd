package org.baoxdev.hotelbooking_test.service.interfaces;

import org.baoxdev.hotelbooking_test.dto.request.HotelRequest;
import org.baoxdev.hotelbooking_test.dto.response.HotelResponse;

import java.util.List;

public interface IHotelService {
    HotelResponse createHotel(HotelRequest request);
    HotelResponse getHotelInfo(String hotelId);
    HotelResponse updateHotelInfo(String hotelId , HotelRequest request);
    void deleteHotel(String hotelId);
    List<HotelResponse> getAllHotelFromPagination(int page , int size , String sortBy , String direction, String city, String country, Integer starRating);
}
