package org.baoxdev.hotelbooking_test.repository;


import org.baoxdev.hotelbooking_test.model.entity.RoomType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomTypeRepository extends JpaRepository<RoomType , String> , JpaSpecificationExecutor<RoomType>{
    List<RoomType> findByHotel_HotelId(String hotelHotelId);
}
