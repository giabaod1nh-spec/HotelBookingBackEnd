package org.baoxdev.hotelbooking_test.service.interfaces;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface IAvailabilityService {
    boolean checkAvailable(String roomTypeId , LocalDate checkOut , LocalDate checkIn , int quantity);

    void reserve(String roomTypeId , LocalDate checkIn , LocalDate checkOut , int quantity);

    BigDecimal calculateTotalPrice(String roomTypeId , LocalDate checkIn , LocalDate checkOut , int quantity);

   void release(String roomTypeId , LocalDate checkIn , LocalDate checkOut , int quantity);
}
