package com.codebase.socialnetwork.domain.exception;

public class WrongPasswordException extends RuntimeException {
    public WrongPasswordException() {
        super("the password is invalid");
    }
}
