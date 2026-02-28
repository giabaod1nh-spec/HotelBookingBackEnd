package org.baoxdev.hotelbooking_test.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ReviewRequest {
    String userId;

    String hotelId;

    String bookingId;

    Integer reviewRating;

    String reviewPositiveComment;

    String reviewNegativeComment;


}
