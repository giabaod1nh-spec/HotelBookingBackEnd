package org.baoxdev.hotelbooking_test.repository;

import org.baoxdev.hotelbooking_test.model.entity.RoomAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Repository
public interface RoomAvailabilityRepository extends JpaRepository<RoomAvailability , String> {
    List<RoomAvailability> findByRoomType_RoomTypeIdAndDateBetween(String roomTypeRoomTypeId, Date end, Date start);

    boolean existsByRoomType_RoomTypeIdAndDate(String roomTypeRoomTypeId, Date date);

    @Modifying
    @Query("""
        update RoomAvailability ra
           set ra.availableCount = ra.availableCount - :qty,
               ra.version = ra.version + 1
         where ra.roomType.roomTypeId = :roomTypeId
           and ra.date = :date
           and ra.availableCount >= :qty
    """)
    int tryReserveOneDay(String roomTypeId, LocalDate date, int qty);

}
