package ru.kpfu.itis.kropinov.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kpfu.itis.kropinov.dao.RouteDao;
import ru.kpfu.itis.kropinov.exceptions.DataAccessException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RouteDaoImpl implements RouteDao {
    private DataSource ds;
    private final static Logger logger = LoggerFactory.getLogger(RouteDaoImpl.class);

    public RouteDaoImpl(DataSource ds) {
        this.ds = ds;
    }

    @Override
    public boolean existsByCompanyIdAndCityIdAndRouteNumber(int companyId, int cityId, String routeNumber) {
        String sql = "SELECT EXISTS(SELECT 1 FROM routes WHERE number = ? AND city_id = ? AND company_id = ?)";

        try (Connection connection = ds.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, routeNumber);
            stmt.setInt(2, cityId);
            stmt.setInt(3, companyId);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getBoolean(1);
            }
        } catch (SQLException e) {
            logger.error("Failed to check route number {} for city {} and companyId {}", routeNumber, cityId, companyId, e);
            throw new DataAccessException("Failed to check route number existence", e);
        }
    }
}
