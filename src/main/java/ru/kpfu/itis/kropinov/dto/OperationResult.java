package ru.kpfu.itis.kropinov.dto;

public class OperationResult {
    private boolean success;
    private String errorMessage;

    public static OperationResult success() {
        return new OperationResult(true, null);
    }

    public static OperationResult error(String errorMessage) {
        return new OperationResult(false, errorMessage);
    }

    private OperationResult(boolean success, String errorMessage) {
        this.success = success;
        this.errorMessage = errorMessage;
    }

    public boolean isSuccess() {
        return this.success;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }
}
