package org.baoxdev.hotelbooking_test.controller;

import com.cloudinary.Api;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.baoxdev.hotelbooking_test.dto.ApiResponse;
import org.baoxdev.hotelbooking_test.dto.request.RoomTypeRequest;
import org.baoxdev.hotelbooking_test.dto.request.RoomTypeUpdateRequest;
import org.baoxdev.hotelbooking_test.dto.response.RoomTypeResponse;
import org.baoxdev.hotelbooking_test.service.impl.RoomTypeServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import tools.jackson.databind.json.JsonMapper;

import java.util.List;

@RestController
@RequestMapping("/roomType")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE , makeFinal = true)
public class RoomTypeController {
    RoomTypeServiceImpl roomTypeService;
    private final JsonMapper.Builder builder;

    @PostMapping("/create/{hotelId}")
    public ApiResponse<RoomTypeResponse> createRoomType(@PathVariable String hotelId , @RequestBody RoomTypeRequest request){
        return ApiResponse.<RoomTypeResponse>builder()
                .code(1000)
                .result(roomTypeService.createRoomType(hotelId , request))
                .build();
    }

    @GetMapping("/get/{roomTypeId}")
    public ApiResponse<RoomTypeResponse> getRoomTypeByRoomTypeId(@PathVariable String roomTypeId){
        return ApiResponse.<RoomTypeResponse>builder()
                .code(1000)
                .result(roomTypeService.getERoomTypeById(roomTypeId))
                .build();

    }

    @PutMapping("update/{roomTypeId}")
    public ApiResponse<RoomTypeResponse> updateRoomType(@PathVariable String roomTypeId , @RequestBody RoomTypeUpdateRequest request){
        return ApiResponse.<RoomTypeResponse>builder()
                .code(1000)
                .result(roomTypeService.updateRoomType(roomTypeId , request))
                .build();
    }

    @GetMapping("/getByHotelId/{hotelId}")
    public ApiResponse<List<RoomTypeResponse>> getAllByHotelId(@PathVariable String hotelId) {
        return ApiResponse.<List<RoomTypeResponse>>builder()
                .code(1000)
                .result(roomTypeService.getRoomTypeByHotelId(hotelId))
                .build();
    }


}
