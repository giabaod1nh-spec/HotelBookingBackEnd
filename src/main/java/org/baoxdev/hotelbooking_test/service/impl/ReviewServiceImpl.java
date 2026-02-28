package org.baoxdev.hotelbooking_test.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.baoxdev.hotelbooking_test.dto.request.ReviewRequest;
import org.baoxdev.hotelbooking_test.dto.response.CloudinarySaveResponse;
import org.baoxdev.hotelbooking_test.dto.response.ReviewImageResponse;
import org.baoxdev.hotelbooking_test.dto.response.ReviewResponse;
import org.baoxdev.hotelbooking_test.exception.AppException;
import org.baoxdev.hotelbooking_test.model.entity.*;
import org.baoxdev.hotelbooking_test.model.enums.BookingStatus;
import org.baoxdev.hotelbooking_test.model.enums.ErrorCode;
import org.baoxdev.hotelbooking_test.repository.*;
import org.baoxdev.hotelbooking_test.service.interfaces.IReviewService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE , makeFinal = true)
public class ReviewServiceImpl implements IReviewService {
    HotelRepository hotelRepository;
    CloudinaryService cloudinaryService;
    UserRepository userRepository;
    BookingRepository bookingRepository;
    ReviewRepository reviewRepository;
    ReviewImageRepository reviewImageRepository;

    @Override
    public ReviewResponse postReviewOfUser(ReviewRequest request, List<MultipartFile> files) throws IOException {
        //Search va lay ra hotelId
        Hotel hotel = hotelRepository.findById(request.getHotelId())
                .orElseThrow(() -> new AppException(ErrorCode.HOTEL_NOT_FOUND));

        //Search va lay ra user
        User user = userRepository.findUserByUserName(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        //Search va lay ra booking

        Booking booking  = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new AppException(ErrorCode.BOOKING_NOT_FOUND));

        //Neu booking chua CHECK_OUT chua cho review
        if(!booking.getBookingStatus().equals(BookingStatus.CHECK_OUT)){
            throw new AppException(ErrorCode.REVIEW_AFTER_BOOKING_COMPLETED);
        }


        //Booking phai thuoc user hien tai
        if(!booking.getUser().getUserId().equals(user.getUserId())){
            throw new AppException(ErrorCode.BOOKING_ACCESS_DENIED);
        }

        //Tao review
        Review review = Review.builder()

                .reviewRating(request.getReviewRating())
                .reviewPositiveComment(request.getReviewPositiveComment())
                .reviewNegativeComment(request.getReviewNegativeComment())
                .user(user)
                .hotel(hotel)
                .booking(booking)
                .build();

        //Update hotel Rating using new Rating
        updateHotelRating(hotel , review.getReviewRating());


        List<ReviewImageResponse> responses = new ArrayList<>();

        //Tao tung review images
        for(MultipartFile file : files){
            if(file.isEmpty()) continue;

            //Upload image len tren cloudinary
            org.baoxdev.hotelbooking_test.dto.response.CloudinarySaveResponse response = cloudinaryService.upLoadFile(file);

            //Create ReviewImage Entity
            ReviewImages img = ReviewImages.builder()
                    .review(review)
                    .imageUrl(response.getImageUrl())
                    .build();

            reviewImageRepository.save(img);

            //Add reviewImage vao review va tra ve 1 list
            responses.add(ReviewImageResponse.builder()
                            .reviewImageId(img.getReviewImageId())
                            .reviewImageUrl(img.getImageUrl())
                    .build());
        }

        reviewRepository.save(review);

        return ReviewResponse.builder()
                .reviewId(review.getReviewId())
                .reviewRating(review.getReviewRating())
                .reviewPositiveComment(review.getReviewPositiveComment())
                .reviewNegativeComment(review.getReviewNegativeComment())
                .userName(user.getUserName())
                .createdAt(review.getCreatedAt())
                .reviewImagesList(responses)
                .build();
    }

    @Override
    public Double calculatingAdjustedRating(double avgRating, double globalAvgRating, int totalReviews, int m) {

        if(totalReviews == 0) return globalAvgRating;

        return (totalReviews / (totalReviews + m)) * avgRating +
                (m / (totalReviews +m)) * globalAvgRating;
    }

    @Override
    public void updateHotelRating(Hotel hotel, int newRating) {
        double oldAvg = hotel.getAvgRating();
        int oldReviewCount = hotel.getTotalReviews();
        //Update avg toi uu pe  rformance thay vi sql : select avg()

        double newAvg = (oldAvg + newRating) / (oldReviewCount + 1);

        int newCount = oldReviewCount + 1 ;
        //global average rating
        Double globalAverage = reviewRepository.getGloBalAverageRating();
        if(globalAverage == null){
           globalAverage = 0.0 ;
        }

        //Tinh lai adjustedRating
        int m = 50 ;

        double adjusted = calculatingAdjustedRating(newAvg , globalAverage , newCount , m);
        //Save cac du lieu moi vao hotel
        hotel.setAvgRating(newAvg);
        hotel.setTotalReviews(newCount);
        hotel.setAdjustRating(adjusted);
        hotelRepository.save(hotel);
    }

    @Override
    public List<ReviewResponse> getReviewBelongToHotel(String hotelId) {
        //Tim hotel tu hotelId
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new AppException(ErrorCode.HOTEL_NOT_FOUND));

        List<Review> reviews = reviewRepository.findByHotel_HotelId(hotelId);


        //N + 1
         List<ReviewResponse> reviewResponses = reviews.stream().map(review -> {

             //N + 1
             List<ReviewImageResponse> responses;

             List<ReviewImages> reviewImagesList = review.getReviewImagesList();
             responses = reviewImagesList.stream().map(reviewImages ->
                     ReviewImageResponse.builder()
                             .reviewImageUrl(reviewImages.getImageUrl())
                             .build()
                     ).toList();


             return ReviewResponse.builder()
                    .reviewId(review.getReviewId())
                    .reviewRating(review.getReviewRating())
                     .reviewPositiveComment(review.getReviewPositiveComment())
                     .reviewNegativeComment(review.getReviewNegativeComment())
                     .userName(review.getUser() != null ? review.getUser().getUserName() : null)
                     .createdAt(review.getCreatedAt())
                     .reviewImagesList(responses)
                    .build();

        }).toList();

         return reviewResponses;
    }
}
