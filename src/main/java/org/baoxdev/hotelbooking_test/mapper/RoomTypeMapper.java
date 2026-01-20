package org.baoxdev.hotelbooking_test.mapper;

import org.baoxdev.hotelbooking_test.dto.request.RoomTypeRequest;
import org.baoxdev.hotelbooking_test.dto.response.RoomTypeResponse;
import org.baoxdev.hotelbooking_test.model.entity.RoomType;
import org.baoxdev.hotelbooking_test.model.enums.RoomTypeStatus;
import org.springframework.stereotype.Component;

@Component
public class RoomTypeMapper {

    public RoomType convertRoomTypeFromRequest(RoomTypeRequest request){
        RoomType roomType = RoomType.builder()
                .roomTypeName(request.getRoomTypeName())
                .roomTypeDesc(request.getRoomTypeDesc())
                .basePrice(request.getBasePrice())
                .maxOccupy(request.getMaxOccupy())
                .totalRooms(request.getTotalRooms())
                .roomTypeStatus(RoomTypeStatus.ACTIVE)
                .build();


        return roomType;
    }


    public RoomTypeResponse convertResponseFromRoomType(RoomType roomType){
        RoomTypeResponse response = RoomTypeResponse.builder()
                .roomTypeId(roomType.getRoomTypeId())
                .roomTypeName(roomType.getRoomTypeName())
                .roomTypeDesc(roomType.getRoomTypeDesc())
                .basePrice(roomType.getBasePrice())
                .maxOccupy(roomType.getMaxOccupy())
                .totalRooms(roomType.getTotalRooms())
                .build();

        return  response;
    }
}
