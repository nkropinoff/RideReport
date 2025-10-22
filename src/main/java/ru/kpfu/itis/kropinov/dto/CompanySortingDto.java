package ru.kpfu.itis.kropinov.dto;

import ru.kpfu.itis.kropinov.enums.VerifyStatus;

public class CompanySortingDto {
    private final int page;
    private final int size;
    private final String sortOrder;
    private final VerifyStatus status;

    public int getPage() {
        return page;
    }

    public int getSize() {
        return size;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public VerifyStatus getStatus() {
        return status;
    }

    public CompanySortingDto(int page, int size, String sortOrder, VerifyStatus status) {
        this.page = page;
        this.size = size;
        this.sortOrder = sortOrder;
        this.status = status;
    }
}
