package org.baoxdev.hotelbooking_test.mapper;

import lombok.RequiredArgsConstructor;
import org.baoxdev.hotelbooking_test.dto.request.RoomRequest;
import org.baoxdev.hotelbooking_test.dto.response.RoomResponse;
import org.baoxdev.hotelbooking_test.model.entity.Room;
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
                .roomStatus(request.getRoomStatus())
                .build();
        return  room;
    }

    public RoomResponse convertResponeFromRoom(Room room){
        RoomResponse response = RoomResponse
                .builder()
                .roomNumber(room.getRoomNumber())
                .roomStatus(room.getRoomStatus())
                .build();
        return  response;
    }
}
