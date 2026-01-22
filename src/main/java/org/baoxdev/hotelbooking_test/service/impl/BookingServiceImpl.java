package org.baoxdev.hotelbooking_test.service.impl;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.baoxdev.hotelbooking_test.dto.request.BookingRequest;
import org.baoxdev.hotelbooking_test.dto.response.BookingResponse;
import org.baoxdev.hotelbooking_test.exception.AppException;
import org.baoxdev.hotelbooking_test.mapper.BookingMapper;
import org.baoxdev.hotelbooking_test.model.entity.Booking;
import org.baoxdev.hotelbooking_test.model.entity.Hotel;
import org.baoxdev.hotelbooking_test.model.entity.RoomType;
import org.baoxdev.hotelbooking_test.model.entity.User;
import org.baoxdev.hotelbooking_test.model.enums.BookingStatus;
import org.baoxdev.hotelbooking_test.model.enums.ErrorCode;
import org.baoxdev.hotelbooking_test.repository.BookingRepository;
import org.baoxdev.hotelbooking_test.repository.HotelRepository;
import org.baoxdev.hotelbooking_test.repository.RoomTypeRepository;
import org.baoxdev.hotelbooking_test.repository.UserRepository;
import org.baoxdev.hotelbooking_test.service.interfaces.IAvailabilityService;
import org.baoxdev.hotelbooking_test.service.interfaces.IBookingService;
import org.springframework.stereotype.Service;

import java.awt.print.Book;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE , makeFinal = true)
public class BookingServiceImpl implements IBookingService {
    BookingRepository bookingRepository;
    HotelRepository hotelRepository;
    RoomTypeRepository roomTypeRepository;
    IAvailabilityService availabilityService;
    UserRepository userRepository;
    BookingMapper bookingMapper;

    @Transactional
    @Override
    public BookingResponse createBooking(String hotelId, String roomTypeId, BookingRequest request, String currentUserName ) {
        Hotel hotel = hotelRepository.findById(hotelId).orElseThrow(() -> new AppException(ErrorCode.HOTEL_NOT_FOUND));

        RoomType roomType = roomTypeRepository.findById(roomTypeId).orElseThrow(() -> new AppException(ErrorCode.ROOM_TYPE_NOT_FOUND));

        //Check xem roomType co cung voi hotel ko
        if(!roomType.getHotel().getHotelId().equals(hotel.getHotelId())){
            throw new AppException(ErrorCode.ROOM_TYPE_NOT_FOUND);
        }

       //Check xem co hope le ko
        if (!availabilityService.checkAvailable(roomTypeId, request.getCheckInDate(), request.getCheckOutDate(), request.getQuantity())) {
            throw new AppException(ErrorCode.ROOM_AVAILABLE_NOT_ENOUGH);
        }

        //Reserve (dat phong o availability)
        availabilityService.reserve(roomTypeId , request.getCheckOutDate() , request.getCheckInDate() , request.getQuantity());

        //Tinh tong tien sau khi booking
        BigDecimal totalPrice = availabilityService.calculateTotalPrice(roomTypeId , request.getCheckInDate() , request.getCheckOutDate() , request.getQuantity());

        //Dung entity booking
        Booking booking = Booking.builder()
                .bookingCode(generateCode())
                .hotel(hotel)
                //.bookingRooms()
                .checkInDate(Date.valueOf(request.getCheckInDate()))
                .checkOutDate(Date.valueOf(request.getCheckOutDate()))
                .totalPrice(totalPrice)
                .bookingStatus(BookingStatus.PENDING)
                .numGuest(request.getNumGuests())
                .guestName(request.getGuestName())
                .guestEmail(request.getGuestEmail())
                .guestPhone(request.getGuestPhone())
                .quantity(request.getQuantity())
                .specialRequest(request.getSpecialRequest())
                .build();

        if (currentUserName != null && !currentUserName.isEmpty()){
            User user = userRepository.findUserByUserName(currentUserName).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
            booking.setUser(user);
        }
        bookingRepository.save(booking);

        return BookingResponse.builder()
                .bookingId(booking.getBookingId())
                .bookingCode(booking.getBookingCode())
                .status(booking.getBookingStatus())
                 .checkInDate(booking.getCheckInDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                .checkOutDate(booking.getCheckOutDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                .quantity(request.getQuantity())
                .totalPrice(totalPrice)
                .build();
    }

    @Override
    public BookingResponse getById(String bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(()
                -> new AppException(ErrorCode.BOOKING_NOT_FOUND));

        return bookingMapper.convertResponseFromBooking(booking);
    }

    @Override
    public List<BookingResponse> listMyBookings(String currentUserName) {
        User user = userRepository.findUserByUserName(currentUserName).orElseThrow(()
                -> new AppException(ErrorCode.USER_NOT_FOUND));

        List<Booking> bookings = bookingRepository.findByUser_UserIdOrderByCreatedAtDesc(user.getUserId());

        return bookings.stream().map(booking ->
                bookingMapper.convertResponseFromBooking(booking)).toList();
    }

    @Override
    public void cancel(String bookingId, String currentUserName) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(()
                -> new AppException(ErrorCode.BOOKING_NOT_FOUND));

        // 2) Idempotent: already cancelled -> do nothing
        if (booking.getBookingStatus() == BookingStatus.CANCELLED) {
            return;
        }

        // 3) Cannot cancel after check-in (or after check-out)
        if (booking.getBookingStatus() == BookingStatus.CHECK_IN
                || booking.getBookingStatus() == BookingStatus.CHECK_OUT) {
            throw new AppException(ErrorCode.BOOKING_CANNOT_CANCEL);
        }

        // 4) Need roomType and quantity to release inventory
        if (booking.getRoomType() == null || booking.getQuantity() == null) {
            throw new AppException(ErrorCode.BOOKING_CANNOT_CANCEL);
        }
        //Check xem co dung User nay dang booking ko
        User currentUser = userRepository.findUserByUserName(currentUserName)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        if (!booking.getUser().getUserId().equals(currentUser.getUserId())) {
            throw new AppException(ErrorCode.BOOKING_ACCESS_DENIED);
        }
        //Cancel phong , tra lai quantity
        availabilityService.release(booking.getRoomType().getRoomTypeId() ,
                booking.getCheckInDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                booking.getCheckOutDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                booking.getQuantity()
                );

        booking.setBookingStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);
    }

    private String generateCode(){
        return "BK" + UUID.randomUUID().toString().substring(0 , 8).toUpperCase();
    }

}
