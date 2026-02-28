package org.baoxdev.hotelbooking_test.service.interfaces;

import org.baoxdev.hotelbooking_test.dto.response.BestPriceFromRoomTypeInHotelResponse;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface IPriceService {
    BestPriceFromRoomTypeInHotelResponse calculateBestPrice(String hotelId  , List<String> availabilityRoomTypeIds , LocalDate checkIn, LocalDate checkOut , int totalRooms);
}
