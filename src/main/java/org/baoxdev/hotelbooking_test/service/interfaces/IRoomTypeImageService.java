package org.baoxdev.hotelbooking_test.service.interfaces;

import org.baoxdev.hotelbooking_test.dto.response.RoomTypeImageResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IRoomTypeImageService {
   List<RoomTypeImageResponse> uploadRoomImages(String roomTypeId ,List<MultipartFile> files) throws IOException;
   List<RoomTypeImageResponse> getRoomTypeImages(String roomTypeId);

   void deleteRoomTypeImage(String roomTypeImageId);

   void setPrimary(String roomTypeId , String roomTypeImageId);
}
