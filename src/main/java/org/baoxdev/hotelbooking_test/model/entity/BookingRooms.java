package org.baoxdev.hotelbooking_test.model.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class BookingRooms {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String bookingRoomId;

    @Column(name = "price_per_night" , precision = 10 , scale = 2)
    BigDecimal pricePerNight;

    String bookingRoomStatus;

    @Column(name = "actual_check_in_time")
    LocalDateTime actualCheckInTime;

    @Column(name = "actual_check_out_time")
    LocalDateTime actualCheckOutTime;

    @CreatedDate
    @Column(name = "created_at")
    LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    LocalDateTime updatedAt;


    @ManyToOne(fetch = FetchType.LAZY)
    Booking booking;

    @ManyToOne(fetch = FetchType.LAZY)
    RoomType roomType;

    @ManyToOne(fetch = FetchType.LAZY)
    Room room;

}
