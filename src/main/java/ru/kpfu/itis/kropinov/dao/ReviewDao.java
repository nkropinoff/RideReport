package ru.kpfu.itis.kropinov.dao;

import ru.kpfu.itis.kropinov.dto.ReviewSortingDto;
import ru.kpfu.itis.kropinov.dto.ReviewTableInfoDto;
import ru.kpfu.itis.kropinov.entities.Review;

import java.sql.Connection;
import java.util.List;

public interface ReviewDao {
    Review saveWithConnection(Review review, Connection connection);
    void saveFeedbackTagConnectedToReviewWithConnection(int reviewId, int tagId, Connection connection);
    List<ReviewTableInfoDto> findReviewTableInfoWithConnection(ReviewSortingDto dto, Connection connection);
    int countAllReviewsByCompanyWithConnection(int companyId, Connection connection);
}
