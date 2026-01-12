package org.baoxdev.hotelbooking_test.model.enums;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public enum ErrorCode {
    USER_EXISTED(1001 , "Nguoi dung da ton tai" , HttpStatus.BAD_GATEWAY),
    AUTHENTICATED_FAILED(1002 , "Username or password incorrect" , HttpStatus.NOT_FOUND),
    CREATETOKEN_FAILED(1003 ,"Failed when create token" , HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(1004 , "User not found" , HttpStatus.BAD_REQUEST)
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
