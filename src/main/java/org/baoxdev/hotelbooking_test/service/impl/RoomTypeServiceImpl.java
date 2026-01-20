package org.baoxdev.hotelbooking_test.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.baoxdev.hotelbooking_test.dto.request.RoomTypeRequest;
import org.baoxdev.hotelbooking_test.dto.request.RoomTypeUpdateRequest;
import org.baoxdev.hotelbooking_test.dto.response.RoomTypeResponse;
import org.baoxdev.hotelbooking_test.exception.AppException;
import org.baoxdev.hotelbooking_test.mapper.RoomTypeMapper;
import org.baoxdev.hotelbooking_test.model.entity.Hotel;
import org.baoxdev.hotelbooking_test.model.entity.RoomType;
import org.baoxdev.hotelbooking_test.model.enums.ErrorCode;
import org.baoxdev.hotelbooking_test.model.enums.RoomTypeStatus;
import org.baoxdev.hotelbooking_test.repository.HotelRepository;
import org.baoxdev.hotelbooking_test.repository.RoomTypeRepository;
import org.baoxdev.hotelbooking_test.service.interfaces.IRoomTypeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE , makeFinal = true)
@RequiredArgsConstructor
public class RoomTypeServiceImpl implements IRoomTypeService {
    RoomTypeMapper roomTypeMapper;
    RoomTypeRepository roomTypeRepository;
    HotelRepository hotelRepository;

    @Override
    public RoomTypeResponse createRoomType(String hotelId , RoomTypeRequest request) {
        RoomType roomType = roomTypeMapper.convertRoomTypeFromRequest(request);

        Hotel hotel = hotelRepository.findById(hotelId).orElseThrow(() ->
                new AppException(ErrorCode.HOTEL_NOT_FOUND));
        roomType.setHotel(hotel);
        return roomTypeMapper.convertResponseFromRoomType(roomTypeRepository.save(roomType));
    }

    @Override
    public RoomTypeResponse getERoomTypeById(String roomTypeId) {
        RoomType roomType = roomTypeRepository.findById(roomTypeId).orElseThrow(() ->
                new AppException(ErrorCode.ROOM_TYPE_NOT_FOUND));

        return roomTypeMapper.convertResponseFromRoomType(roomType);
    }

    @Override
    public RoomTypeResponse updateRoomType(String roomTypeId, RoomTypeUpdateRequest request) {
        RoomType roomType = roomTypeRepository.findById(roomTypeId).orElseThrow(() ->
                new AppException(ErrorCode.ROOM_TYPE_NOT_FOUND));

        roomType.setRoomTypeName(request.getRoomTypeName());
        roomType.setRoomTypeDesc(request.getRoomTypeDesc());
        roomType.setBasePrice(request.getBasePrice());
        roomType.setMaxOccupy(request.getMaxOccupy());
        roomType.setTotalRooms(request.getTotalRooms());

        return roomTypeMapper.convertResponseFromRoomType(roomTypeRepository.save(roomType));
    }


    @Override
    public List<RoomTypeResponse> getRoomTypeByHotelId(String hotelId) {
        List<RoomType> roomTypes = roomTypeRepository.findByHotel_HotelId(hotelId);

        return roomTypes.stream().map(roomType ->  roomTypeMapper.convertResponseFromRoomType(roomType)).toList();
    }

    @Override
    public void deleteRoomType(String roomTypeId) {
        RoomType roomType = roomTypeRepository.findById(roomTypeId).orElseThrow(() ->
                new AppException(ErrorCode.ROOM_TYPE_NOT_FOUND));

        roomType.setRoomTypeStatus(RoomTypeStatus.INACTIVE);
        roomTypeRepository.save(roomType);
    }

}
