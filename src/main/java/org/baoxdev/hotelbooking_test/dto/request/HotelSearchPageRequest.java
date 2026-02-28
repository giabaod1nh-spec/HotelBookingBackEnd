package org.baoxdev.hotelbooking_test.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class HotelSearchPageRequest {
    String city;
    LocalDate checkIn ;
    LocalDate checkOut;
    int totalGuest;
    int totalRoom;

    //optional filter
    List<Integer> starRating;
    List<String> amenityIds;

    //pagination + sort
    Integer page;
    Integer size;
    String sortBy;  //price , score review . starRating , createdAt
    String direction;  //asc , desc
}
