package ru.kpfu.itis.kropinov.exceptions;

public class UserNotSavedException extends RuntimeException {
    public UserNotSavedException(String message) {
        super(message);
    }

    public UserNotSavedException(String message, Throwable throwable) {super(message, throwable);}
}
