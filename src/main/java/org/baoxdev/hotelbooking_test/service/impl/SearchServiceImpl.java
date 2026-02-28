package org.baoxdev.hotelbooking_test.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.baoxdev.hotelbooking_test.dto.request.HotelSearchPageRequest;
import org.baoxdev.hotelbooking_test.dto.response.BestPriceFromRoomTypeInHotelResponse;
import org.baoxdev.hotelbooking_test.dto.response.HotelImageResponse;
import org.baoxdev.hotelbooking_test.dto.response.HotelSearchResponse;
import org.baoxdev.hotelbooking_test.dto.response.PageResponse;
import org.baoxdev.hotelbooking_test.exception.AppException;
import org.baoxdev.hotelbooking_test.mapper.AmenityMapper;
import org.baoxdev.hotelbooking_test.model.entity.Hotel;
import org.baoxdev.hotelbooking_test.model.entity.HotelImages;
import org.baoxdev.hotelbooking_test.model.enums.ErrorCode;
import org.baoxdev.hotelbooking_test.model.enums.HotelStatus;
import org.baoxdev.hotelbooking_test.repository.HotelRepository;
import org.baoxdev.hotelbooking_test.repository.RoomAvailabilityRepository;
import org.baoxdev.hotelbooking_test.service.interfaces.IHotelImageService;
import org.baoxdev.hotelbooking_test.service.interfaces.IHotelService;
import org.baoxdev.hotelbooking_test.service.interfaces.IPriceService;
import org.baoxdev.hotelbooking_test.service.interfaces.ISearchService;
import org.baoxdev.hotelbooking_test.service.interfaces.projection.RoomTypePriceAgg;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE , makeFinal = true)
public class SearchServiceImpl implements ISearchService {
    HotelRepository hotelRepository;
    RoomAvailabilityRepository availabilityRepository;
    IPriceService priceService;
    AmenityMapper amenityMapper;
    IHotelImageService hotelImageService;
    //API search first like booking.com
    @Override
    public List<HotelSearchResponse> searchHotels(String city, LocalDate checkIn, LocalDate checkOut, int totalGuest, int totalRoom) {
       //Dau tien filter hotel by city

        List<Hotel> hotels = hotelRepository.findByHotelCityAndHotelStatus(city , HotelStatus.OPEN);

        List<HotelSearchResponse> responses = new ArrayList<>();

        //Tiep dep loop qua hotel va search tung hotel xem co thoa man demand user ko
        for(Hotel h : hotels){
            long expectedDays = ChronoUnit.DAYS.between(checkIn , checkOut);

            int guestPerRoom  = (int) (double) (totalGuest / totalRoom);

            List<String> availableRoomTypes = availabilityRepository
                    .findAvailableRoomTypeIdsForHotelAndUserQuantity(h.getHotelId() , checkIn , checkOut , totalRoom , expectedDays , guestPerRoom);

            //Reuse code in AvailabilityService de tinh totalPrice dua tren roomType
            if(availableRoomTypes != null) {
                //We need a search service to calculate best price for combination
                BestPriceFromRoomTypeInHotelResponse response = priceService.calculateBestPrice(h.getHotelId(), availableRoomTypes, checkIn, checkOut, totalRoom);


                //Add
                responses.add(HotelSearchResponse.builder()
                        .hotelId(h.getHotelId())
                        .hotelName(h.getHotelName())
                        .hotelCity(h.getHotelCity())
                        .starRating(h.getStarRating())
                        .hotelStatus(HotelStatus.OPEN)
                        .lowestPrice(response.getBestPrice())
                        .recommendRoomType(response.getRoomTypeName())
                        .bedSummary(response.getBedType())
                        .totalGuest(totalGuest)
                        .totalNight((int) expectedDays)
                        .totalReview(h.getTotalReviews())
                        .adjustedScore(h.getAdjustRating())
                        .primaryImageUrl(h.getHotelImages().getFirst().getHotelImageUrl())
                        .amenities(h.getAmenities().stream().map(amenity -> amenityMapper.convertResponseFromAmenity(amenity)).toList())
                        .hotelImages(hotelImageService.getHotelImages(h.getHotelId()))
                        .hotelDesc(h.getHotelDescription())
                        //.roomTypeSearchItems()
                        //.reviewCount()
                        .build()
                );
            }
        }

        return responses;
    }

    @Override
    public HotelSearchResponse getHotelDetail(String hotelId) {
        Hotel h = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new AppException(ErrorCode.HOTEL_NOT_FOUND));

        String primaryUrl = (h.getHotelImages() != null && !h.getHotelImages().isEmpty())
                ? h.getHotelImages().getFirst().getHotelImageUrl()
                : null;

        return HotelSearchResponse.builder()
                .hotelId(h.getHotelId())
                .hotelName(h.getHotelName())
                .hotelCity(h.getHotelCity())
                .hotelCountry(h.getHotelCountry())
                .hotelAddress(h.getHotelAddress())
                .starRating(h.getStarRating())
                .hotelStatus(h.getHotelStatus())
                .primaryImageUrl(primaryUrl)
                .amenities(h.getAmenities() != null
                        ? h.getAmenities().stream()
                                .map(amenity -> amenityMapper.convertResponseFromAmenity(amenity))
                                .toList()
                        : List.of())
                .hotelImages(hotelImageService.getHotelImages(h.getHotelId()))
                .hotelDesc(h.getHotelDescription() != null ? h.getHotelDescription() : "")
                .totalReview(h.getTotalReviews() != null ? h.getTotalReviews() : 0)
                .adjustedScore(h.getAdjustRating() != null ? h.getAdjustRating() : 0.0)
                .build();
    }

    @Override
    public PageResponse<HotelSearchResponse> searchHotelsV2(HotelSearchPageRequest req) {
        int page = req.getPage() == null ? 0 : req.getPage();
        int size = req.getSize() == null ? 10 : req.getSize();
        long expectedDays = ChronoUnit.DAYS.between(req.getCheckIn(), req.getCheckOut());
        int guestPerRoom = (int) Math.ceil((double) req.getTotalGuest() / req.getTotalRoom());

        Pageable pageable = PageRequest.of(page, size, buildSort(req));

        // Step A: DB returns only paged hotel IDs
        Page<String> hotelIdPage = hotelRepository.findSearchHotelIds(
                req.getCity(),
                HotelStatus.OPEN,
                req.getCheckIn(),
                req.getCheckOut(),
                req.getTotalRoom(),
                guestPerRoom,
                expectedDays,
                pageable
        );

        if (hotelIdPage.isEmpty()) {
            return PageResponse.<HotelSearchResponse>builder()
                    .content(List.of())
                    .page(page)
                    .size(size)
                    .totalElements(0)
                    .totalPages(0)
                    .hasNext(false)
                    .build();
        }

        List<String> hotelIds = hotelIdPage.getContent();

        // Step B1: batch load hotel details
        List<Hotel> hotels = hotelRepository.findDetailsByHotelIds(hotelIds);
        Map<String, Hotel> hotelMap = hotels.stream()
                .collect(Collectors.toMap(Hotel::getHotelId, Function.identity()));

        // Step B2: batch load room-type total prices
        List<RoomTypePriceAgg> priceRows = availabilityRepository.findRoomTypeTotalPrices(
                hotelIds,
                req.getCheckIn(),
                req.getCheckOut(),
                req.getTotalRoom(),
                expectedDays
        );

        // pick cheapest roomType per hotel
        Map<String, RoomTypePriceAgg> cheapestByHotel = new HashMap<>();
        for (RoomTypePriceAgg row : priceRows) {
            RoomTypePriceAgg cur = cheapestByHotel.get(row.getHotelId());
            if (cur == null || row.getTotalPrice().compareTo(cur.getTotalPrice()) < 0) {
                cheapestByHotel.put(row.getHotelId(), row);
            }
        }

        // keep original DB page order
        List<HotelSearchResponse> content = new ArrayList<>();
        for (String hotelId : hotelIds) {
            Hotel h = hotelMap.get(hotelId);
            if (h == null) continue;

            RoomTypePriceAgg best = cheapestByHotel.get(hotelId);

            String primaryImage = null;
            if (h.getHotelImages() != null && !h.getHotelImages().isEmpty()) {
                primaryImage = h.getHotelImages().stream()
                        .sorted(Comparator.comparing(HotelImages::getIsPrimary).reversed())
                        .map(HotelImages::getHotelImageUrl)
                        .findFirst()
                        .orElse(null);
            }

            List<HotelImageResponse> imageResponses = (h.getHotelImages() == null) ? List.of() :
                    h.getHotelImages().stream()
                            .map(img -> HotelImageResponse.builder()
                                    .hotelImageId(img.getHotelImageId())
                                    .hotelImageUrl(img.getHotelImageUrl())
                                    .isPrimary(img.getIsPrimary())
                                    .build())
                            .toList();

            content.add(HotelSearchResponse.builder()
                    .hotelId(h.getHotelId())
                    .hotelName(h.getHotelName())
                    .hotelCity(h.getHotelCity())
                    .hotelCountry(h.getHotelCountry())
                    .hotelAddress(h.getHotelAddress())
                    .starRating(h.getStarRating())
                    .hotelStatus(h.getHotelStatus())
                    .lowestPrice(best != null ? best.getTotalPrice() : null)
                    .recommendRoomType(best != null ? best.getRoomTypeName() : null)
                    .bedSummary(best != null ? best.getBedSummary() : null)
                    .totalGuest(req.getTotalGuest())
                    .totalNight((int) expectedDays)
                    .totalReview(h.getTotalReviews() != null ? h.getTotalReviews() : 0)
                    .adjustedScore(h.getAdjustRating() != null ? h.getAdjustRating() : 0.0)
                    .primaryImageUrl(primaryImage)
                    .amenities(h.getAmenities() == null ? List.of() :
                            h.getAmenities().stream().map(amenityMapper::convertResponseFromAmenity).toList())
                    .hotelImages(imageResponses)
                    .hotelDesc(h.getHotelDescription())
                    .build());
        }

        return PageResponse.<HotelSearchResponse>builder()
                .content(content)
                .page(hotelIdPage.getNumber())
                .size(hotelIdPage.getSize())
                .totalElements(hotelIdPage.getTotalElements())
                .totalPages(hotelIdPage.getTotalPages())
                .hasNext(hotelIdPage.hasNext())
                .build();
    }

    private void validate(HotelSearchPageRequest req) {
        if (req.getCity() == null || req.getCity().isBlank()) {
            throw new AppException(ErrorCode.HOTEL_NOT_FOUND);
        }
        if (req.getCheckIn() == null || req.getCheckOut() == null || !req.getCheckOut().isAfter(req.getCheckIn())) {
            throw new AppException(ErrorCode.BOOKING_NOT_FOUND); // create dedicated INVALID_DATE error code if possible
        }
        if (req.getTotalGuest() <= 0 || req.getTotalRoom() <= 0) {
            throw new AppException(ErrorCode.BOOKING_NOT_FOUND); // create dedicated INVALID_SEARCH_PARAM
        }
    }

    private Sort buildSort(HotelSearchPageRequest req) {
        String sortBy = req.getSortBy() == null ? "createdAt" : req.getSortBy();
        Sort.Direction dir = "asc".equalsIgnoreCase(req.getDirection()) ? Sort.Direction.ASC : Sort.Direction.DESC;

        // DB-native sort fields in Hotel table
        return switch (sortBy) {
            case "starRating" -> Sort.by(dir, "starRating");
            case "score" -> Sort.by(dir, "adjustRating");
            default -> Sort.by(dir, "createdAt");
        };
    }
}
