package org.baoxdev.hotelbooking_test.service.impl;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.baoxdev.hotelbooking_test.dto.request.RoomTypeRequest;
import org.baoxdev.hotelbooking_test.dto.request.RoomTypeUpdateRequest;
import org.baoxdev.hotelbooking_test.dto.response.RoomTypeDetailResponse;
import org.baoxdev.hotelbooking_test.dto.response.RoomTypeResponse;
import org.baoxdev.hotelbooking_test.exception.AppException;
import org.baoxdev.hotelbooking_test.mapper.RoomTypeMapper;
import org.baoxdev.hotelbooking_test.model.entity.Hotel;
import org.baoxdev.hotelbooking_test.model.entity.RoomAvailability;
import org.baoxdev.hotelbooking_test.model.entity.RoomType;
import org.baoxdev.hotelbooking_test.model.enums.ErrorCode;
import org.baoxdev.hotelbooking_test.model.enums.RoomTypeStatus;
import org.baoxdev.hotelbooking_test.repository.HotelRepository;
import org.baoxdev.hotelbooking_test.repository.RoomAvailabilityRepository;
import org.baoxdev.hotelbooking_test.repository.RoomTypeRepository;
import org.baoxdev.hotelbooking_test.service.interfaces.IAvailabilityService;
import org.baoxdev.hotelbooking_test.service.interfaces.IRoomTypeService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE , makeFinal = true)
@RequiredArgsConstructor
public class RoomTypeServiceImpl implements IRoomTypeService {
    RoomTypeMapper roomTypeMapper;
    RoomTypeRepository roomTypeRepository;
    HotelRepository hotelRepository;
    RoomAvailabilityRepository roomAvailabilityRepository;
    IAvailabilityService availabilityService;

    @Transactional
    @Override
    public RoomTypeResponse createRoomType(String hotelId , RoomTypeRequest request) {
        RoomType roomType = roomTypeMapper.convertRoomTypeFromRequest(request);

        Hotel hotel = hotelRepository.findById(hotelId).orElseThrow(() ->
                new AppException(ErrorCode.HOTEL_NOT_FOUND));
        roomType.setHotel(hotel);


        //Generate availability to next 90 days
        generateAvailabilityIfMissing(roomType , LocalDate.now() , LocalDate.now().plusDays(90));

        return roomTypeMapper.convertResponseFromRoomType(roomTypeRepository.save(roomType));
    }

    @Override
    public RoomTypeResponse getERoomTypeById(String roomTypeId) {
        RoomType roomType = roomTypeRepository.findById(roomTypeId).orElseThrow(() ->
                new AppException(ErrorCode.ROOM_TYPE_NOT_FOUND));


        return roomTypeMapper.convertResponseFromRoomType(roomType);
    }

    @Transactional
    @Override
    public RoomTypeResponse updateRoomType(String roomTypeId, RoomTypeUpdateRequest request) {
        RoomType roomType = roomTypeRepository.findById(roomTypeId).orElseThrow(() ->
                new AppException(ErrorCode.ROOM_TYPE_NOT_FOUND));

        roomType.setRoomTypeName(request.getRoomTypeName());
        roomType.setRoomTypeDesc(request.getRoomTypeDesc());
        roomType.setBasePrice(request.getBasePrice());
        roomType.setMaxOccupy(request.getMaxOccupy());
        roomType.setTotalRooms(request.getTotalRooms());

        roomTypeRepository.save(roomType);

        return roomTypeMapper.convertResponseFromRoomType(roomType);
    }


    @Override
    public List<RoomTypeResponse> getRoomTypeByHotelId(String hotelId) {
        List<RoomType> roomTypes = roomTypeRepository.findByHotel_HotelId(hotelId);

        return roomTypes.stream().map(roomType ->  roomTypeMapper.convertResponseFromRoomType(roomType)).toList();
    }

        @Override
        public List<RoomTypeDetailResponse> getRoomTypesWithAvailability(String hotelId, LocalDate checkIn, LocalDate checkOut, int guests) {
            List<RoomType> roomTypes = roomTypeRepository.findByHotel_HotelId(hotelId);
            List<RoomTypeDetailResponse> result = new ArrayList<>();
            LocalDate endExclusive = checkOut.minusDays(1);

        for (RoomType rt : roomTypes) {
            if (rt.getRoomTypeStatus() != null && rt.getRoomTypeStatus() != RoomTypeStatus.ACTIVE) continue;

            List<RoomAvailability> availabilities = roomAvailabilityRepository
                    .findByRoomType_RoomTypeIdAndDateBetween(rt.getRoomTypeId(), checkIn, endExclusive);

            long expectedDays = ChronoUnit.DAYS.between(checkIn, checkOut);
            int availableCount = 0;
            BigDecimal totalPriceForStay = BigDecimal.ZERO;

            if (availabilities.size() >= expectedDays) {
                availableCount = availabilities.stream()
                        .mapToInt(RoomAvailability::getAvailableCount)
                        .min()
                        .orElse(0);
                try {
                    totalPriceForStay = availabilityService.calculateTotalPrice(rt.getRoomTypeId(), checkIn, checkOut, 1);
                } catch (Exception ignored) {
                }
            }
            if (totalPriceForStay.compareTo(BigDecimal.ZERO) == 0 && rt.getBasePrice() != null) {
                totalPriceForStay = rt.getBasePrice().multiply(BigDecimal.valueOf(expectedDays));
            }

            result.add(RoomTypeDetailResponse.builder()
                    .roomTypeId(rt.getRoomTypeId())
                    .roomTypeName(rt.getRoomTypeName())
                    .roomTypeDesc(rt.getRoomTypeDesc())
                    .bedSummary(rt.getBedSummary())
                    .basePrice(rt.getBasePrice())
                    .maxOccupy(rt.getMaxOccupy())
                    .totalRooms(rt.getTotalRooms())
                    .availableCount(availableCount)
                    .totalPriceForStay(totalPriceForStay)
                    .build());
        }
        return result;
    }

    @Override
    public void deleteRoomType(String roomTypeId) {
        RoomType roomType = roomTypeRepository.findById(roomTypeId).orElseThrow(() ->
                new AppException(ErrorCode.ROOM_TYPE_NOT_FOUND));

        roomType.setRoomTypeStatus(RoomTypeStatus.INACTIVE);
        roomTypeRepository.save(roomType);
    }

    private void generateAvailabilityIfMissing(RoomType roomType , LocalDate start , LocalDate end){
        List<RoomAvailability>  roomAvailabilities = new ArrayList<>();

        for(LocalDate d = start ; d.isBefore(end) ; d = d.plusDays(1)){
            boolean exists = roomAvailabilityRepository.existsByRoomType_RoomTypeIdAndDate(roomType.getRoomTypeId()
            , d);

            if (exists) continue;

            roomAvailabilities.add(RoomAvailability.builder()
                            .roomType(roomType)
                            .date(d)
                            .availableCount(roomType.getTotalRooms())
                            .price(roomType.getBasePrice())
                            .version(0)
                    .build());
        }

        if(!roomAvailabilities.isEmpty()){
            roomAvailabilityRepository.saveAll(roomAvailabilities);
        }
    }

}
