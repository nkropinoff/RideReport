package ru.kpfu.itis.kropinov.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kpfu.itis.kropinov.dao.ReviewPhotoDao;
import ru.kpfu.itis.kropinov.entities.ReviewPhoto;
import ru.kpfu.itis.kropinov.exceptions.DataAccessException;

import java.sql.*;

public class ReviewPhotoDaoImpl implements ReviewPhotoDao {
    private final static Logger logger = LoggerFactory.getLogger(ReviewDaoImpl.class);

    @Override
    public ReviewPhoto saveWithConnection(ReviewPhoto reviewPhoto, Connection connection) {
        String sql = "insert into review_photo (review_id, storage_url, public_id, original_filename, mime_type, size_bytes) values (?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stmt.setInt(1, reviewPhoto.getReviewId());
            stmt.setString(2, reviewPhoto.getUrl());
            stmt.setString(3, reviewPhoto.getPublicId());
            stmt.setString(4, reviewPhoto.getOriginalFilename());
            stmt.setString(5, reviewPhoto.getMimeType());
            stmt.setLong(6, reviewPhoto.getSizeBytes());

            int result = stmt.executeUpdate();
            if (result == 0) {
                logger.error("Review photo with publicId {} was not saved, executeUpdate returned 0", reviewPhoto.getPublicId());
                throw new DataAccessException("Review photo was not saved");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    reviewPhoto.setId(generatedKeys.getInt(1));
                } else {
                    logger.error("Review photo was saved, but no id obtained");
                    throw new DataAccessException("Review photo was saved, but no id obtained");
                }
            }

            return reviewPhoto;
        } catch (SQLException e) {
            logger.error("Error while saving review photo with publicId {}", reviewPhoto.getPublicId(), e);
            throw new DataAccessException("Error while saving review photo with publicId: " + reviewPhoto.getPublicId(), e);
        }
    }
}
