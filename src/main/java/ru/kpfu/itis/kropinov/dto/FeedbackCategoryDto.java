package ru.kpfu.itis.kropinov.dto;

public class FeedbackCategoryDto {
    private int id;
    private String name;

    private int positiveTagId;
    private String positiveTagName;

    private int negativeTagId;
    private String negativeTagName;

    public int getId() {
        return id;
    }

    public FeedbackCategoryDto(int id, String name, int positiveTagId, String positiveTagName, int negativeTagId, String negativeTagName) {
        this.id = id;
        this.name = name;
        this.positiveTagId = positiveTagId;
        this.positiveTagName = positiveTagName;
        this.negativeTagId = negativeTagId;
        this.negativeTagName = negativeTagName;
    }

    public String getName() {
        return name;
    }

    public int getPositiveTagId() {
        return positiveTagId;
    }

    public String getPositiveTagName() {
        return positiveTagName;
    }

    public int getNegativeTagId() {
        return negativeTagId;
    }

    public String getNegativeTagName() {
        return negativeTagName;
    }
}
