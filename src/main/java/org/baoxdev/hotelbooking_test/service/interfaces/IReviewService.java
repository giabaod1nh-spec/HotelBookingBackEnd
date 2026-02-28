package org.baoxdev.hotelbooking_test.service.interfaces;

import org.baoxdev.hotelbooking_test.dto.request.ReviewRequest;
import org.baoxdev.hotelbooking_test.dto.response.ReviewResponse;
import org.baoxdev.hotelbooking_test.model.entity.Hotel;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IReviewService {
    ReviewResponse postReviewOfUser(ReviewRequest request , List<MultipartFile> files) throws IOException;

    Double calculatingAdjustedRating(double avgRating , double globalRating , int totalReviews , int m);

    void updateHotelRating(Hotel hotel , int newRating);
    List<ReviewResponse> getReviewBelongToHotel(String hotelId);
}
