package com.walletwise.authservice.v1.api.exception;

public class InactiveUserException extends RuntimeException {
    public InactiveUserException() {
        super("This account is inactivated, please verify you email to activate you account!");
    }
}
