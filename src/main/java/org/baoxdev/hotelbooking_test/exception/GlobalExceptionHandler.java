package org.baoxdev.hotelbooking_test.exception;

import lombok.extern.slf4j.Slf4j;
import org.baoxdev.hotelbooking_test.dto.ApiResponse;
import org.baoxdev.hotelbooking_test.model.enums.ErrorCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler
    ResponseEntity<ApiResponse> handleRunTimeException(Exception e){

        log.error("Unhandled exception occurred: ", e);
        log.error(e.getLocalizedMessage());

        ApiResponse apiResponse = new ApiResponse();
               apiResponse.setCode(9999);
               apiResponse.setMessage(e.getMessage());
               return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse> handleAppException(AppException e){
        log.warn("AppException: {}", e.getErrorCode());  // Add logging

        ErrorCode error = e.getErrorCode();
        return ResponseEntity.status(error.getHttpStatusCode()).body(ApiResponse.builder()
                        .code(error.getCode())
                        .message(error.getMessage())
                .build());
    }

}
