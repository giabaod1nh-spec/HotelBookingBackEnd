package org.baoxdev.hotelbooking_test.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "BOOKING_EXPIRATION")
@FieldDefaults(level = AccessLevel.PRIVATE , makeFinal = true)
public class BookingExpirationService {
    StringRedisTemplate redisTemplate;
    static  String PREFIX = "booking:pending:";
    static  long PAYMENT_TIMEOUT_MINUTES = 15;

    // Gọi khi tạo booking → set key với TTL 15 phút
    public void scheduleExpiration(String bookingId) {
        String key = PREFIX + bookingId;
        redisTemplate.opsForValue().set(key, bookingId,
                PAYMENT_TIMEOUT_MINUTES, TimeUnit.MINUTES);
        log.info("Scheduled expiration for booking {} in {} min",
                bookingId, PAYMENT_TIMEOUT_MINUTES);
    }

    // Gọi khi thanh toán thành công → xóa key (không cần cancel nữa)
    public void cancelExpiration(String bookingId) {
        String key = PREFIX + bookingId;
        redisTemplate.delete(key);
        log.info("Cancelled expiration for booking {}", bookingId);
    }
}
