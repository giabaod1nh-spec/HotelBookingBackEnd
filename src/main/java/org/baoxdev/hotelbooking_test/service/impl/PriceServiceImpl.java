package org.baoxdev.hotelbooking_test.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.baoxdev.hotelbooking_test.dto.response.BestPriceFromRoomTypeInHotelResponse;
import org.baoxdev.hotelbooking_test.exception.AppException;
import org.baoxdev.hotelbooking_test.model.entity.RoomType;
import org.baoxdev.hotelbooking_test.model.enums.ErrorCode;
import org.baoxdev.hotelbooking_test.repository.RoomTypeRepository;
import org.baoxdev.hotelbooking_test.service.interfaces.IAvailabilityService;
import org.baoxdev.hotelbooking_test.service.interfaces.IPriceService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE , makeFinal = true)
public class PriceServiceImpl implements IPriceService {
    IAvailabilityService availabilityService;
    RoomTypeRepository roomTypeRepository;
    @Override
    public BestPriceFromRoomTypeInHotelResponse calculateBestPrice(String hotelId, List<String> availabilityRoomTypeIds, LocalDate checkIn, LocalDate checkOut, int totalRooms) {

        BigDecimal bestPrice = null;
        String bestRoomTypeId = null;
        for(String roomTypeId : availabilityRoomTypeIds){
           BigDecimal basePrice =  availabilityService.calculateTotalPrice(roomTypeId , checkIn , checkOut , totalRooms);

           if(bestPrice == null || basePrice.compareTo(bestPrice) < 0 ){
                   bestPrice = basePrice;
                   bestRoomTypeId = roomTypeId;
           }
        }

        RoomType roomType = roomTypeRepository.findById(bestRoomTypeId)
                .orElseThrow(() -> new AppException(ErrorCode.ROOM_TYPE_NOT_FOUND));

        return BestPriceFromRoomTypeInHotelResponse.builder()
                .roomTypeName(roomType.getRoomTypeName())
                .bedType(roomType.getBedSummary())
                .bestPrice(bestPrice)
                .build();
    }
}
