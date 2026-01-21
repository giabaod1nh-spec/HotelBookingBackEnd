package org.baoxdev.hotelbooking_test.service.interfaces;

import java.time.LocalDate;

public interface IAvailabilityService {
    boolean checkAvailable(String roomTypeId , LocalDate checkOut , LocalDate checkIn , int quantity);

    void reserve(String roomTypeId , LocalDate checkOut , LocalDate checkin , int quantity);
}
