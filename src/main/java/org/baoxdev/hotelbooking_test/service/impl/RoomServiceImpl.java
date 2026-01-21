package org.baoxdev.hotelbooking_test.service.impl;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.baoxdev.hotelbooking_test.dto.request.RoomRequest;
import org.baoxdev.hotelbooking_test.dto.response.RoomResponse;
import org.baoxdev.hotelbooking_test.exception.AppException;
import org.baoxdev.hotelbooking_test.mapper.RoomMapper;
import org.baoxdev.hotelbooking_test.model.entity.Room;
import org.baoxdev.hotelbooking_test.model.entity.RoomType;
import org.baoxdev.hotelbooking_test.model.enums.ErrorCode;
import org.baoxdev.hotelbooking_test.model.enums.RoomStatus;
import org.baoxdev.hotelbooking_test.repository.RoomRepository;
import org.baoxdev.hotelbooking_test.repository.RoomTypeRepository;
import org.baoxdev.hotelbooking_test.service.interfaces.IRoomService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE  , makeFinal = true)
@Slf4j
public class RoomServiceImpl implements IRoomService {
    RoomMapper roomMapper;
    RoomRepository roomRepository;
    RoomTypeRepository roomTypeRepository;

    @Transactional
    @Override
    public RoomResponse createRome(String roomTypeId , RoomRequest request) {
        Room room = roomMapper.convertRomeFromRequest(request);

        RoomType roomType = roomTypeRepository.findById(roomTypeId).orElseThrow(() ->
                new AppException(ErrorCode.ROOM_TYPE_NOT_FOUND));

        room.setRoomType(roomType);

        return  roomMapper.convertResponeFromRoom(roomRepository.save(room));
    }

    @Override
    public RoomResponse getRomeInfo(String roomId) {
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new AppException(ErrorCode.ROOM_NOT_FOUND));

        return roomMapper.convertResponeFromRoom(room);
    }

    @Override
    public void deleteRoom(String roomId) {
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new AppException(ErrorCode.ROOM_NOT_FOUND));

        room.setRoomStatus(RoomStatus.MAINTENANCE);
    }

    @Override
    public RoomResponse updateRomeInfo(String roomId , RoomRequest request) {
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new AppException(ErrorCode.ROOM_NOT_FOUND));

        room.setRoomNumber(request.getRoomNumber());
        room.setRoomStatus(request.getRoomStatus());
        return roomMapper.convertResponeFromRoom(roomRepository.save(room)) ;
    }

    @Override
    public List<RoomResponse> getAllPageable(int page, int size, String sortBy, String direction) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortBy) , sortBy);

        Pageable pageable = PageRequest.of(page , size , sort);

        return roomRepository.findAll(pageable).stream().map(room -> roomMapper.convertResponeFromRoom(room)).toList();
    }

}
