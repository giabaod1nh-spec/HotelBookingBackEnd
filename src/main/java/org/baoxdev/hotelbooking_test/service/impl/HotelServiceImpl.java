package org.baoxdev.hotelbooking_test.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.baoxdev.hotelbooking_test.dto.request.HotelRequest;
import org.baoxdev.hotelbooking_test.dto.response.HotelResponse;
import org.baoxdev.hotelbooking_test.exception.AppException;
import org.baoxdev.hotelbooking_test.mapper.HotelMapper;
import org.baoxdev.hotelbooking_test.model.entity.Hotel;
import org.baoxdev.hotelbooking_test.model.enums.ErrorCode;
import org.baoxdev.hotelbooking_test.model.enums.HotelStatus;
import org.baoxdev.hotelbooking_test.repository.HotelRepository;
import org.baoxdev.hotelbooking_test.service.interfaces.IHotelService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE , makeFinal = true)
public class HotelServiceImpl implements IHotelService {
     HotelRepository hotelRepository;
     HotelMapper hotelMapper;
    @Override
    public HotelResponse createHotel(HotelRequest request){
        Hotel hotel = hotelMapper.convertHotelFromCreateRequest(request);

        return hotelMapper.convertResponseFromHotel(hotelRepository.save(hotel));
    }

    @Override
    public HotelResponse getHotelInfo(String hotelId) {
        Hotel hotel = hotelRepository.findById(hotelId).orElseThrow(() -> new AppException(ErrorCode.HOTEL_NOT_FOUND));
        return hotelMapper.convertResponseFromHotel(hotel);
    }

    @Override
    public HotelResponse updateHotelInfo(String hotelId , HotelRequest request) {
        Hotel hotel = hotelRepository.findById(hotelId).orElseThrow(() -> new AppException(ErrorCode.HOTEL_NOT_FOUND));

        hotel.setHotelAddress(request.getHotelAddress());
        hotel.setHotelCity(request.getHotelCity());
        hotel.setHotelCountry(request.getHotelCountry());
        hotel.setHotelDescription(request.getHotelDescription());
        hotel.setHotelEmail(request.getHotelEmail());
        hotel.setHotelName(request.getHotelName());
        hotel.setHotelPhone(request.getHotelPhone());

        return hotelMapper.convertResponseFromHotel(hotelRepository.save(hotel));
    }

    @Override
    public void deleteHotel(String hotelId) {
        Hotel hotel = hotelRepository.findById(hotelId).orElseThrow(() -> new AppException(ErrorCode.HOTEL_NOT_FOUND));
        hotel.setHotelStatus(HotelStatus.CLOSED);
    }

    @Override
    public List<HotelResponse> getAllHotelFromPagination(int page, int size, String sortBy, String direction, String city, String country, Integer starRating) {
        Sort sort = Sort.by(Sort.Direction.fromString(direction) , sortBy);
        Pageable pageable = PageRequest.of(page , size , sort);

        // Build specification for filtering (start with TRUE predicate)
        Specification<Hotel> spec = (root, query, cb) -> cb.conjunction();

        if (StringUtils.hasText(city)) {
            spec = spec.and((root, query, cb) -> 
                cb.equal(cb.lower(root.get("hotelCity")), city.toLowerCase()));
        }

        if (StringUtils.hasText(country)) {
            spec = spec.and((root, query, cb) -> 
                cb.equal(cb.lower(root.get("hotelCountry")), country.toLowerCase()));
        }

        if (starRating != null) {
            spec = spec.and((root, query, cb) -> 
                cb.equal(root.get("starRating"), starRating));
        }

        return hotelRepository.findAll(spec, pageable).stream()
                .map(hotel -> hotelMapper.convertResponseFromHotel(hotel))
                .toList();
    }



}
