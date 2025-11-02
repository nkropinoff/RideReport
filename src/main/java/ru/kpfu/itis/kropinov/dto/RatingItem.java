package ru.kpfu.itis.kropinov.dto;

public class RatingItem {
    private String feedbackCategory;
    private String feedbackTag;
    private String tagType;

    public RatingItem(String feedbackCategory, String feedbackTag, String tagType) {
        this.feedbackCategory = feedbackCategory;
        this.feedbackTag = feedbackTag;
        this.tagType = tagType;
    }

    public String getFeedbackCategory() {
        return feedbackCategory;
    }

    public String getFeedbackTag() {
        return feedbackTag;
    }

    public String getTagType() {
        return tagType;
    }
}
