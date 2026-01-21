package org.baoxdev.hotelbooking_test.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.baoxdev.hotelbooking_test.model.enums.HotelStatus;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HotelResponse {
     String hotelId;

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
