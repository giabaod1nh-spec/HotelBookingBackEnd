package org.baoxdev.hotelbooking_test.service.interfaces;

import org.baoxdev.hotelbooking_test.dto.request.AssignAmenitiesRequest;
import org.baoxdev.hotelbooking_test.dto.request.HotelRequest;
import org.baoxdev.hotelbooking_test.dto.request.HotelSearchRequest;
import org.baoxdev.hotelbooking_test.dto.response.AmenityResponse;
import org.baoxdev.hotelbooking_test.dto.response.HotelResponse;
import org.baoxdev.hotelbooking_test.dto.response.HotelSearchResponse;
import org.baoxdev.hotelbooking_test.dto.response.PageResponse;

import java.util.List;

public interface IHotelService {
    HotelResponse createHotel(HotelRequest request);
    HotelResponse getHotelInfo(String hotelId);
    HotelResponse updateHotelInfo(String hotelId , HotelRequest request);
    void deleteHotel(String hotelId);
    void assignAmenitiesToHotel(String hotelId , AssignAmenitiesRequest request);
    List<HotelResponse> getAllHotelFromPagination(int page , int size , String sortBy , String direction, String city, String country, Integer starRating);
    void deleteAmenityFromHotel(String hotelId , String amenityId);
    PageResponse<HotelSearchResponse> searchHotels(HotelSearchRequest request);
}
