package org.baoxdev.hotelbooking_test.service.impl;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.baoxdev.hotelbooking_test.exception.AppException;
import org.baoxdev.hotelbooking_test.model.entity.RoomAvailability;
import org.baoxdev.hotelbooking_test.model.enums.ErrorCode;
import org.baoxdev.hotelbooking_test.repository.RoomAvailabilityRepository;
import org.baoxdev.hotelbooking_test.service.interfaces.IAvailabilityService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE , makeFinal = true)
@Slf4j(topic = "AVAILABILITY_SERVICE")
public class AvailabilityServiceImpl implements IAvailabilityService {
    RoomAvailabilityRepository roomAvailabilityRepository;
    DistributedLockService lockService;


    @Override
    public boolean checkAvailable(String roomTypeId, LocalDate checkOut, LocalDate checkIn, int quantity) {
        LocalDate endInclusive = checkOut.minusDays(1);

        //Lay ra cac availability trong khoang thoi gian reserve
        List<RoomAvailability> roomAvailabilities = roomAvailabilityRepository
                .findByRoomType_RoomTypeIdAndDateBetween(roomTypeId,
                        endInclusive , checkIn);

        //Phai co du availability tuong ung voi ngay
        long days = ChronoUnit.DAYS.between(checkIn , checkOut);
        if(roomAvailabilities.size() != days) return  false;

        return roomAvailabilities.stream().allMatch(available -> available.getAvailableCount() >= quantity);
    }

    @Transactional
    @Override
    public void reserve(String roomTypeId, LocalDate checkIn, LocalDate checkOut, int quantity){
        //Create lock key for specific roomType and date range
        String lockKey = buildAvailabilityLockKey(roomTypeId ,checkIn , checkOut );

        lockService.executeWithLock(lockKey , 10 , () -> {

            for (LocalDate d = checkIn; d.isBefore(checkOut); d = d.plusDays(1)) {

                int updated = roomAvailabilityRepository.tryReserveOneDay(roomTypeId, d, quantity);
                if (updated == 0) {
                    throw new AppException(ErrorCode.ROOM_AVAILABLE_NOT_ENOUGH);
                }
            }

            log.info("Successfully reserved {} rooms for roomType = {} , dates = {} to {}" ,
                    quantity , roomTypeId , checkIn , checkOut);
            return null;
        });
    }

    @Override
    public BigDecimal calculateTotalPrice(String roomTypeId, LocalDate checkIn, LocalDate checkOut, int quantity) {
        List<RoomAvailability> roomAvailabilities = roomAvailabilityRepository
                .findByRoomType_RoomTypeIdAndDateBetween(roomTypeId , checkOut ,checkIn);
        //Check availability co thieu ko
        long days = ChronoUnit.DAYS.between(checkIn , checkOut);
        if(roomAvailabilities.size() !=  days){
            throw new AppException(ErrorCode.ROOM_AVAILABLE_NOT_ENOUGH);
        }
        return roomAvailabilities.stream().map(r -> r.getPrice().multiply(BigDecimal.valueOf(quantity)))
                .reduce(BigDecimal.ZERO , BigDecimal::add);
    }

    @Transactional
    @Override
    public void release(String roomTypeId, LocalDate checkIn, LocalDate checkOut, int quantity) {
        for(LocalDate d = checkIn; d.isBefore(checkOut); d = d.plusDays(1)){
            roomAvailabilityRepository.releaseOneDay(roomTypeId , Date.valueOf(d) , quantity);
        }
    }

    private String buildAvailabilityLockKey(String roomTypeId, LocalDate checkIn, LocalDate checkOut) {
        return String.format("availability:roomType:%s:dates:%s:%s",
                roomTypeId, checkIn, checkOut);
    }

}
