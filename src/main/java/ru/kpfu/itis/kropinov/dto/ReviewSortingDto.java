package ru.kpfu.itis.kropinov.dto;

public class ReviewSortingDto {
    private final int page;
    private final int size;
    private final String sortOrder;
    private final int companyId;

    public ReviewSortingDto(int page, int size, String sortOrder, int companyId) {
        this.page = page;
        this.size = size;
        this.sortOrder = sortOrder;
        this.companyId = companyId;
    }

    public int getCompanyId() {
        return companyId;
    }

    public int getPage() {
        return page;
    }

    public int getSize() {
        return size;
    }

    public String getSortOrder() {
        return sortOrder;
    }
}
