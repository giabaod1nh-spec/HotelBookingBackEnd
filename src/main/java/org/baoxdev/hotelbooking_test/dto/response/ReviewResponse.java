package org.baoxdev.hotelbooking_test.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReviewResponse {
    String reviewId;

    Integer reviewRating;

    String reviewPositiveComment;

    String reviewNegativeComment;

    String userName;

    Instant createdAt;

    List<ReviewImageResponse> reviewImagesList;
}
