package org.baoxdev.hotelbooking_test.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.baoxdev.hotelbooking_test.dto.response.HotelImageResponse;
import org.baoxdev.hotelbooking_test.exception.AppException;
import org.baoxdev.hotelbooking_test.model.entity.Hotel;
import org.baoxdev.hotelbooking_test.model.entity.HotelImages;
import org.baoxdev.hotelbooking_test.model.enums.ErrorCode;
import org.baoxdev.hotelbooking_test.model.enums.HotelStatus;
import org.baoxdev.hotelbooking_test.repository.HotelImageRepository;
import org.baoxdev.hotelbooking_test.repository.HotelRepository;
import org.baoxdev.hotelbooking_test.service.interfaces.IHotelImageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE , makeFinal = true)
@RequiredArgsConstructor
public class HotelImageImpl implements IHotelImageService {
    HotelRepository hotelRepository;
    HotelImageRepository hotelImageRepository;
    CloudinaryService cloudinaryService;

    @Override
    public List<HotelImageResponse> uploadHotelImages(String hotelId, List<MultipartFile> files) throws IOException {
        //Find hotel
        Hotel hotel = hotelRepository.findById(hotelId).orElseThrow(() ->
                new AppException(ErrorCode.HOTEL_NOT_FOUND));

        //Check xem hotel co image chua
        List<HotelImages> existingImages = hotelImageRepository.findByHotel_HotelId(hotelId);
        boolean isFirstImage = existingImages.isEmpty();

        List<HotelImageResponse> responses = new ArrayList<>();

        //Upload tung file
        for(MultipartFile file: files){
            if(file.isEmpty()) continue;

        //Validate file type
        //String contentType = file.getContentType();
        //if(contentType == null || contentType.startsWith("image/")){
            //throw new AppException(ErrorCode.FILE_MUST_BE_IMAGE);
        //}
            String imageUrl = cloudinaryService.upLoadFile(file);

        //Create hotelImage
            HotelImages hotelImages = HotelImages.builder()
                    .hotel(hotel)
                    .hotelImageUrl(imageUrl)
                    .isPrimary(isFirstImage)
                    .build();

            hotelImageRepository.save(hotelImages);

            //Tra ve 1 list HotelImageResponse
            responses.add(HotelImageResponse.builder()
                            .hotelImageId(hotelImages.getHotelImageId())
                            .hotelImageUrl(hotelImages.getHotelImageUrl())
                            .isPrimary(hotelImages.getIsPrimary())
                    .build());
            isFirstImage = false;
            //Tu anh so 2 tro di ko phai anh chinh
        }
        return responses;
    }

    @Override
    public List<HotelImageResponse> getHotelImages(String hotelId) {
        List<HotelImages> images = hotelImageRepository.findByHotel_HotelId(hotelId);

       return images.stream().map(image ->
                HotelImageResponse.builder()
                        .hotelImageUrl(image.getHotelImageUrl()).isPrimary(image.getIsPrimary())
                        .build()
                ).toList();
    }

    @Override
    public void deleteHotelImage(String hotelImageId) {
        hotelImageRepository.deleteById(hotelImageId);
    }

    @Override
    public void setPrimaryImage(String hotelId, String hotelImageId) {
        //Find all image
        List<HotelImages> allImages  = hotelImageRepository.findByHotel_HotelId(hotelImageId);

        //  Set all img to false
        allImages.forEach(image -> image.setIsPrimary(false));

        //Selected one to true
        HotelImages primaryImage = allImages.stream()
                .filter(img -> img.getHotelImageId().equals(hotelImageId))
                .findFirst()
                .orElseThrow(() -> new AppException(ErrorCode.IMAGE_NOT_FOUND));

        primaryImage.setIsPrimary(true);
        hotelImageRepository.saveAll(allImages);
    }
}
