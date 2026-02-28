package org.baoxdev.hotelbooking_test.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class HotelSearchRequest {
    LocalDate checkIn ;
    LocalDate checkOut;
    Integer numGuests;
    Integer rooms ;
    String city ;
    String country;
    Integer starRating;
    List<String> amenityIds;
    BigDecimal minPrice;
    BigDecimal maxPrice;
    int page;
    int size;
    String sortBy;
    String direction;
}
