package org.baoxdev.hotelbooking_test.service.interfaces;

import org.baoxdev.hotelbooking_test.dto.request.RoomTypeRequest;
import org.baoxdev.hotelbooking_test.dto.request.RoomTypeUpdateRequest;
import org.baoxdev.hotelbooking_test.dto.response.RoomTypeResponse;

import java.util.List;

public interface IRoomTypeService {
    RoomTypeResponse createRoomType(String hotelId , RoomTypeRequest request);

    RoomTypeResponse getERoomTypeById(String roomTypeId);

    RoomTypeResponse updateRoomType (String roomTypeId , RoomTypeUpdateRequest request);

    List<RoomTypeResponse> getRoomTypeByHotelId(String hotelId);

    void deleteRoomType(String roomTypeId);
}
