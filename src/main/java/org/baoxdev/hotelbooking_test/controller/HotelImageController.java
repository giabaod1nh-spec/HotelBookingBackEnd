package org.baoxdev.hotelbooking_test.controller;

import com.cloudinary.Api;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.baoxdev.hotelbooking_test.dto.ApiResponse;
import org.baoxdev.hotelbooking_test.dto.response.HotelImageResponse;
import org.baoxdev.hotelbooking_test.repository.HotelImageRepository;
import org.baoxdev.hotelbooking_test.service.impl.HotelImageImpl;
import org.baoxdev.hotelbooking_test.service.interfaces.IHotelImageService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/images")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE , makeFinal = true)
public class HotelImageController {
    IHotelImageService hotelImageService;

    @PostMapping("/upload/{hotelId}")
    public ApiResponse<List<HotelImageResponse>> uploadHotelImage(
            @PathVariable String hotelId,
            @RequestParam("files") List<MultipartFile> files) throws IOException {
        return ApiResponse.<List<HotelImageResponse>>builder()
                .code(1000)
                .result(hotelImageService.uploadHotelImages(hotelId , files))
                .build();
    }

    @GetMapping("/getImage/{hotelId}")
    public ApiResponse<List<HotelImageResponse>> getHotelImage(@PathVariable String hotelId){
        return ApiResponse.<List<HotelImageResponse>>builder()
                .code(1000)
                .result(hotelImageService.getHotelImages(hotelId))
                .build();

    }

    @DeleteMapping("/delete/{imageId}")
    public ApiResponse<Void> deleteImage(@PathVariable String imageId){
        hotelImageService.deleteHotelImage(imageId);
        return ApiResponse.<Void>builder().build();
    }

    @PutMapping("set-primary/{hotelId}/{imageId}")
    public ApiResponse<Void> setPrimaryForImage(
            @PathVariable String hotelId ,
            @PathVariable String imageId
    ){
      hotelImageService.setPrimaryImage(hotelId , imageId);
      return ApiResponse.<Void>builder()
              .code(1000)
              .build();
    }
}
