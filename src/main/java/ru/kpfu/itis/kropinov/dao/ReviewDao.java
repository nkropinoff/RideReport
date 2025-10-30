package ru.kpfu.itis.kropinov.dao;

import ru.kpfu.itis.kropinov.entities.Review;

import java.sql.Connection;

public interface ReviewDao {
    Review saveWithConnection(Review review, Connection connection);
    void saveFeedbackTagConnectedToReviewWithConnection(int reviewId, int tagId, Connection connection);

}
