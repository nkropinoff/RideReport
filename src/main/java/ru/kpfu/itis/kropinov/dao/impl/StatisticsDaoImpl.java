package ru.kpfu.itis.kropinov.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kpfu.itis.kropinov.dao.StatisticsDao;
import ru.kpfu.itis.kropinov.dto.CategoryStatDto;
import ru.kpfu.itis.kropinov.exceptions.DataAccessException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StatisticsDaoImpl implements StatisticsDao {
    private final DataSource dataSource;
    private static final Logger logger = LoggerFactory.getLogger(StatisticsDaoImpl.class);

    public StatisticsDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<CategoryStatDto> getCompanyStatistics(int companyId) {
        String sql = """
                        SELECT
                            fc.id,
                            fc.name,
                            COUNT(CASE WHEN ft.type = 'positive' AND r.id IS NOT NULL THEN 1 END) as positive_count,
                            COUNT(CASE WHEN ft.type = 'negative' AND r.id IS NOT NULL THEN 1 END) as negative_count
                        FROM feedback_categories fc
                                 LEFT JOIN feedback_tags ft ON fc.id = ft.category_id
                                 LEFT JOIN review_tags rt ON ft.id = rt.tag_id
                                 LEFT JOIN reviews rw ON rt.review_id = rw.id
                                 LEFT JOIN route_vehicles rv ON rw.route_id = rv.route_id AND rw.vehicle_number = rv.vehicle_number
                                 LEFT JOIN routes r ON rv.route_id = r.id AND r.company_id = ?
                        GROUP BY fc.id, fc.name
                        ORDER BY fc.id;
                     """;
        List<CategoryStatDto> categoryStatDtos = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, companyId);

            try (ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    categoryStatDtos.add(new CategoryStatDto(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getInt("positive_count"),
                            rs.getInt("negative_count")
                    ));
                }
            }

            return categoryStatDtos;
        } catch (SQLException e) {
            logger.error("Failed to fetch statistics by company id: {}", companyId, e);
            throw new DataAccessException("Failed to fetch statistics by company id: " + companyId, e);
        }
    }

    @Override
    public List<CategoryStatDto> getVehicleStatistics(String vehicleNumber) {
        String sql = """
                        SELECT
                            fc.id,
                            fc.name,
                            COUNT(CASE WHEN ft.type = 'positive' AND rv.route_id IS NOT NULL THEN 1 END) as positive_count,
                            COUNT(CASE WHEN ft.type = 'negative' AND rv.route_id IS NOT NULL THEN 1 END) as negative_count
                        FROM feedback_categories fc
                                 LEFT JOIN feedback_tags ft ON fc.id = ft.category_id
                                 LEFT JOIN review_tags rt ON ft.id = rt.tag_id
                                 LEFT JOIN reviews rv ON rt.review_id = rv.id AND rv.vehicle_number = ?
                        GROUP BY fc.id, fc.name
                        ORDER BY fc.id;
                     """;
        List<CategoryStatDto> categoryStatDtos = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, vehicleNumber);

            try (ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    categoryStatDtos.add(new CategoryStatDto(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getInt("positive_count"),
                            rs.getInt("negative_count")
                    ));
                }
            }

            return categoryStatDtos;
        } catch (SQLException e) {
            logger.error("Failed to fetch statistics by vehicle number: {}", vehicleNumber, e);
            throw new DataAccessException("Failed to fetch statistics by vehicle number: " + vehicleNumber, e);
        }
    }
}
