package ru.kpfu.itis.kropinov.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

public class CategoryStatDto implements Serializable {
    private final int categoryId;
    private final String categoryName;
    private final int positiveCount;
    private final int negativeCount;
    private final int positivePercent;
    private final int negativePercent;

    public CategoryStatDto(int categoryId, String categoryName, int positiveCount, int negativeCount) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.positiveCount = positiveCount;
        this.negativeCount = negativeCount;

        int total = positiveCount + negativeCount;
        if (total > 0) {
            this.positivePercent = (int) Math.round( ((double) positiveCount / total) * 100);
            this.negativePercent = (int) Math.round( ((double) negativeCount / total) * 100);
        } else {
            this.negativePercent = 0;
            this.positivePercent = 0;
        }
    }

    public int getPositivePercent() {
        return positivePercent;
    }

    public int getNegativePercent() {
        return negativePercent;
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