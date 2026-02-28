package org.baoxdev.hotelbooking_test.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.baoxdev.hotelbooking_test.dto.ApiResponse;
import org.baoxdev.hotelbooking_test.dto.response.IpnResponse;
import org.baoxdev.hotelbooking_test.dto.response.PaymentResultResponse;
import org.baoxdev.hotelbooking_test.dto.response.PaymentUrlResponse;
import org.baoxdev.hotelbooking_test.service.impl.VNPayService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE )
@Slf4j(topic = "PAYMENT_CONTROLLER")
public class PaymentController {
    final VNPayService vnPayService;

    @Value("${app.frontend-url:http://localhost:5173}")
    String frontendUrl;

    @PostMapping("create/{bookingId}")
    public ApiResponse<PaymentUrlResponse> createPayment(@PathVariable String bookingId , HttpServletRequest request){
        String paymentReturnUrl = vnPayService.createPaymentUrl(bookingId , getUserName() , getClientIp(request));

        return ApiResponse.<PaymentUrlResponse>builder()
                .message("create payment success")
                .result(PaymentUrlResponse.builder()
                        .paymentUrl(paymentReturnUrl).build())
                .build();
    }

    @GetMapping("/vnpay-return")
    public ResponseEntity<Void> vnpayReturn(@RequestParam Map<String, String> params) throws IOException {
        PaymentResultResponse result = vnPayService.processReturn(params);
        log.info(result.getBookingCode());
        log.info(result.getMessage());
        // Redirect to frontend with result
        String redirectUrl = result.getSuccess()
                ? frontendUrl + "/booking/success?bookingCode=" + (result.getBookingCode() != null ? result.getBookingCode() : "")
                : frontendUrl + "/booking/failed?message=" + (result.getMessage() != null ? URLEncoder.encode(result.getMessage(), StandardCharsets.UTF_8) : "");

        return ResponseEntity.status(HttpStatus.FOUND)
                .header("Location", redirectUrl)
                .build();
    }

    @GetMapping("/vnpay-ipn")
    public IpnResponse vnpayIpn(@RequestParam Map<String, String> allParams) {
        // Hàm này bạn đã viết trong VNPayService rồi
        return vnPayService.processIpn(allParams);
    }


    private String getUserName(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    public static String getClientIp(HttpServletRequest request) {
        String[] headers = {
                "X-Forwarded-For",
                "Proxy-Client-IP",
                "WL-Proxy-Client-IP",
                "HTTP_X_FORWARDED_FOR",
                "HTTP_X_FORWARDED",
                "HTTP_X_CLUSTER_CLIENT_IP",
                "HTTP_CLIENT_IP",
                "HTTP_FORWARDED_FOR",
                "HTTP_FORWARDED",
                "X-Real-IP"
        };

        for (String header : headers) {
            String ip = request.getHeader(header);
            if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
                return ip.split(",")[0].trim();
            }
        }

        return request.getRemoteAddr();
    }

}
