package com.walletwise.authservice.v1.core.http;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@Builder
@AllArgsConstructor
public class Response {
    private String message;
    private HttpStatus status;
    private int statusCode;
    private Object body;
}