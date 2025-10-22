package ru.kpfu.itis.kropinov.dto;

import java.io.Serializable;
import java.util.List;

public class PaginatedResult<T> implements Serializable {
    private List<T> data;
    private final int totalPages;
    private final int currentPage;

    public PaginatedResult(List<T> data, int totalPages, int currentPage) {
        this.data = data;
        this.totalPages = totalPages;
        this.currentPage = currentPage;
    }

    public List<T> getData() {
        return data;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getCurrentPage() {
        return currentPage;
    }
}
