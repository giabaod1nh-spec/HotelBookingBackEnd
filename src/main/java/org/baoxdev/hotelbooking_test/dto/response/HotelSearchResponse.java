package org.baoxdev.hotelbooking_test.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.baoxdev.hotelbooking_test.dto.request.RoomTypeSearchItem;
import org.baoxdev.hotelbooking_test.model.enums.HotelStatus;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class HotelSearchResponse {
    String hotelId ;
    String hotelName;
    String hotelCity;
    String hotelCountry;
    Integer starRating;
    HotelStatus hotelStatus;
    BigDecimal lowestPrice;
    String primaryImageUrl;
    //Ten roomType FamilyRoomWithTerrace , bedSummary la 2 double beds
    String recommendRoomType;
    String bedSummary;
    //So luong khach va so dem : 3 nights , 4 adults
    int totalGuest;
    int totalNight;
    //So luong review va adjustedReview
    int totalReview;
    double adjustedScore;
    List<AmenityResponse> amenities;
    //Anh khach san khi an vao khach san do
    List<HotelImageResponse> hotelImages;
    String hotelDesc;
    String hotelAddress;

    List<RoomTypeSearchItem> roomTypeSearchItems; //room types with availability and prices
    Integer reviewCount;
}
