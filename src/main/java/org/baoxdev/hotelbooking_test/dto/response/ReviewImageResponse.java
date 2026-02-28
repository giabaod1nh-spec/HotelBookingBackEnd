package org.baoxdev.hotelbooking_test.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReviewImageResponse {
    String reviewImageId;
    String reviewImageUrl;
}
