package ru.kpfu.itis.kropinov.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kpfu.itis.kropinov.dao.FeedbackDao;
import ru.kpfu.itis.kropinov.dao.ReviewDao;
import ru.kpfu.itis.kropinov.dao.ReviewPhotoDao;
import ru.kpfu.itis.kropinov.db.CustomDataSource;
import ru.kpfu.itis.kropinov.dto.*;
import ru.kpfu.itis.kropinov.entities.Review;
import ru.kpfu.itis.kropinov.entities.ReviewPhoto;
import ru.kpfu.itis.kropinov.exceptions.DataAccessException;
import ru.kpfu.itis.kropinov.services.FileStorageService;
import ru.kpfu.itis.kropinov.services.ReviewService;

import javax.servlet.http.Part;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ReviewServiceImpl implements ReviewService {
    private final static Logger logger = LoggerFactory.getLogger(ReviewServiceImpl.class);

    private final FileStorageService fileStorageService;
    private final DataSource ds;
    private final FeedbackDao feedbackDao;
    private final ReviewDao reviewDao;
    private final ReviewPhotoDao reviewPhotoDao;

    private final static String REVIEWS_PHOTO_FOLDER = "review-photos";

    public ReviewServiceImpl(DataSource ds, FileStorageService fileStorageService, FeedbackDao feedbackDao, ReviewDao reviewDao, ReviewPhotoDao reviewPhotoDao) {
        this.fileStorageService = fileStorageService;
        this.feedbackDao = feedbackDao;
        this.ds = ds;
        this.reviewDao = reviewDao;
        this.reviewPhotoDao = reviewPhotoDao;
    }

    @Override
    public List<FeedbackCategoryDto> getAllFeedbackCategoriesWithTags() {
        return feedbackDao.getAllFeedbackCategoriesWithTags();
    }

    @Override
    public void createReview(ReviewCreationDto dto) {
        Review review = new Review(dto.getUserId(), dto.getRouteId(), dto.getVehicleNumber(), dto.getRideTime(), dto.getReviewText());

        try (Connection connection = ds.getConnection()) {
            connection.setAutoCommit(false);
            try {
                Review savedReview = reviewDao.saveWithConnection(review, connection);

                for (Integer tagId : dto.getFeedbackTagIds()) {
                    reviewDao.saveFeedbackTagConnectedToReviewWithConnection(savedReview.getId(), tagId, connection);
                }

                if (dto.getPhoto() != null && dto.getPhoto().getSize() > 0) {
                    Part photo = dto.getPhoto();
                    CloudinaryUploadResult uploadResult = fileStorageService.saveFile(photo.getInputStream(), photo.getSubmittedFileName(), photo.getContentType(), REVIEWS_PHOTO_FOLDER);
                    ReviewPhoto reviewPhoto = new ReviewPhoto(
                            savedReview.getId(),
                            uploadResult.getUrl(),
                            uploadResult.getPublicId(),
                            photo.getSubmittedFileName(),
                            uploadResult.getMimeType(),
                            photo.getSize()
                    );
                    reviewPhotoDao.saveWithConnection(reviewPhoto, connection);
                }

                connection.commit();
            } catch (SQLException | IOException | DataAccessException e) {
                CustomDataSource.rollback(connection);
                logger.error("Failed saving review", e);
                throw new DataAccessException("Failed saving review", e);
            }
        } catch (SQLException e) {
            logger.error("Could not obtain connection", e);
            throw new DataAccessException("Could not obtain connection", e);
        }
    }

    @Override
    public PaginatedResult<ReviewTableInfoDto> getReviewsTableInfo(ReviewSortingDto dto) {
        try (Connection connection = ds.getConnection()) {
            List<ReviewTableInfoDto> reviewTableInfoDtos = reviewDao.findReviewTableInfoWithConnection(dto, connection);
            int totalCount = reviewDao.countAllReviewsByCompanyWithConnection(dto.getCompanyId(), connection);
            int totalPages = (int) Math.ceil( (double) totalCount / dto.getSize());
            return new PaginatedResult<>(reviewTableInfoDtos, totalPages, dto.getPage());
        } catch (SQLException e) {
            logger.error("Failed to fetch reviewTableInfos", e);
            throw new DataAccessException("Failed to fetch reviewTableInfos", e);
        }
    }
}
