package org.baoxdev.hotelbooking_test.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.baoxdev.hotelbooking_test.model.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review , String> {
    List<Review> findByHotel_HotelId(String hotelHotelId);

   @Query("select avg(r.reviewRating) from  Review r where r.hotel.hotelId = :hotelId")
    Double getAverageRatingByHotelId(@Param("hotelId") String hotelId);

   @Query("select avg (r.reviewRating) from  Review r")
    Double getGloBalAverageRating();

    boolean existsByBooking_BookingId(String bookingBookingId);
}
