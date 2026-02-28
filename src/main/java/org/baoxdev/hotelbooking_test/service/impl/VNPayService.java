package org.baoxdev.hotelbooking_test.service.impl;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.baoxdev.hotelbooking_test.configuration.VNPayConfig;
import org.baoxdev.hotelbooking_test.dto.response.IpnResponse;
import org.baoxdev.hotelbooking_test.dto.response.PaymentResultResponse;
import org.baoxdev.hotelbooking_test.exception.AppException;
import org.baoxdev.hotelbooking_test.model.entity.Booking;
import org.baoxdev.hotelbooking_test.model.entity.Payments;
import org.baoxdev.hotelbooking_test.model.entity.User;
import org.baoxdev.hotelbooking_test.model.enums.BookingStatus;
import org.baoxdev.hotelbooking_test.model.enums.ErrorCode;
import org.baoxdev.hotelbooking_test.model.enums.PayStatus;
import org.baoxdev.hotelbooking_test.repository.BookingRepository;
import org.baoxdev.hotelbooking_test.repository.PaymentRepository;
import org.baoxdev.hotelbooking_test.repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.lang.reflect.MalformedParameterizedTypeException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.stream.LangCollectors.collect;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE , makeFinal = true)
@Slf4j(topic = "VNPAY_SERVICE")
public class VNPayService {
    VNPayConfig vnPayConfig;
    PaymentRepository paymentRepository;
    BookingRepository bookingRepository;
    UserRepository userRepository;
    NotificationService notificationService;
    BookingExpirationService bookingExpirationService;
    public String createPaymentUrl(String bookingId , String userName , String ipAddress){
        //Get user
        User user = userRepository.findUserByUserName(userName).orElseThrow(()
                -> new AppException(ErrorCode.USER_NOT_FOUND));

        //Get booking
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new AppException(ErrorCode.BOOKING_NOT_FOUND));

        if(booking.getBookingStatus() != BookingStatus.PENDING){
            throw new AppException(ErrorCode.BOOKING_PAID);
        }

        //Generate secure txnRef
        String txnRef = UUID.randomUUID().toString().replace("-" , "");

        //Create payment
        Payments payments = Payments.builder()
                .txnRef(txnRef)
                .amountPayment(booking.getTotalPrice())
                .payStatus(PayStatus.PENDING)
                .booking(booking)
                .user(user)
                .build();

        paymentRepository.save(payments);

        //VNPAY params
        Map<String , String> vnParams = new TreeMap<>();
        vnParams.put("vnp_Version" , "2.1.0");
        vnParams.put("vnp_Command" , "pay");
        vnParams.put("vnp_TmnCode" , vnPayConfig.getTmnCode());
        BigDecimal amount = booking.getTotalPrice()
                .setScale(0, RoundingMode.DOWN)
                .multiply(BigDecimal.valueOf(100));

        vnParams.put("vnp_Amount" , amount +"");
        vnParams.put("vnp_CurrCode" , "VND");
        vnParams.put("vnp_TxnRef" , txnRef);
        vnParams.put("vnp_OrderInfo" , "Thanh toan booking " + booking.getBookingCode());
        vnParams.put("vnp_OrderType" , "other");
        vnParams.put("vnp_Locale" , "vn");
        vnParams.put("vnp_ReturnUrl" , vnPayConfig.getReturnUrl());
        vnParams.put("vnp_IpAddr", ipAddress);
        log.info("ipAdress: {}"  , ipAddress);
        vnParams.put("vnp_CreateDate" , LocalDateTime.now().format( DateTimeFormatter.ofPattern("yyyyMMddHHmmss") ));
        vnParams.put("vnp_ExpireDate" , LocalDateTime.now().plus(15 , ChronoUnit.MINUTES).format( DateTimeFormatter.ofPattern("yyyyMMddHHmmss") ));

        String hashData = buildHashData(vnParams);
        String queryString = buildQueryString(vnParams);

        String secureHash = hmacSHA512(vnPayConfig.getHashSecret(), hashData);
        log.info("QueryString: {}", queryString);
        log.info("HashData: {}", hashData);
        log.info("SecureHash: {}", secureHash);
        log.info("ReturnUrl: {}", vnPayConfig.getReturnUrl());
        return vnPayConfig.getPayUrl()
                + "?" + queryString
                + "&vnp_SecureHash=" + secureHash;

    }

    @Transactional
    public PaymentResultResponse processReturn(Map<String, String> params) throws IOException {
        // 1. Verify checksum
        String vnpSecureHash = params.get("vnp_SecureHash");
        params.remove("vnp_SecureHash");
        params.remove("vnp_SecureHashType");

        String signData = buildHashData(new TreeMap<>(params));
        String calculatedHash = hmacSHA512(vnPayConfig.getHashSecret(), signData);

        if (!calculatedHash.equals(vnpSecureHash)) {
            log.error("Invalid checksum");
            return PaymentResultResponse.builder().success(false).message("Invalid checksum").build();
        }

        // 2. Get payment
        String txnRef = params.get("vnp_TxnRef");
        Payments payment = paymentRepository.findByTxnRef(txnRef)
                .orElseThrow(() -> new AppException(ErrorCode.PAYMENT_NOT_FOUND));

        // 3. Check response code
        String responseCode = params.get("vnp_ResponseCode");
        if ("00".equals(responseCode)) {
            // Payment successful
            payment.setPayStatus(PayStatus.SUCCESS);
            payment.setTransactionNo(params.get("vnp_TransactionNo"));
            payment.setCardType(params.get("vnp_CardType"));
            payment.setCreatedAt(Instant.now());
            payment.setBankCode(responseCode);
            paymentRepository.save(payment);

            // Update booking status
            Booking booking = payment.getBooking();
            booking.setBookingStatus(BookingStatus.CONFIRMED);
            bookingRepository.save(booking);

            //Send email notify success payment
            notificationService.emailNotifyPaymentSuccess(payment);


            return PaymentResultResponse.builder()
                    .success(true)
                    .message("Payment successful")
                    .bookingCode(booking.getBookingCode())
                    .build();
        } else {
            // Payment failed
            payment.setPayStatus(PayStatus.FAILED);
            payment.setBankCode(responseCode);
            paymentRepository.save(payment);

            return PaymentResultResponse.builder()
                    .success(false)
                    .message("Payment failed: " + getErrorMessage(responseCode))
                    .build();
        }
    }


   @Transactional
    public IpnResponse processIpn(Map<String, String> params) {
        // Same verification logic as processReturn
        // But return specific format for VNPay
        String vnpSecureHash = params.get("vnp_SecureHash");
        params.remove("vnp_SecureHash");
        params.remove("vnp_SecureHashType");

        String signData = buildHashData(new TreeMap<>(params));
        String calculatedHash = hmacSHA512(vnPayConfig.getHashSecret(), signData);

        if (!calculatedHash.equals(vnpSecureHash)) {
            return new IpnResponse("97", "Invalid checksum");
        }

        String txnRef = params.get("vnp_TxnRef");
        Optional<Payments> paymentOpt = paymentRepository.findByTxnRef(txnRef);

        if (paymentOpt.isEmpty()) {
            return new IpnResponse("01", "Order not found");
        }

        Payments payment = paymentOpt.get();

        // Check if already processed
        if (payment.getPayStatus()== PayStatus.SUCCESS) {
            return new IpnResponse("00", "Already confirmed");
        }

        String responseCode = params.get("vnp_ResponseCode");
        if ("00".equals(responseCode)) {
            payment.setPayStatus(PayStatus.SUCCESS);
            payment.setTransactionNo(params.get("vnp_TransactionNo"));
            paymentRepository.save(payment);

            Booking booking = payment.getBooking();
            booking.setBookingStatus(BookingStatus.CONFIRMED);
            bookingRepository.save(booking);

            // Cancel scheduled expiration (user already paid)
            bookingExpirationService.cancelExpiration(booking.getBookingId());

        }

        return new IpnResponse("00", "Confirm success");
    }


    private String hmacSHA512(String key, String data) {
        try {
            Mac hmac = Mac.getInstance("HmacSHA512");
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
            hmac.init(secretKey);
            byte[] hash = hmac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString( 0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error generating HMAC", e);
        }
    }

    private String buildQueryString(Map<String, String> params) {
        return params.entrySet().stream()
                .filter(e -> e.getValue() != null && !e.getValue().isEmpty())
                .map(e -> URLEncoder.encode(e.getKey(), StandardCharsets.US_ASCII)
                        + "=" + URLEncoder.encode(e.getValue(), StandardCharsets.US_ASCII))
                .collect(Collectors.joining("&"));
    }


    private String getErrorMessage(String code) {
        return switch (code) {
            case "07" -> "Trừ tiền thành công nhưng giao dịch bị nghi ngờ";
            case "09" -> "Thẻ/Tài khoản chưa đăng ký InternetBanking";
            case "10" -> "Xác thực thông tin thẻ/tài khoản không đúng quá 3 lần";
            case "11" -> "Đã hết hạn chờ thanh toán";
            case "12" -> "Thẻ/Tài khoản bị khóa";
            case "24" -> "Khách hàng hủy giao dịch";
            case "51" -> "Tài khoản không đủ số dư";
            case "65" -> "Tài khoản đã vượt quá hạn mức giao dịch trong ngày";
            case "75" -> "Ngân hàng đang bảo trì";
            default -> "Lỗi không xác định";
        };
    }

    private String buildHashData(Map<String, String> params) {
        return params.entrySet().stream()
                .filter(e -> e.getValue() != null && !e.getValue().isEmpty())
                .map(e -> e.getKey() + "="
                        + URLEncoder.encode(e.getValue(), StandardCharsets.US_ASCII))
                .collect(Collectors.joining("&"));
    }
}







