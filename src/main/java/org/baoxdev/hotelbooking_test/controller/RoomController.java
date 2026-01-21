package org.baoxdev.hotelbooking_test.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.baoxdev.hotelbooking_test.dto.ApiResponse;
import org.baoxdev.hotelbooking_test.dto.request.RoomRequest;
import org.baoxdev.hotelbooking_test.dto.response.RoomResponse;
import org.baoxdev.hotelbooking_test.service.impl.RoomServiceImpl;
import org.baoxdev.hotelbooking_test.service.interfaces.IRoomService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE , makeFinal = true)
@RequestMapping("/room")
public class RoomController {
    IRoomService roomService;

    @PostMapping("/create/{roomTypeId}")
    public ApiResponse<RoomResponse> createRoom (@PathVariable String roomTypeId , @RequestBody  RoomRequest request){
        return ApiResponse.<RoomResponse>builder()
                .code(1000)
                .result(roomService.createRome(roomTypeId , request))
                .build();
    }

    @GetMapping("/getInfo/{roomId}")
    public ApiResponse<RoomResponse> getInfoRoom(@PathVariable String roomId){
        return  ApiResponse.<RoomResponse>builder()
                .code(1000)
                .result(roomService.getRomeInfo(roomId))
                .build();
    }


    @GetMapping("/getPagination")
    public ApiResponse<List<RoomResponse>> getListRoomPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction
    ){
        return ApiResponse.<List<RoomResponse>>builder()
                .code(1000)
                .result(roomService.getAllPageable(page , size , sortBy , direction))
                .build();
    }
    

    
}
