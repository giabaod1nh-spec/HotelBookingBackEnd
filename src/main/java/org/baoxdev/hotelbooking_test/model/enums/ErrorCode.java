package org.baoxdev.hotelbooking_test.model.enums;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public enum ErrorCode {
    USER_EXISTED(1001 , "Nguoi dung da ton tai" , HttpStatus.BAD_GATEWAY),
    AUTHENTICATED_FAILED(1002 , "Username or password incorrect" , HttpStatus.NOT_FOUND),
    CREATETOKEN_FAILED(1003 ,"Failed when create token" , HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(1004, "User not found" , HttpStatus.NOT_FOUND),
    ROLE_NOT_FOUND(1005 , "Not found any role" , HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(1006 , "authentication failed" , HttpStatus.UNAUTHORIZED),
    TOKEN_INVALID(1007, "Token invalid" , HttpStatus.BAD_REQUEST),
    TOKEN_EXPIRED(1008 , "Token expiration timed out" , HttpStatus.BAD_REQUEST),
    TOKEN_TYPE_INVALID(1009 , "Token type is not refresh" , HttpStatus.BAD_REQUEST),
    TOKEN_NOT_FOUND(1010 , "Refresh token not found in DB" , HttpStatus.BAD_REQUEST),
    TOKEN_REVOKED(1011 , "Token exists in blacklists" , HttpStatus.BAD_REQUEST),
    HOTEL_NOT_FOUND(1012 , "Hotel not exists", HttpStatus.BAD_REQUEST),
    ROOM_NOT_FOUND(1013 , "Room not exists" , HttpStatus.BAD_REQUEST),
    FILE_MUST_BE_IMAGE(1014 , "Wrong file types" , HttpStatus.BAD_REQUEST),
    FAILED_UPLOAD(1015 , "Upload file failed , check again" , HttpStatus.BAD_REQUEST),
    IMAGE_NOT_FOUND(1016 , "Image not found" , HttpStatus.NOT_FOUND),
    ROOM_TYPE_NOT_FOUND(1017 , "Not found any room types" , HttpStatus.NOT_FOUND),
    ROOM_TYPE_IMAGE_NOT_FOUND(1018 , "Not found any room type images" , HttpStatus.NOT_FOUND),
    ROOM_AVAILABLE_NOT_ENOUGH(1019 , "Not enough room left to book " , HttpStatus.NOT_FOUND),
    ROOM_NOT_AVAILABLE(1028 , "Room status is not available" , HttpStatus.BAD_REQUEST),
    BOOKING_NOT_FOUND(1020 , "Access denied to this booking" , HttpStatus.NOT_FOUND),
    BOOKING_ACCESS_DENIED(1021 , "Access denied to this booking" , HttpStatus.FORBIDDEN),
    BOOKING_CANNOT_CANCEL(1022 , "Cannot cancel this booking" , HttpStatus.BAD_REQUEST),
    VERIFICATION_TOKEN_INVALID(1023, "Cannot find token in verify list" , HttpStatus.BAD_REQUEST),
    BOOKING_NOT_CONFIRMED(1024 ,"Booking is not confirmed , check again" , HttpStatus.BAD_REQUEST),
    INSUFFIECNT_ROOM(1025 , "Insufficent room for booking, check again" , HttpStatus.BAD_REQUEST),
    BOOKING_ROOM_NOT_FOUND(1026 , "Booking room not found" , HttpStatus.BAD_REQUEST),
    ROOM_TYPE_MISMATCHED(1027 , "RoomType and Rome not matched type" , HttpStatus.BAD_REQUEST),
    LOCK_NOT_ACQUIRED(1028 , "Lock not acquired" , HttpStatus.LOCKED),
    LOCK_IS_BUSY(1029 , "Lock is busy , user please try again" ,HttpStatus.LOCKED),
    LOCK_INTERUPTED(1030 , "Lock is interupted" , HttpStatus.BAD_REQUEST),
    BOOKING_PAID(1031 , "Booking already paid " , HttpStatus.BAD_REQUEST),
    PAYMENT_NOT_FOUND(1032 , "Not found this payment" , HttpStatus.BAD_REQUEST),
    BOOKING_NOT_CHECK_IN(1033 , "Booking not in check in status " , HttpStatus.BAD_REQUEST),
    AMENITY_NOT_FOUND(1034 , "Amenity not found" , HttpStatus.BAD_REQUEST),
    REVIEW_AFTER_BOOKING_COMPLETED(1035 , "Review denied" , HttpStatus.BAD_REQUEST),
    HOTEL_IMAGE_NOT_FOUND(1036 , "Hotel image not found" , HttpStatus.BAD_REQUEST)
    ;


    private int code;
    private String message;
    private HttpStatusCode httpStatusCode;

    ErrorCode(int code , String message , HttpStatusCode httpStatusCode){
        this.code = code;
        this.message = message;
        this.httpStatusCode = httpStatusCode;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatusCode getHttpStatusCode() {
        return httpStatusCode;
    }
}
