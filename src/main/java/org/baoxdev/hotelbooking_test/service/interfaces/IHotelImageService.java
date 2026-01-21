package org.baoxdev.hotelbooking_test.service.interfaces;

import org.baoxdev.hotelbooking_test.dto.response.HotelImageResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IHotelImageService {
    List<HotelImageResponse> uploadHotelImages(String hotelId , List<MultipartFile> files) throws IOException;
    List<HotelImageResponse> getHotelImages(String hotelId);
    void deleteHotelImage(String imageId);
    void setPrimaryImage(String hotelId , String imageId);
}
