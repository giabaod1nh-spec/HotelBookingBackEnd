package org.baoxdev.hotelbooking_test.repository;

import org.baoxdev.hotelbooking_test.model.entity.Hotel;
import org.baoxdev.hotelbooking_test.model.enums.HotelStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface HotelRepository extends JpaRepository<Hotel , String >, JpaSpecificationExecutor<Hotel> {
    Page<Hotel> findAll(Pageable pageable) ;
    List<Hotel> findByHotelCityAndHotelStatus(String hotelCity, HotelStatus hotelStatus);

    @Query("""
    select h.hotelId
    from Hotel h
    where lower(h.hotelCity) = lower(:city)
    and h.hotelStatus = :status
    and exists (
    select 1
    from RoomType rt
    join rt.roomAvailabilities ra
    where rt.hotel.hotelId = h.hotelId
    and ra.date between :checkIn and :checkOut
    and ra.availableCount >= :quantity
    and rt.maxOccupy >= :guestPerRoom
    group by rt.roomTypeId
    having count (ra.date) >= :expectedDays
      )
""")
    Page<String> findSearchHotelIds(
            @Param("city") String city ,
            @Param("status") HotelStatus status ,
            @Param("checkIn") LocalDate checkIn ,
            @Param("checkOut") LocalDate checkOut,
            @Param("quantity") int quantity ,
            @Param("guestPerRoom") int guestPerRoom ,
            @Param("expectedDays") long expectedDays ,
            Pageable pageable
    );

    @EntityGraph(attributePaths = {"amenities" , "hotelImages"})
    @Query("select distinct h from Hotel h where h.hotelId in :hotelIds")
    List<Hotel> findDetailsByHotelIds(@Param("hotelIds") List<String> hotelIds);

}
