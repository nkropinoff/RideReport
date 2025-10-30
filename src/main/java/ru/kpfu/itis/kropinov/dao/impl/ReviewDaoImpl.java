package ru.kpfu.itis.kropinov.dao.impl;

import ch.qos.logback.core.spi.DeferredProcessingAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kpfu.itis.kropinov.dao.ReviewDao;
import ru.kpfu.itis.kropinov.dto.ReviewSortingDto;
import ru.kpfu.itis.kropinov.dto.ReviewTableInfoDto;
import ru.kpfu.itis.kropinov.entities.Review;
import ru.kpfu.itis.kropinov.exceptions.DataAccessException;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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

    @Override
    public List<ReviewTableInfoDto> findReviewTableInfoWithConnection(ReviewSortingDto dto, Connection connection) {
        String finalSortOrder = "desc".equalsIgnoreCase(dto.getSortOrder()) ? "DESC" : "ASC";

        String sql = String.format("""
            SELECT 
                r.id AS review_id,
                r.ride_time AS ride_time,
                c.name AS city,
                tm.name AS transport_mode,
                rt.number AS route,
                r.vehicle_number,
                u.email AS passenger_email
            FROM 
                reviews r
                INNER JOIN routes rt ON r.route_id = rt.id
                INNER JOIN cities c ON rt.city_id = c.id
                INNER JOIN transport_modes tm ON rt.transport_mode_id = tm.id
                LEFT JOIN users u ON r.owner_id = u.id
            WHERE 
                rt.company_id = ?
            ORDER BY 
                r.created_at %s
            LIMIT ? OFFSET ?
        """, finalSortOrder);

        int offset = dto.getSize() * (dto.getPage() - 1);

        List<ReviewTableInfoDto> reviewTableInfos = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, dto.getCompanyId());
            stmt.setInt(2, dto.getSize());
            stmt.setInt(3, offset);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    reviewTableInfos.add(new ReviewTableInfoDto(
                            rs.getInt("review_id"),
                            formattedRideTime(rs.getObject("ride_time", LocalDateTime.class)),
                            rs.getString("city"),
                            rs.getString("transport_mode"),
                            rs.getString("route"),
                            rs.getString("vehicle_number"),
                            rs.getString("passenger_email")
                    ));
                }
            }

            return reviewTableInfos;
        } catch (SQLException e) {
            logger.error("Failed to fetch review table info", e);
            throw new DataAccessException("Failed to fetch review table info", e);
        }
    }

    private String formattedRideTime(LocalDateTime time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        return time.format(formatter);
    }

    @Override
    public int countAllReviewsByCompanyWithConnection(int companyId, Connection connection) {
        String sql = "SELECT COUNT(*) FROM reviews r INNER JOIN routes rt ON r.route_id = rt.id WHERE rt.company_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, companyId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
                return 0;
            }
        } catch (SQLException e) {
            logger.error("Failed to count reviews of company with id: {}", companyId, e);
            throw new DataAccessException("Failed to count reviews of company with id: " + companyId, e);
        }
    }
}
