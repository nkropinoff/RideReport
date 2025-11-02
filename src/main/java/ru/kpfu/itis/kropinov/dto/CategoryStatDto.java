package ru.kpfu.itis.kropinov.dto;

public class CategoryStatDto {
    private int categoryId;
    private String categoryName;
    private int positiveCount;
    private int negativeCount;

    public CategoryStatDto(int categoryId, String categoryName, int positiveCount, int negativeCount) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.positiveCount = positiveCount;
        this.negativeCount = negativeCount;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public int getPositiveCount() {
        return positiveCount;
    }

    public int getNegativeCount() {
        return negativeCount;
    }
}