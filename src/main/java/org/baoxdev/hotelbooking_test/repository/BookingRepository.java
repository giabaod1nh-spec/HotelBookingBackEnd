package org.baoxdev.hotelbooking_test.repository;

import org.baoxdev.hotelbooking_test.model.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking , String> {
    List<Booking> findByUser_UserIdAndCreatedAtOrderByCreatedAtDesc(String userUserId, Instant createdAt);

    List<Booking> findByUser_UserIdOrderByCreatedAtDesc(String userUserId);
}
