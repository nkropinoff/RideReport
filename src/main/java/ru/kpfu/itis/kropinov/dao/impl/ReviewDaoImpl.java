package ru.kpfu.itis.kropinov.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kpfu.itis.kropinov.dao.ReviewDao;
import ru.kpfu.itis.kropinov.entities.Review;
import ru.kpfu.itis.kropinov.exceptions.DataAccessException;

import java.sql.*;

public class ReviewDaoImpl implements ReviewDao {
    private final static Logger logger = LoggerFactory.getLogger(ReviewDaoImpl.class);


    @Override
    public Review saveWithConnection(Review review, Connection connection) {
        String sql = """
                INSERT INTO reviews (owner_id, route_id, vehicle_number, ride_time, text)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, review.getOwnerId());
            stmt.setInt(2, review.getRouteId());
            stmt.setString(3, review.getVehicleNumber());
            stmt.setObject(4, review.getRideTime());
            stmt.setString(5, review.getText());

            int result = stmt.executeUpdate();
            if (result == 0) {
                logger.error("Review was not saved, executeUpdate returned 0");
                throw new DataAccessException("Review was not saved");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    review.setId(generatedKeys.getInt(1));
                } else {
                    logger.error("Review was saved, but no id obtained");
                    throw new DataAccessException("Review was saved, but no id obtained");
                }
            }

            return review;
        } catch (SQLException e) {
            logger.error("Review was not saved", e);
            throw new DataAccessException("Review was not saved", e);
        }
    }

    @Override
    public void saveFeedbackTagConnectedToReviewWithConnection(int reviewId, int tagId, Connection connection) {
        String sql = "INSERT INTO review_tags (review_id, tag_id) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, reviewId);
            stmt.setInt(2, tagId);

            int result = stmt.executeUpdate();
            if (result == 0) {
                logger.error("Feedback tag connected to review was not saved, executeUpdate returned 0");
                throw new DataAccessException("Feedback tag connected to review was not saved");
            }
        } catch (SQLException e) {
            logger.error("Feedback tag connected to review was not saved", e);
            throw new DataAccessException("Feedback tag connected to review was not saved", e);
        }
    }
}
