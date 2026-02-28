package org.baoxdev.hotelbooking_test.repository;

import org.baoxdev.hotelbooking_test.model.entity.BookingRooms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public interface BookingRoomRepository extends JpaRepository<BookingRooms , String> {

    @Query("select br from BookingRooms br " +
            "JOIN FETCH br.roomType " +
            "where br.booking.bookingId = :bookingId")
    List<BookingRooms> findByBookingIdWithRoomType(String bookingId);

    //List<BookingRooms> findByBooking_BookingId(String bookingBookingId);

    List<BookingRooms> findByBooking_BookingId(String bookingBookingId);

    List<BookingRooms> findByRoomType_RoomTypeId(String roomTypeRoomTypeId);

    List<BookingRooms> findByRoomType_RoomTypeIdAndCreatedAt(String roomTypeRoomTypeId, LocalDateTime createdAt);
}
