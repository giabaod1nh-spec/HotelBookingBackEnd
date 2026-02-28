package org.baoxdev.hotelbooking_test.mapper;

import org.baoxdev.hotelbooking_test.dto.request.AmenityRequest;
import org.baoxdev.hotelbooking_test.dto.response.AmenityResponse;
import org.baoxdev.hotelbooking_test.model.entity.Amenities;
import org.springframework.stereotype.Component;

@Component
public class AmenityMapper {
    public Amenities convertAmenityFromResponse(AmenityRequest request){
        return   Amenities.builder()
                .amenityName(request.getAmenityName())
                .amenityIcon(request.getAmenityIcon())
                .amenityType(request.getAmenityType())
                .amenityDesc(request.getAmenityDesc())
                .isDeleted(false)
                .build();
    }

    public AmenityResponse convertResponseFromAmenity(Amenities amenity){

        return AmenityResponse.builder()
                .amenityId(amenity.getAmenityId())
                .amenityName(amenity.getAmenityName())
                .amenityIcon(amenity.getAmenityIcon())
                .amenityDesc(amenity.getAmenityDesc())
                .build();
    }
}
