package ru.kpfu.itis.kropinov.exceptions;

public class UserLookupByEmailException extends RuntimeException {
    public UserLookupByEmailException(String message) {
        super(message);
    }
    public UserLookupByEmailException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
