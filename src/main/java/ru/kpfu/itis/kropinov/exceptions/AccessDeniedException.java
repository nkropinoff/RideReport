package ru.kpfu.itis.kropinov.exceptions;

public class AccessDeniedException extends RuntimeException {
  public AccessDeniedException(String message) {
    super(message);
  }
}
