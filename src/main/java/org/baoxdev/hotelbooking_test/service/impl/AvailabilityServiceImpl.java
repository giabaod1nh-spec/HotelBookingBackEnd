package org.baoxdev.hotelbooking_test.service.impl;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.baoxdev.hotelbooking_test.exception.AppException;
import org.baoxdev.hotelbooking_test.model.entity.RoomAvailability;
import org.baoxdev.hotelbooking_test.model.enums.ErrorCode;
import org.baoxdev.hotelbooking_test.repository.RoomAvailabilityRepository;
import org.baoxdev.hotelbooking_test.service.interfaces.IAvailabilityService;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE , makeFinal = true)
public class AvailabilityServiceImpl implements IAvailabilityService {
    RoomAvailabilityRepository roomAvailabilityRepository;


    @Override
    public boolean checkAvailable(String roomTypeId, LocalDate checkOut, LocalDate checkIn, int quantity) {
        //Lay ra cac availability trong khoang thoi gian reserve
        List<RoomAvailability> roomAvailabilities = roomAvailabilityRepository
                .findByRoomType_RoomTypeIdAndDateBetween(roomTypeId,
                        Date.from(checkOut.atStartOfDay(ZoneId.systemDefault()).toInstant()),
                        Date.from(checkIn.atStartOfDay(ZoneId.systemDefault()).toInstant()));

        //Phai co du availability tuong ung voi ngay
        long days = ChronoUnit.DAYS.between(checkIn , checkOut);
        if(roomAvailabilities.size() != days) return  false;

        return roomAvailabilities.stream().allMatch(available -> available.getAvailableCount() >= quantity);
    }

    @Transactional
    @Override
    public void reserve(String roomTypeId, LocalDate checkOut, LocalDate checkin, int quantity) {
         for(LocalDate d = checkin; d.isBefore(checkOut); d.plusDays(1)){

             int updated = roomAvailabilityRepository.tryReserveOneDay(roomTypeId , d , quantity);
             if(updated == 0){
                 throw new AppException(ErrorCode.ROOM_AVAILABLE_NOT_ENOUGH);
             }
        }
    }
}
