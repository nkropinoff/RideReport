package ru.kpfu.itis.kropinov.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kpfu.itis.kropinov.dao.FeedbackDao;
import ru.kpfu.itis.kropinov.dto.FeedbackCategoryDto;
import ru.kpfu.itis.kropinov.exceptions.DataAccessException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class FeedbackDaoImpl implements FeedbackDao {
    private final static Logger logger = LoggerFactory.getLogger(FeedbackDaoImpl.class);
    private final DataSource ds;

    public FeedbackDaoImpl(DataSource ds) {
        this.ds = ds;
    }

    @Override
    public List<FeedbackCategoryDto> getAllFeedbackCategoriesWithTags() {
        String sql = """
                SELECT
                    c.id,
                    c.name,
                    pos.id as positive_tag_id,
                    pos.name as positive_tag_name,
                    neg.id as negative_tag_id,
                    neg.name as negative_tag_name
                FROM feedback_categories c
                LEFT JOIN feedback_tags pos ON c.id = pos.category_id AND pos.type = 'positive'
                LEFT JOIN feedback_tags neg ON c.id = neg.category_id AND neg.type = 'negative'
                ORDER BY c.id;
                """;

        List<FeedbackCategoryDto> feedbackCategories = new ArrayList<>();
        try (Connection connection = ds.getConnection();
            Statement stmt = connection.createStatement()) {

            try (ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    feedbackCategories.add(new FeedbackCategoryDto(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getInt("positive_tag_id"),
                            rs.getString("positive_tag_name"),
                            rs.getInt("negative_tag_id"),
                            rs.getString("negative_tag_name")
                    ));
                }
            }
            return feedbackCategories;
        } catch (SQLException e) {
            logger.error("Failed to fetch all feedback categories with tags", e);
            throw new DataAccessException("Failed to fetch all feedback categories with tags", e);
        }
    }
}
