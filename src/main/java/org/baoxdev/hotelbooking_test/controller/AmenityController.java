package org.baoxdev.hotelbooking_test.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.baoxdev.hotelbooking_test.dto.ApiResponse;
import org.baoxdev.hotelbooking_test.dto.request.AmenityRequest;
import org.baoxdev.hotelbooking_test.dto.request.AmenityUpdateRequest;
import org.baoxdev.hotelbooking_test.dto.response.AmenityResponse;
import org.baoxdev.hotelbooking_test.service.interfaces.IAmenityService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/amenities")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE , makeFinal = true)
public class AmenityController {
    IAmenityService amenityService;

    @PostMapping("/create")
    ApiResponse<AmenityResponse> createAmenity(@RequestBody AmenityRequest request){

        return ApiResponse.<AmenityResponse>builder()
                .code(1000)
                .message("Tao amenity thanh cong")
                .result(amenityService.createAmenity(request))
                .build();
    }

    @GetMapping("/getAll")
    ApiResponse<List<AmenityResponse>> getListAmenity(){

        return ApiResponse.<List<AmenityResponse>>builder()
                .code(1000)
                .message("Lay tat ca amenity")
                .result(amenityService.getAllAmenityExists())
                .build();
    }

    @PutMapping("/update")
    ApiResponse<AmenityResponse> updateAmenity(@RequestBody AmenityUpdateRequest request){

        return ApiResponse.<AmenityResponse>builder()
                .code(1000)
                .message("Update amenity thanh cong")
                .result(amenityService.updateAmenity(request))
                .build();
    }

    @DeleteMapping("/delete/{amenityId}")
    ApiResponse<AmenityResponse> deleteAmenity(@PathVariable String amenityId){
        amenityService.deleteAmenity(amenityId);
        return ApiResponse.<AmenityResponse>builder()
                .message("Xoa thanh cong")
                .build();
    }


}
