package org.baoxdev.hotelbooking_test.service.impl;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.baoxdev.hotelbooking_test.dto.request.RoomTypeRequest;
import org.baoxdev.hotelbooking_test.dto.request.RoomTypeUpdateRequest;
import org.baoxdev.hotelbooking_test.dto.response.RoomTypeResponse;
import org.baoxdev.hotelbooking_test.exception.AppException;
import org.baoxdev.hotelbooking_test.mapper.RoomTypeMapper;
import org.baoxdev.hotelbooking_test.model.entity.Hotel;
import org.baoxdev.hotelbooking_test.model.entity.RoomAvailability;
import org.baoxdev.hotelbooking_test.model.entity.RoomType;
import org.baoxdev.hotelbooking_test.model.enums.ErrorCode;
import org.baoxdev.hotelbooking_test.model.enums.RoomTypeStatus;
import org.baoxdev.hotelbooking_test.repository.HotelRepository;
import org.baoxdev.hotelbooking_test.repository.RoomAvailabilityRepository;
import org.baoxdev.hotelbooking_test.repository.RoomTypeRepository;
import org.baoxdev.hotelbooking_test.service.interfaces.IRoomTypeService;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE , makeFinal = true)
@RequiredArgsConstructor
public class RoomTypeServiceImpl implements IRoomTypeService {
    RoomTypeMapper roomTypeMapper;
    RoomTypeRepository roomTypeRepository;
    HotelRepository hotelRepository;
    RoomAvailabilityRepository roomAvailabilityRepository;

    @Transactional
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

    @Transactional
    @Override
    public RoomTypeResponse updateRoomType(String roomTypeId, RoomTypeUpdateRequest request) {
        RoomType roomType = roomTypeRepository.findById(roomTypeId).orElseThrow(() ->
                new AppException(ErrorCode.ROOM_TYPE_NOT_FOUND));

        roomType.setRoomTypeName(request.getRoomTypeName());
        roomType.setRoomTypeDesc(request.getRoomTypeDesc());
        roomType.setBasePrice(request.getBasePrice());
        roomType.setMaxOccupy(request.getMaxOccupy());
        roomType.setTotalRooms(request.getTotalRooms());

        roomTypeRepository.save(roomType);
        //Generate availability to next 90 days
        generateAvailabilityIfMissing(roomType , LocalDate.now() , LocalDate.now().plusDays(90));

        return roomTypeMapper.convertResponseFromRoomType(roomType);
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

    private void generateAvailabilityIfMissing(RoomType roomType , LocalDate start , LocalDate end){
        List<RoomAvailability>  roomAvailabilities = new ArrayList<>();

        for(LocalDate d = start ; d.isBefore(end) ; d.plusDays(1)){
            boolean exists = roomAvailabilityRepository.existsByRoomType_RoomTypeIdAndDate(roomType.getRoomTypeId()
            , Date.from(d.atStartOfDay(ZoneId.systemDefault()).toInstant()));

            if (exists) continue;

            roomAvailabilities.add(RoomAvailability.builder()
                            .roomType(roomType)
                            .date(Date.from(d.atStartOfDay(ZoneId.systemDefault()).toInstant()))
                            .availableCount(roomType.getTotalRooms())
                            .price(roomType.getBasePrice())
                            .version(0)
                    .build());
        }

        if(!roomAvailabilities.isEmpty()){
            roomAvailabilityRepository.saveAll(roomAvailabilities);
        }
    }

}
