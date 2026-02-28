package org.baoxdev.hotelbooking_test.configuration;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.baoxdev.hotelbooking_test.model.entity.Booking;
import org.baoxdev.hotelbooking_test.model.entity.BookingRooms;
import org.baoxdev.hotelbooking_test.model.enums.BookingStatus;
import org.baoxdev.hotelbooking_test.repository.BookingRepository;
import org.baoxdev.hotelbooking_test.repository.BookingRoomRepository;
import org.baoxdev.hotelbooking_test.service.interfaces.IAvailabilityService;
import org.jspecify.annotations.Nullable;
import org.redisson.api.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

import static io.lettuce.core.pubsub.PubSubOutput.Type.message;

@Component
@RequiredArgsConstructor
@Slf4j(topic = "BOOKING_EXPIRATION")
@FieldDefaults(level = AccessLevel.PRIVATE , makeFinal = true)
public class BookingExpirationListener implements MessageListener {
    BookingRepository bookingRepository;
    BookingRoomRepository bookingRoomRepository;
    IAvailabilityService availabilityService;

    static String PREFIX = "booking:pending:";


    @Override
    @Transactional
    public void onMessage(org.springframework.data.redis.connection.Message message, byte @Nullable [] pattern) {
        String expiredKey = new String(message.getBody());

        // Only process booking keys
        if (!expiredKey.startsWith(PREFIX)) return;

        String bookingId = expiredKey.replace(PREFIX, "");
        log.info("Booking expired: {}", bookingId);

        try {
            Booking booking = bookingRepository.findById(bookingId).orElse(null);

            if (booking == null) return;

            // Only cancel if still PENDING (user might have paid just in time)
            if (booking.getBookingStatus() != BookingStatus.PENDING) {
                log.info("Booking {} already processed (status={}), skipping",
                        bookingId, booking.getBookingStatus());
                return;
            }

            // Release availability
            List<BookingRooms> bookingRooms = bookingRoomRepository
                    .findByBookingIdWithRoomType(bookingId);

            for (BookingRooms br : bookingRooms) {
                availabilityService.release(
                        br.getRoomType().getRoomTypeId(),
                        booking.getCheckInDate(),
                        booking.getCheckOutDate(),
                        br.getQuantity()
                );
            }

            // Cancel booking
            booking.setBookingStatus(BookingStatus.CANCELLED);
            bookingRepository.save(booking);

            log.info("Auto-cancelled expired booking: {}", booking.getBookingCode());

        } catch (Exception e) {
            log.error("Failed to cancel expired booking {}: {}",
                    bookingId, e.getMessage(), e);
        }
    }
}
