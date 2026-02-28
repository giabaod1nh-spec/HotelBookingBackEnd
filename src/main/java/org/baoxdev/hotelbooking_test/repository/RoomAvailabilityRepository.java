package org.baoxdev.hotelbooking_test.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.baoxdev.hotelbooking_test.model.entity.RoomAvailability;
import org.baoxdev.hotelbooking_test.service.interfaces.projection.RoomTypePriceAgg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Repository
public interface RoomAvailabilityRepository extends JpaRepository<RoomAvailability , String> {
    List<RoomAvailability> findByRoomType_RoomTypeIdAndDateBetween(String roomTypeRoomTypeId, LocalDate start, LocalDate end);

    boolean existsByRoomType_RoomTypeIdAndDate(String roomTypeRoomTypeId, LocalDate date);

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

    @Modifying(clearAutomatically = true)
    @Query("""
        update RoomAvailability ra
           set ra.availableCount = ra.availableCount + :qty,
               ra.version = ra.version + 1
         where ra.roomType.roomTypeId = :roomTypeId
           and ra.date = :date
    """)
    int releaseOneDay(String roomTypeId, Date date, int qty);


    @Query("""
    select sum(ra.price)
    from RoomAvailability ra
    where ra.roomType.roomTypeId = :roomTypeId
    and ra.date >= :checkIn 
    and ra.date < :checkOut
    and ra.availableCount >= :quantity
""")
    BigDecimal getTotalPriceForDateRange(
            @Param("roomTypeId") String roomTypeId ,
            @Param("checkIn") LocalDate checkIn ,
            @Param("checkOut") LocalDate checkOut ,
            @Param("quantity") int quantity
    );



    @Query("""
    select distinct ra.roomType.roomTypeId
    from RoomAvailability ra
    where ra.roomType.hotel.hotelId = :hotelId
    and ra.date >= :checkIn
    and ra.date < :checkOut
    and ra.availableCount >= :quantity
    and ra.roomType.maxOccupy >= :guestPerRoom
    group by ra.roomType.roomTypeId
    having count(ra.date) >= :expectedDays
""")
    List<String>findAvailableRoomTypeIdsForHotelAndUserQuantity(
            @Param("hotelId") String hotelId,
            @Param("checkIn") LocalDate checkIn ,
            @Param("checkOut") LocalDate checkOut ,
            @Param("quantity") int quantity ,
            @Param("expectedDays") long expectedDays,
            @Param("guestPerRoom") int guestPerRoom
    );

    @Query("""
        select ra.roomType.hotel.hotelId as hotelId,
               ra.roomType.roomTypeId as roomTypeId,
               ra.roomType.roomTypeName as roomTypeName,
               ra.roomType.bedSummary as bedSummary,
               sum(ra.price) as totalPrice
        from RoomAvailability ra
        where ra.roomType.hotel.hotelId in :hotelIds
          and ra.date >= :checkIn
          and ra.date < :checkOut
          and ra.availableCount >= :quantity
        group by ra.roomType.hotel.hotelId,
                 ra.roomType.roomTypeId,
                 ra.roomType.roomTypeName,
                 ra.roomType.bedSummary
        having count(distinct ra.date) = :expectedDays
        """)
    List<RoomTypePriceAgg> findRoomTypeTotalPrices(
            @org.springframework.data.repository.query.Param("hotelIds") List<String> hotelIds,
            @org.springframework.data.repository.query.Param("checkIn") LocalDate checkIn,
            @org.springframework.data.repository.query.Param("checkOut") LocalDate checkOut,
            @org.springframework.data.repository.query.Param("quantity") int quantity,
            @org.springframework.data.repository.query.Param("expectedDays") long expectedDays
    );
}
