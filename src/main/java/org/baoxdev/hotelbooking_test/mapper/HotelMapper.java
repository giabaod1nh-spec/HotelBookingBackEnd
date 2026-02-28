package org.baoxdev.hotelbooking_test.mapper;

import lombok.RequiredArgsConstructor;
import org.baoxdev.hotelbooking_test.dto.request.HotelRequest;
import org.baoxdev.hotelbooking_test.dto.response.AmenityResponse;
import org.baoxdev.hotelbooking_test.dto.response.HotelResponse;
import org.baoxdev.hotelbooking_test.model.entity.Amenities;
import org.baoxdev.hotelbooking_test.model.entity.Hotel;
import org.baoxdev.hotelbooking_test.model.enums.HotelStatus;
import org.baoxdev.hotelbooking_test.repository.AmenityRepository;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class HotelMapper {
    AmenityRepository amenityRepository;
    public Hotel convertHotelFromCreateRequest(HotelRequest request){

        Set<Amenities> amenities = new HashSet<>();
        if(request.getAmenities() != null){
            for(String obj : request.getAmenities()){
                Amenities amenity = amenityRepository.findByAmenityName(obj);
                amenities.add(amenity);
            }
        }

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
                .adjustRating(0.0)
                .avgRating(0.0)
                .distanceToCenterKm(0.0)
                .amenities(amenities)
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
                 .avgRating(hotel.getAvgRating())
                 .totalReviews(hotel.getTotalReviews())
                 .amenities(hotel.getAmenities().stream()
                         .map(amenity ->
                                 AmenityResponse.builder()
                                         .amenityId(amenity.getAmenityId())
                                         .amenityName(amenity.getAmenityName())
                                         .amenityIcon(amenity.getAmenityIcon())
                                         .amenityDesc(amenity.getAmenityDesc()).build()).toList())
                 .build();
    }
}
