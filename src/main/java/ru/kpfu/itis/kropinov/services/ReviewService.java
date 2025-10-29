package ru.kpfu.itis.kropinov.services;

import ru.kpfu.itis.kropinov.dto.FeedbackCategoryDto;

import java.util.List;

public interface ReviewService {
    List<FeedbackCategoryDto> getAllFeedbackCategoriesWithTags();
}
