package org.baoxdev.hotelbooking_test.service.impl;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.baoxdev.hotelbooking_test.dto.request.BookingRequest;
import org.baoxdev.hotelbooking_test.dto.request.BookingRoomItemRequest;
import org.baoxdev.hotelbooking_test.dto.request.CheckInRequest;
import org.baoxdev.hotelbooking_test.dto.request.RoomAssignmentRequest;
import org.baoxdev.hotelbooking_test.dto.response.BookingResponse;
import org.baoxdev.hotelbooking_test.dto.response.BookingRoomItemResponse;
import org.baoxdev.hotelbooking_test.exception.AppException;
import org.baoxdev.hotelbooking_test.mapper.BookingMapper;
import org.baoxdev.hotelbooking_test.model.entity.*;
import org.baoxdev.hotelbooking_test.model.enums.BookingStatus;
import org.baoxdev.hotelbooking_test.model.enums.ErrorCode;
import org.baoxdev.hotelbooking_test.model.enums.RoomStatus;
import org.baoxdev.hotelbooking_test.model.enums.BookingStatus;
import org.baoxdev.hotelbooking_test.repository.*;
import org.baoxdev.hotelbooking_test.service.interfaces.IAvailabilityService;
import org.baoxdev.hotelbooking_test.service.interfaces.IBookingService;
import org.springframework.stereotype.Service;

import java.awt.print.Book;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE , makeFinal = true)
@Slf4j(topic = "BOOKING_SERVICE")
public class BookingServiceImpl implements IBookingService {
    BookingRepository bookingRepository;
    HotelRepository hotelRepository;
    RoomTypeRepository roomTypeRepository;
    RoomRepository roomRepository;
    IAvailabilityService availabilityService;
    UserRepository userRepository;
    BookingMapper bookingMapper;
    BookingRoomRepository bookingRoomRepository;
    BookingExpirationService bookingExpirationService;
    ReviewRepository reviewRepository;

    @Transactional
    @Override
    public BookingResponse createBooking(String hotelId, BookingRequest request, String currentUserName ) {

        Hotel hotel = hotelRepository.findById(hotelId).orElseThrow(() -> new AppException(ErrorCode.HOTEL_NOT_FOUND));

        if(request.getRooms() == null || request.getRooms().isEmpty()){
            throw new AppException(ErrorCode.ROOM_TYPE_NOT_FOUND);
        }

        //Check xem roomType co cung voi hotel ko
        //if(!roomType.getHotel().getHotelId().equals(hotel.getHotelId())){
            //throw new AppException(ErrorCode.ROOM_TYPE_NOT_FOUND);
        //}

       //Check xem co hope le ko
        //if (!availabilityService.checkAvailable(roomTypeId, request.getCheckInDate(), request.getCheckOutDate(), request.getQuantity())) {
            //throw new AppException(ErrorCode.ROOM_AVAILABLE_NOT_ENOUGH);
        //}

        //Reserve (dat phong o availability)
        //availabilityService.reserve(roomTypeId , request.getCheckOutDate() , request.getCheckInDate() , request.getQuantity());

        //Tinh tong tien sau khi booking
        //BigDecimal totalPrice = availabilityService.calculateTotalPrice(roomTypeId , request.getCheckInDate() , request.getCheckOutDate() , request.getQuantity());

        //Dung entity booking
        Booking booking = Booking.builder()
                .bookingCode(generateCode())
                .hotel(hotel)
                //.bookingRooms()
                .checkInDate(request.getCheckInDate())
                .checkOutDate(request.getCheckOutDate())
                .bookingStatus(BookingStatus.PENDING)
                .numGuest(request.getNumGuests())
                .guestName(request.getGuestName())
                .guestEmail(request.getGuestEmail())
                .guestPhone(request.getGuestPhone())
                .totalPrice(BigDecimal.ZERO)
                .specialRequest(request.getSpecialRequest())
                .build();

        if (currentUserName != null && !currentUserName.isEmpty()){
            User user = userRepository.findUserByUserName(currentUserName).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
            booking.setUser(user);
        }
        bookingRepository.save(booking);

        List<BookingRooms> bookingRoomsList = new ArrayList<>();
        BigDecimal totalPrice = BigDecimal.ZERO;

        for(BookingRoomItemRequest roomItem : request.getRooms()){
            //Validate roomType
            RoomType roomType = roomTypeRepository.findById(roomItem.getRoomTypeId())
                    .orElseThrow(() -> new AppException(ErrorCode.ROOM_TYPE_NOT_FOUND));

            //Validate roomType co thuoc hotel ko
            if(!roomType.getHotel().getHotelId().equals(hotelId)){
                throw new AppException(ErrorCode.ROOM_TYPE_NOT_FOUND);
            }

            //Check availbility xem tung loai roomType co du phong dat ko
            if(!availabilityService
                    .checkAvailable(roomItem.getRoomTypeId()
                            , request.getCheckInDate()
                            , request.getCheckOutDate()
                            , roomItem.getQuantity())){
                log.info("Loi o phan check Avail trong create");
                throw new AppException(ErrorCode.ROOM_AVAILABLE_NOT_ENOUGH);
            }

            //Calculate price subtotal   -> pricePerNight * quantity * nights
            BigDecimal subTotal = availabilityService
                    .calculateTotalPrice(roomItem.getRoomTypeId(),
                            request.getCheckInDate(),
                            request.getCheckOutDate(),
                            roomItem.getQuantity());

            totalPrice = totalPrice.add(subTotal);

            log.info(totalPrice + "tong tien ko loi");
            //Reserve (Đặt chỗ cho từng yêu cầu roomType
            availabilityService.reserve(roomItem.getRoomTypeId(),
                    request.getCheckInDate() ,
                    request.getCheckOutDate(),
                    roomItem.getQuantity());

            //Create bookingRoom entity
            BookingRooms bookingRooms = BookingRooms.builder()
                    .pricePerNight(roomType.getBasePrice())
                    .quantity(roomItem.getQuantity())
                    .booking(booking)
                    .roomType(roomType)
                    .build();

            bookingRoomsList.add(bookingRooms);
        }

        bookingRoomRepository.saveAll(bookingRoomsList);
        //set Total Price after calculate in booking
        booking.setTotalPrice(totalPrice);
        bookingRepository.save(booking);
        //Scheduled cancel booking after 15 min neu ko tra tien
        bookingExpirationService.scheduleExpiration(booking.getBookingId());

        long nights = ChronoUnit.DAYS.between(request.getCheckInDate() , request.getCheckOutDate());
        return bookingMapper.buildBookingResponse(booking , bookingRoomsList , nights);
     }

    @Override
    public BookingResponse getById(String bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(()
                -> new AppException(ErrorCode.BOOKING_NOT_FOUND));

        List<BookingRooms> bookingRooms = bookingRoomRepository.findByBookingIdWithRoomType(bookingId);

        long nights = ChronoUnit.DAYS.between(
                booking.getCheckInDate()
                , booking.getCheckOutDate());

        return bookingMapper.buildBookingResponse(booking, bookingRooms , nights);
    }

    @Override
    public BookingResponse getByBookingCode(String bookingCode) {
        Booking booking = bookingRepository.findByBookingCode(bookingCode)
                .orElseThrow(() -> new AppException(ErrorCode.BOOKING_NOT_FOUND));

        List<BookingRooms> bookingRooms = bookingRoomRepository.findByBookingIdWithRoomType(booking.getBookingId());
        long nights = ChronoUnit.DAYS.between(booking.getCheckInDate(), booking.getCheckOutDate());

        return bookingMapper.buildBookingResponse(booking, bookingRooms, nights);
    }

    @Override
    public List<BookingResponse> listMyBookings(String currentUserName) {
        User user = userRepository.findUserByUserName(currentUserName).orElseThrow(()
                -> new AppException(ErrorCode.USER_NOT_FOUND));

        List<Booking> bookings = bookingRepository.findByUser_UserIdOrderByCreatedAtDesc(user.getUserId());

        return bookings.stream().map(booking ->{
            List<BookingRooms> bookingRooms = bookingRoomRepository.findByBookingIdWithRoomType(booking.getBookingId());

            long nights = ChronoUnit.DAYS.between(
                    booking.getCheckInDate() ,
                    booking.getCheckOutDate());

            return bookingMapper.buildBookingResponse(booking , bookingRooms , nights);
        }).toList();
    }

    @Override
    public List<BookingResponse> getReviewableBookings(String currentUserName) {
        User user = userRepository.findUserByUserName(currentUserName)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        List<Booking> bookings = bookingRepository.findByUser_UserIdOrderByCreatedAtDesc(user.getUserId());

        return bookings.stream()
                .filter(b -> b.getBookingStatus() == BookingStatus.CHECK_OUT)
                .filter(b -> !reviewRepository.existsByBooking_BookingId(b.getBookingId()))
                .map(booking -> {
                    List<BookingRooms> bookingRooms = bookingRoomRepository.findByBookingIdWithRoomType(booking.getBookingId());
                    long nights = ChronoUnit.DAYS.between(booking.getCheckInDate(), booking.getCheckOutDate());
                    return bookingMapper.buildBookingResponse(booking, bookingRooms, nights);
                })
                .toList();
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
        //if (booking.getRoomType() == null || booking.getQuantity() == null) {
            //throw new AppException(ErrorCode.BOOKING_CANNOT_CANCEL);
       //}
        //Check xem co dung User nay dang booking ko
        User currentUser = userRepository.findUserByUserName(currentUserName)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        if (!booking.getUser().getUserId().equals(currentUser.getUserId())) {
            throw new AppException(ErrorCode.BOOKING_ACCESS_DENIED);
        }
        //Tu booking lay ra List<BookingRooms>
        List<BookingRooms> bookingRooms = bookingRoomRepository.findByBookingIdWithRoomType(bookingId);

        //Cancel phong cho moi loai roomType trong booking
        for(BookingRooms br : bookingRooms){
            availabilityService.release(br.getRoomType().getRoomTypeId() ,
                    booking.getCheckInDate(),
                    booking.getCheckOutDate(),
                    br.getQuantity()
                    );
        }
        //set status cancel
        booking.setBookingStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);

    }

    @Transactional
    @Override
    public void checkIn(String bookingId, CheckInRequest request) {

        // 1.Get Booking
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(()
                -> new AppException(ErrorCode.BOOKING_NOT_FOUND));

        //2. Validate booking status , if payment done , status CONFIRMED
        if(booking.getBookingStatus() != BookingStatus.CONFIRMED){
            throw new AppException(ErrorCode.BOOKING_NOT_CONFIRMED);
        }
        //3. Lay ra bookingRoom tu Booking
        List<BookingRooms> bookingRooms = bookingRoomRepository.findByBooking_BookingId(bookingId);

        //4 Assign Room da chon cho Booking Rooms
        for(RoomAssignmentRequest req : request.getRooms()){
            BookingRooms br = bookingRooms.stream()
                    .filter(bookingRoom ->bookingRoom.getBookingRoomId().equals(req.getBookingRoomId()))
                    .findFirst()
                    .orElseThrow(() ->new AppException(ErrorCode.BOOKING_ROOM_NOT_FOUND));

            Room room = roomRepository.findById(req.getRoomId())
                    .orElseThrow(() -> new AppException(ErrorCode.ROOM_NOT_FOUND));

            //Kiem tra roomType co match voi BookingRoom ko
            if(!room.getRoomType().getRoomTypeId().equals(br.getRoomType().getRoomTypeId())){
                throw  new AppException(ErrorCode.ROOM_TYPE_MISMATCHED);
            }

            //Kiem tra roomStatus co available ko
            if(room.getRoomStatus() != RoomStatus.AVAILABLE){
                throw  new AppException(ErrorCode.ROOM_NOT_AVAILABLE);
            }
            //Assign room vao BookingRoom
            br.setRoom(room);
            br.setActualCheckInTime(LocalDateTime.now());

            //Update room status
            room.setRoomStatus(RoomStatus.OCCUPIED);
            roomRepository.save(room);
        }

        //Update booking status
        booking.setBookingStatus(BookingStatus.CHECK_IN);
        bookingRepository.save(booking);
        //Luu lai toan bo bookingrooms sau khi gan room
        bookingRoomRepository.saveAll(bookingRooms);

        //Bad code N+ 1 QUERY
        //List<BookingRooms> brs = request.getRooms().stream().map(roomAssign ->{
                //Room room = roomRepository.findById(roomAssign.getRoomId()).orElseThrow(()
                    //-> new AppException(ErrorCode.ROOM_NOT_FOUND));

                //BookingRooms bookingRooms = bookingRoomRepository.findById(roomAssign.getBookingRoomId())
                        //.orElseThrow(() -> new AppException(ErrorCode.BOOKING_ROOM_NOT_FOUND));
                //Set room 101 belongs to booking Room br-101
                //bookingRooms.setRoom(room);
                //room.setRoomStatus(RoomStatus.OCCUPIED);
                //roomRepository.save(room);
                //In check-in proccess
                //bookingRooms.setActualCheckInTime(LocalDateTime.now());
                //booking.setBookingStatus(BookingStatus.CHECK_IN);
                //bookingRoomRepository.save(bookingRooms);

                //return bookingRooms;
                //}).toList();
    }

    @Override
    public void checkOut(String bookingId) {
        // 1.Get Booking
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(()
                -> new AppException(ErrorCode.BOOKING_NOT_FOUND));

        //2. Validate booking status , if payment done , status CONFIRMED
        if(booking.getBookingStatus() != BookingStatus.CHECK_IN){
            throw new AppException(ErrorCode.BOOKING_NOT_CHECK_IN);
        }

        //Lay ra BookingRoom tu Booking de thao tac
        List<BookingRooms> bkrooms = bookingRoomRepository.findByBooking_BookingId(bookingId);

        List<BookingRooms> bookingRooms = bkrooms.stream()
                .map(bkr -> {
                    bkr.setActualCheckOutTime(LocalDateTime.now());

                    Room room = roomRepository.findById(bkr.getRoom().getRoomId())
                            .orElseThrow(() -> new AppException(ErrorCode.ROOM_NOT_FOUND));
                    //Update room status sau khi khach check out
                    room.setRoomStatus(RoomStatus.AVAILABLE);
                    roomRepository.save(room);
                    return bookingRoomRepository.save(bkr);
                }).toList();


        //Update booking status sang check_out
        booking.setBookingStatus(BookingStatus.CHECK_OUT);
        bookingRepository.save(booking);
    }

    private String generateCode(){
        return "BK" + UUID.randomUUID().toString().substring(0 , 8).toUpperCase();
    }

}
