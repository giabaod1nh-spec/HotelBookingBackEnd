package org.baoxdev.hotelbooking_test.service.interfaces;

import org.baoxdev.hotelbooking_test.dto.request.RoomRequest;
import org.baoxdev.hotelbooking_test.dto.response.RoomResponse;

import java.util.List;

public interface IRoomService {
    RoomResponse createRome(String roomTypeId , RoomRequest roomRequest);
    RoomResponse getRomeInfo(String roomId);
    void deleteRoom(String roomId);
    RoomResponse updateRomeInfo(String roomId , RoomRequest request);

    List<RoomResponse> getAllPageable(int page , int size , String sortBy  , String direction);
}
