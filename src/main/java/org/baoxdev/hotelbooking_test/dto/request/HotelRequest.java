package org.baoxdev.hotelbooking_test.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.baoxdev.hotelbooking_test.model.enums.HotelStatus;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HotelRequest {
    String hotelName;

    String hotelDescription;

    String hotelAddress;

    String hotelCity;

    String hotelCountry;

    String hotelPhone;

    String hotelEmail;

    Integer starRating;

    HotelStatus hotelStatus;
}
