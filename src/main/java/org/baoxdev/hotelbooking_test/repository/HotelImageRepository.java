package org.baoxdev.hotelbooking_test.repository;

import org.baoxdev.hotelbooking_test.model.entity.HotelImages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelImageRepository extends JpaRepository<HotelImages , String> {

    double findHotelImagesByHotel_HotelId(String hotelHotelId);

    List<HotelImages> findByHotel_HotelId(String hotelHotelId);
}
