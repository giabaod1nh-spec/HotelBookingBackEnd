package org.baoxdev.hotelbooking_test.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.baoxdev.hotelbooking_test.dto.ApiResponse;
import org.baoxdev.hotelbooking_test.dto.request.ReviewRequest;
import org.baoxdev.hotelbooking_test.dto.response.ReviewResponse;
import org.baoxdev.hotelbooking_test.service.interfaces.IReviewService;
import org.hibernate.annotations.FetchProfile;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE , makeFinal = true)
public class ReviewController {
    IReviewService reviewService;

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<ReviewResponse> createReviewForUser(
            @RequestPart("review") ReviewRequest request,
            @RequestPart(value = "files", required = false) List<MultipartFile> files
    ) throws IOException {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        request.setUserId(userName);
        return ApiResponse.<ReviewResponse>builder()
                .code(1000)
                .message("Dang review user thanh cong")
                .result(reviewService.postReviewOfUser(request , files != null ? files : List.of()))
                .build();
    }

    @PostMapping(value = "/create-json", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<ReviewResponse> createReviewJson(@RequestBody ReviewRequest request) throws IOException {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        request.setUserId(userName);
        return ApiResponse.<ReviewResponse>builder()
                .code(1000)
                .message("Review submitted successfully")
                .result(reviewService.postReviewOfUser(request, List.of()))
                .build();
    }

    @GetMapping("/getAlls/{hotelId}")
    public ApiResponse<List<ReviewResponse>> getAllReviewFromHotel(@PathVariable String hotelId){
        return ApiResponse.<List<ReviewResponse>>builder()
                .code(1000)
                .message("Lay tat ca cac review thanh cong")
                .result(reviewService.getReviewBelongToHotel(hotelId))
                .build();
    }

    @GetMapping("/hotel/{hotelId}")
    public ApiResponse<List<ReviewResponse>> getReviewsByHotelId(@PathVariable String hotelId) {
        ReviewRequest request = new ReviewRequest();
        request.setHotelId(hotelId);
        return ApiResponse.<List<ReviewResponse>>builder()
                .code(1000)
                .message("Get reviews success")
                .result(reviewService.getReviewBelongToHotel(hotelId))
                .build();
    }

}
