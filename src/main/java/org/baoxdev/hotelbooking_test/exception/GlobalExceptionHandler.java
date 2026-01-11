package org.baoxdev.hotelbooking_test.exception;

import org.baoxdev.hotelbooking_test.dto.ApiResponse;
import org.baoxdev.hotelbooking_test.model.enums.ErrorCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler
    ResponseEntity<ApiResponse> handleRunTimeException(Exception e){
               ApiResponse apiResponse = new ApiResponse();
               apiResponse.setCode(1001);
               apiResponse.setMessage(e.getMessage());
               return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse> handleAppException(AppException e){
        ErrorCode error = e.getErrorCode();
        return ResponseEntity.badRequest().body(ApiResponse.builder()
                        .code(error.getCode())
                        .message(error.getMessage())
                .build());
    }

}
