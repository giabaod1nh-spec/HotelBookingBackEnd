package org.baoxdev.hotelbooking_test.mapper;

import lombok.RequiredArgsConstructor;
import org.baoxdev.hotelbooking_test.dto.request.RoomRequest;
import org.baoxdev.hotelbooking_test.dto.response.RoomResponse;
import org.baoxdev.hotelbooking_test.model.entity.Room;
import org.baoxdev.hotelbooking_test.model.enums.RoomStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoomMapper {

    public Room convertRomeFromRequest(RoomRequest request){
        Room room = Room.builder()
                //.hotel()
                //.roomType()
                //.bookingRooms()
                .roomNumber(request.getRoomNumber())
                .roomStatus(RoomStatus.AVAILABLE)
                .build();
        return  room;
    }

    public RoomResponse convertResponeFromRoom(Room room){
        RoomResponse response = RoomResponse
                .builder()
                .roomId(room.getRoomId())
                .roomNumber(room.getRoomNumber())
                .roomStatus(room.getRoomStatus())
                .build();
        return  response;
    }
}
