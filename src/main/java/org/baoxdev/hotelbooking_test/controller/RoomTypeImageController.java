package org.baoxdev.hotelbooking_test.controller;

import com.cloudinary.Api;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.baoxdev.hotelbooking_test.dto.ApiResponse;
import org.baoxdev.hotelbooking_test.dto.response.RoomTypeImageResponse;
import org.baoxdev.hotelbooking_test.repository.RoomTypeImageRepository;
import org.baoxdev.hotelbooking_test.service.impl.RoomTypeImageImpl;
import org.baoxdev.hotelbooking_test.service.interfaces.IRoomTypeImageService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE , makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("/roomTypeImage")
public class RoomTypeImageController {
    IRoomTypeImageService roomTypeImageService;


    @PostMapping("/create/{roomTypeId}")
    public ApiResponse<List<RoomTypeImageResponse>> createRoomTypeImage(
            @PathVariable String roomTypeId,
            @RequestParam("files") List<MultipartFile> files
    ) throws IOException {
        return ApiResponse.<List<RoomTypeImageResponse>>builder()
                .code(1000)
                .result(roomTypeImageService.uploadRoomImages(roomTypeId , files))
                .build();
    }

    @GetMapping("/get/{roomTypeId}")
    public ApiResponse<List<RoomTypeImageResponse>> getRoomTypeImage(
            @PathVariable String roomTypeId
    ){
        return ApiResponse.<List<RoomTypeImageResponse>>builder()
                .code(1000)
                .result(roomTypeImageService.getRoomTypeImages(roomTypeId))
                .build();
    }

    @DeleteMapping("delete/{roomTypeId}")
    public ApiResponse<Void> deleteRoomTypeImage(@PathVariable String roomTypeId){
        roomTypeImageService.deleteRoomTypeImage(roomTypeId);
        return ApiResponse.<Void> builder()
                .code(1000)
                .build();
    }

    @PutMapping("set_primary/{roomTypeId}/{roomTypeImageId}")
    public ApiResponse<Void> updateIsPrimary(
            @PathVariable String roomTypeId ,
            @PathVariable String roomTypeImageId
    ){
        roomTypeImageService.setPrimary(roomTypeId , roomTypeImageId);
        return ApiResponse.<Void>builder().build() ;
    }
}
