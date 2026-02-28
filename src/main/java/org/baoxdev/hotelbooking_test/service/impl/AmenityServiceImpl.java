package org.baoxdev.hotelbooking_test.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.baoxdev.hotelbooking_test.dto.request.AmenityRequest;
import org.baoxdev.hotelbooking_test.dto.request.AmenityUpdateRequest;
import org.baoxdev.hotelbooking_test.dto.response.AmenityResponse;
import org.baoxdev.hotelbooking_test.exception.AppException;
import org.baoxdev.hotelbooking_test.mapper.AmenityMapper;
import org.baoxdev.hotelbooking_test.model.entity.Amenities;
import org.baoxdev.hotelbooking_test.model.enums.ErrorCode;
import org.baoxdev.hotelbooking_test.repository.AmenityRepository;
import org.baoxdev.hotelbooking_test.service.interfaces.IAmenityService;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE , makeFinal = true)
public class AmenityServiceImpl implements IAmenityService {
    AmenityRepository amenityRepository;
    AmenityMapper amenityMapper;

    @Override
    public AmenityResponse createAmenity(AmenityRequest request) {
        Amenities amenity = amenityMapper.convertAmenityFromResponse(request);

        return amenityMapper.convertResponseFromAmenity(amenityRepository.save(amenity));
    }

    @Override
    public List<AmenityResponse> getAllAmenityExists() {

        List<Amenities> amenities = amenityRepository.findAll();

        return  amenities.stream().map(amenity -> AmenityResponse.builder()
                .amenityName(amenity.getAmenityName()).amenityIcon(amenity.getAmenityIcon()).amenityDesc(amenity.getAmenityDesc()).build()
        ).toList();
    }

    @Override
    public AmenityResponse updateAmenity(AmenityUpdateRequest request) {

        Amenities amenity = amenityRepository.findById(request.getAmenityId())
                .orElseThrow(() -> new AppException(ErrorCode.AMENITY_NOT_FOUND));
        //Update amenity

        amenity.setAmenityDesc(request.getAmenityDesc());
        amenity.setAmenityName(request.getAmenityName());
        amenity.setAmenityIcon(request.getAmenityIcon());
        amenity.setAmenityType(request.getAmenityType());
        amenityRepository.save(amenity);

        return amenityMapper.convertResponseFromAmenity(amenity);
    }

    @Override
    public void deleteAmenity(String amenityId) {
        Amenities amenity = amenityRepository.findById(amenityId)
                .orElseThrow(() -> new AppException(ErrorCode.AMENITY_NOT_FOUND));
        //Set deleted to true
        amenity.setIsDeleted(true);
        //Save the change in amenity
        amenityRepository.save(amenity);
    }
}
