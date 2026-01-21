package org.baoxdev.hotelbooking_test.repository;

import org.baoxdev.hotelbooking_test.dto.response.RoomTypeImageResponse;
import org.baoxdev.hotelbooking_test.model.entity.RoomTypeImages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomTypeImageRepository extends JpaRepository<RoomTypeImages , String> {
    List<RoomTypeImages> findByRoomType_RoomTypeId(String roomTypeRoomTypeId);
}
