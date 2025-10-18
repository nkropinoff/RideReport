package ru.kpfu.itis.kropinov.dto;

public class Result<T> {
    private final boolean success;
    private final String errorMessage;
    private final T data;

    public static Result<Void> success() {
        return new Result<>(true, null, null);
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(true, null, data);
    }

    public static <T> Result<T> error(String errorMessage) {
        return new Result<>(false, errorMessage, null);
    }

    private Result(boolean success, String errorMessage, T data) {
        this.success = success;
        this.errorMessage = errorMessage;
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public boolean isSuccess() {
        return this.success;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }
}
