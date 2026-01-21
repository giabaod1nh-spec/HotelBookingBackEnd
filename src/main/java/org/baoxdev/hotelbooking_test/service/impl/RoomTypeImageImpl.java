package org.baoxdev.hotelbooking_test.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.baoxdev.hotelbooking_test.dto.response.HotelImageResponse;
import org.baoxdev.hotelbooking_test.dto.response.RoomTypeImageResponse;
import org.baoxdev.hotelbooking_test.exception.AppException;
import org.baoxdev.hotelbooking_test.model.entity.RoomType;
import org.baoxdev.hotelbooking_test.model.entity.RoomTypeImages;
import org.baoxdev.hotelbooking_test.model.enums.ErrorCode;
import org.baoxdev.hotelbooking_test.repository.RoomTypeImageRepository;
import org.baoxdev.hotelbooking_test.repository.RoomTypeRepository;
import org.baoxdev.hotelbooking_test.service.interfaces.IRoomTypeImageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE , makeFinal = true)
public class RoomTypeImageImpl implements IRoomTypeImageService {
    RoomTypeImageRepository roomTypeImageRepository;
    RoomTypeRepository roomTypeRepository;
    CloudinaryService cloudinaryService;

    @Override
    public List<RoomTypeImageResponse> uploadRoomImages(String roomTypeId, List<MultipartFile> files) throws IOException {
        //Tim roomType de gan vao

        RoomType roomType = roomTypeRepository.findById(roomTypeId).orElseThrow(() ->
                new AppException(ErrorCode.ROOM_TYPE_NOT_FOUND));

        //Check xem roomType co image chua

        List<RoomTypeImages> existingImages = roomTypeImageRepository.findByRoomType_RoomTypeId(roomTypeId);

        boolean isFirstImage = existingImages.isEmpty();

        List<RoomTypeImageResponse> responses = new ArrayList<>();
        //Upload tung file
        for(MultipartFile file : files){
            String imageUrl = cloudinaryService.upLoadFile(file);

            //Create first RoomTypeImage
            RoomTypeImages roomTypeImages = RoomTypeImages.builder()
                    .roomImageUrl(imageUrl)
                    .isPrimary(isFirstImage)
                    .roomType(roomType)
                    .build();

            roomTypeImageRepository.save(roomTypeImages);

            //Create response luon
            responses.add(RoomTypeImageResponse.builder()
                    .roomTypeImageId(roomTypeImages.getRoomTypeImageId())
                    .roomImageUrl(roomTypeImages.getRoomImageUrl())
                    .isPrimary(roomTypeImages.getIsPrimary())
                    .build()

            );

            isFirstImage = false;
        }

        return responses;
    }

    @Override
    public List<RoomTypeImageResponse> getRoomTypeImages(String roomTypeId) {
        List<RoomTypeImages> images = roomTypeImageRepository.findByRoomType_RoomTypeId(roomTypeId);

        return  images.stream().map(image -> RoomTypeImageResponse.builder()
                .roomTypeImageId(image.getRoomTypeImageId())
                .roomImageUrl(image.getRoomImageUrl())
                .isPrimary(image.getIsPrimary())
                .build()).toList();
    }

    @Override
    public void deleteRoomTypeImage(String roomTypeImageId) {
            roomTypeImageRepository.deleteById(roomTypeImageId);
    }

    @Override
    public void setPrimary(String roomTypeId, String roomTypeImageId) {
         //Find all image
        List<RoomTypeImages> images = roomTypeImageRepository.findByRoomType_RoomTypeId(roomTypeId);

        //Set all isPrimary to false
        images.forEach(image -> image.setIsPrimary(false));

        //Search image equals roomTypeImageId to set
        RoomTypeImages primary = images.stream().filter(image -> image.getRoomTypeImageId().equals(roomTypeImageId))
                .findFirst()
                .orElseThrow(() -> new AppException(ErrorCode.ROOM_TYPE_IMAGE_NOT_FOUND));

        primary.setIsPrimary(true);
        roomTypeImageRepository.saveAll(images);
    }
}
