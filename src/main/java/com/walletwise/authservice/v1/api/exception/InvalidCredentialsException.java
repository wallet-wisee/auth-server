package com.walletwise.authservice.v1.api.exception;

public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException() {
        super("The credentials you've provided are invalid, check your email and password, and try again!");
    }
}
