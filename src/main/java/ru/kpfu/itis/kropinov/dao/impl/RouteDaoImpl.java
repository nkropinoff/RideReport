package ru.kpfu.itis.kropinov.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kpfu.itis.kropinov.dao.RouteDao;
import ru.kpfu.itis.kropinov.dto.Result;
import ru.kpfu.itis.kropinov.dto.RouteNumberDto;
import ru.kpfu.itis.kropinov.entities.Route;
import ru.kpfu.itis.kropinov.entities.Vehicle;
import ru.kpfu.itis.kropinov.exceptions.DataAccessException;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RouteDaoImpl implements RouteDao {
    private DataSource ds;
    private final static Logger logger = LoggerFactory.getLogger(RouteDaoImpl.class);

    public RouteDaoImpl(DataSource ds) {
        this.ds = ds;
    }

    @Override
    public boolean existsByTransportModeIdAndCityIdAndRouteNumber(int transportModeId, int cityId, String routeNumber) {
        String sql = "SELECT EXISTS(SELECT 1 FROM routes WHERE number = ? AND city_id = ? AND transport_mode_id = ?)";

        try (Connection connection = ds.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, routeNumber);
            stmt.setInt(2, cityId);
            stmt.setInt(3, transportModeId);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getBoolean(1);
            }
        } catch (SQLException e) {
            logger.error("Failed to check route number {} for city {} and transportModeId {}", routeNumber, cityId, transportModeId, e);
            throw new DataAccessException("Failed to check route number existence", e);
        }
    }

    @Override
    public Route saveRouteWithConnection(Route route, Connection connection) {
        String sql = "INSERT INTO routes (company_id, city_id, transport_mode_id, number) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, route.getCompanyId());
            stmt.setInt(2, route.getCityId());
            stmt.setInt(3, route.getTransportModeId());
            stmt.setString(4, route.getRouteNumber());

            int result = stmt.executeUpdate();
            if (result == 0) {
                logger.error("Route was not saved, executeUpdate returned 0");
                throw new DataAccessException("Route was not saved");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    route.setId(generatedKeys.getInt(1));
                } else {
                    logger.error("Route was saved, but no id obtained");
                    throw new DataAccessException("Route was saved, but no id obtained");
                }
            }

            return route;
        } catch (SQLException e) {
            logger.error("Error while saving route", e);
            throw new DataAccessException("Error while saving company", e);
        }
    }

    @Override
    public void saveVehicleForRouteWithConnection(int routeId, String vehicle, Connection connection) {
        String sql = "INSERT INTO route_vehicles (route_id, vehicle_number) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, routeId);
            stmt.setString(2, vehicle);

            int result = stmt.executeUpdate();
            if (result == 0) {
                logger.error("Vehicle: {} for route with id: {} was not saved, executeUpdate returned 0", vehicle, routeId);
                throw new DataAccessException("Vehicle for route with was not saved");
            }
        } catch (SQLException e) {
            logger.error("Failed saving vehicle: {} for route with id: {}", vehicle, routeId, e);
            throw new DataAccessException("Failed saving vehicle for route", e);
        }
    }

    @Override
    public List<RouteNumberDto> findRouteNumbersByCompanyCityAndTransportMode(int companyId, int cityId, int transportModeId) {
        String sql = "SELECT id, number FROM routes WHERE company_id = ? AND city_id = ? AND transport_mode_id = ?";

        List<RouteNumberDto> routeNumbers = new ArrayList<>();
        try (Connection connection = ds.getConnection();
        PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, companyId);
            stmt.setInt(2, cityId);
            stmt.setInt(3, transportModeId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    routeNumbers.add(new RouteNumberDto(
                            rs.getInt("id"),
                            rs.getString("number"))
                    );
                }
            }
            return routeNumbers;
        } catch (SQLException e) {
            logger.error("Failed fetch route numbers by company id: {}, city id: {}, transport mode id: {}", companyId, cityId, transportModeId, e);
            throw new DataAccessException("Failed fetch route numbers by company id, city id, transport mode id", e);
        }
    }

    @Override
    public boolean isRouteOwnedByCompany(int routeId, int companyId) {
        String sql = "SELECT EXISTS(SELECT 1 FROM routes WHERE id = ? AND company_id = ?)";

        try (Connection connection = ds.getConnection();
        PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, routeId);
            stmt.setInt(2, companyId);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getBoolean(1);
            }
        } catch (SQLException e) {
            logger.error("Failed check route id {} owning to company id {}", routeId, companyId, e);
            throw new DataAccessException("Failed check route owning to company", e);
        }
    }
}
