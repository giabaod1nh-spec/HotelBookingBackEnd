package org.baoxdev.hotelbooking_test.repository;

import org.baoxdev.hotelbooking_test.model.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking , String> {
    List<Booking> findByUser_UserIdAndCreatedAtOrderByCreatedAtDesc(String userUserId, Instant createdAt);

    List<Booking> findByUser_UserIdOrderByCreatedAtDesc(String userUserId);

    Optional<Booking> findByBookingCode(String bookingCode);
}
