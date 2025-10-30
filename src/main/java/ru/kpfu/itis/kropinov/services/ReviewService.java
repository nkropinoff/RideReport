package ru.kpfu.itis.kropinov.services;

import ru.kpfu.itis.kropinov.dto.*;

import java.util.List;

public interface ReviewService {
    List<FeedbackCategoryDto> getAllFeedbackCategoriesWithTags();
    void createReview(ReviewCreationDto dto);
    PaginatedResult<ReviewTableInfoDto> getReviewsTableInfo(ReviewSortingDto dto);
}
