package ru.kpfu.itis.kropinov.services.impl;

import ru.kpfu.itis.kropinov.dao.FeedbackDao;
import ru.kpfu.itis.kropinov.dto.FeedbackCategoryDto;
import ru.kpfu.itis.kropinov.services.ReviewService;

import java.util.List;

public class ReviewServiceImpl implements ReviewService {
    private final FeedbackDao feedbackDao;

    public ReviewServiceImpl(FeedbackDao feedbackDao) {
        this.feedbackDao = feedbackDao;
    }

    @Override
    public List<FeedbackCategoryDto> getAllFeedbackCategoriesWithTags() {
        return feedbackDao.getAllFeedbackCategoriesWithTags();
    }
}
