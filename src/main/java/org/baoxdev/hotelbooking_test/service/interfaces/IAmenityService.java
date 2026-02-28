package org.baoxdev.hotelbooking_test.service.interfaces;

import org.baoxdev.hotelbooking_test.dto.request.AmenityRequest;
import org.baoxdev.hotelbooking_test.dto.request.AmenityUpdateRequest;
import org.baoxdev.hotelbooking_test.dto.response.AmenityResponse;
import org.springframework.stereotype.Service;

import java.util.List;

public interface IAmenityService {
    AmenityResponse createAmenity(AmenityRequest request);

    List<AmenityResponse> getAllAmenityExists();

    AmenityResponse updateAmenity(AmenityUpdateRequest request);

    void deleteAmenity(String amenityId);
}
