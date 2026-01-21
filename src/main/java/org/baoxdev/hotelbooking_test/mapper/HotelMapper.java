package org.baoxdev.hotelbooking_test.mapper;

import org.baoxdev.hotelbooking_test.dto.request.HotelRequest;
import org.baoxdev.hotelbooking_test.dto.response.HotelResponse;
import org.baoxdev.hotelbooking_test.model.entity.Hotel;
import org.baoxdev.hotelbooking_test.model.enums.HotelStatus;
import org.springframework.stereotype.Component;

@Component
public class HotelMapper {
    public Hotel convertHotelFromCreateRequest(HotelRequest request){
        return Hotel.builder()
                .hotelName(request.getHotelName())
                .hotelDescription(request.getHotelDescription())
                .hotelAddress(request.getHotelAddress())
                .hotelCity(request.getHotelCity())
                .hotelCountry(request.getHotelCountry())
                .hotelPhone(request.getHotelPhone())
                .hotelEmail(request.getHotelEmail())
                .starRating(request.getStarRating())
                .hotelStatus(HotelStatus.OPEN)
                .build();
    }

    public HotelResponse convertResponseFromHotel(Hotel hotel){
         return HotelResponse.builder()
                 .hotelId(hotel.getHotelId())
                 .hotelName(hotel.getHotelName())
                 .hotelDescription(hotel.getHotelDescription())
                 .hotelAddress(hotel.getHotelAddress())
                 .hotelCity(hotel.getHotelCity())
                 .hotelCountry(hotel.getHotelCountry())
                 .hotelPhone(hotel.getHotelPhone())
                 .hotelEmail(hotel.getHotelEmail())
                 .hotelStatus(hotel.getHotelStatus())
                 .starRating(hotel.getStarRating())
                 .build();
    }
}
