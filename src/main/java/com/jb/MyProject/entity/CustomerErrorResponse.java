package com.example.MyProject.entity;

import org.springframework.http.HttpStatus;

public class CustomerErrorResponse {
    private HttpStatus httpStatus;
    private String message;
    private long timestamp;

    private CustomerErrorResponse(HttpStatus httpStatus, String message, long timestamp) {
        this.httpStatus = httpStatus;
        this.message = message;
        this.timestamp = timestamp;
    }

    public static CustomerErrorResponse of(HttpStatus httpStatus, String message) {
        return new CustomerErrorResponse(httpStatus, message, System.currentTimeMillis());
    }
}
