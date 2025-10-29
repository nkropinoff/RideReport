package ru.kpfu.itis.kropinov.dao;

import ru.kpfu.itis.kropinov.dto.FeedbackCategoryDto;

import java.util.List;

public interface FeedbackDao {
    List<FeedbackCategoryDto> getAllFeedbackCategoriesWithTags();
}
