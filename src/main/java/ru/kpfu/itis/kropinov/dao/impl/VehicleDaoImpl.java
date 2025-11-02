package ru.kpfu.itis.kropinov.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kpfu.itis.kropinov.dao.VehicleDao;
import ru.kpfu.itis.kropinov.entities.Vehicle;
import ru.kpfu.itis.kropinov.exceptions.DataAccessException;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VehicleDaoImpl implements VehicleDao {
    private final static Logger logger = LoggerFactory.getLogger(VehicleDaoImpl.class);
    private final DataSource ds;

    public VehicleDaoImpl(DataSource ds) {
        this.ds = ds;
    }

    @Override
    public boolean existsByVehicleNumber(String vehicleNumber) {
        String sql = "SELECT EXISTS(SELECT 1 FROM route_vehicles WHERE vehicle_number = ?);";
        try (Connection connection = ds.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, vehicleNumber);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getBoolean(1);
            }
        } catch (SQLException e) {
            logger.error("Failed check existing vehicle number {}", vehicleNumber, e);
            throw new DataAccessException("Failed check existing vehicle number", e);
        }
    }

    @Override
    public List<Vehicle> findVehiclesByRouteId(int routeId) {
        String sql = "SELECT vehicle_number FROM route_vehicles WHERE route_id = ?";
        List<Vehicle> vehicles = new ArrayList<>();
        try (Connection connection = ds.getConnection();
        PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, routeId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    vehicles.add(new Vehicle(
                            rs.getString("vehicle_number")
                            )
                    );
                }
            }
            return vehicles;
        } catch (SQLException e) {
            logger.error("Failed to fetch vehicles by route id: {}", routeId, e);
            throw new DataAccessException("Failed to fetch vehicles by route id", e);
        }
    }

    @Override
    public void deleteVehicleNumberWithConnection(String vehicleNumber, Connection connection) {
        String sql = "DELETE FROM route_vehicles WHERE vehicle_number = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, vehicleNumber);

            int result = stmt.executeUpdate();
            if (result == 0) {
                logger.error("Failed deletion vehicle number: {}, executeUpdate returned 0", vehicleNumber);
                throw new DataAccessException("Failed deletion vehicle number, executeUpdate returned 0");
            }
        } catch (SQLException e) {
            logger.error("Failed deletion vehicle number: {}", vehicleNumber);
            throw new DataAccessException("Failed deletion vehicle number", e);
        }
    }

    @Override
    public List<Vehicle> findVehiclesByCompanyId(int companyId) {
        String sql = "SELECT rv.vehicle_number FROM route_vehicles rv JOIN routes r ON rv.route_id = r.id WHERE r.company_id = ?";
        List<Vehicle> vehicles = new ArrayList<>();

        try (Connection connection = ds.getConnection();
        PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, companyId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    vehicles.add(new Vehicle(
                       rs.getString("vehicle_number")
                    ));
                }
            }

            return vehicles;
        } catch (SQLException e) {
            logger.error("Failed to fetch vehicle number by company id: {}", companyId, e);
            throw new DataAccessException("Failed to fetch vehicle number by company id: " + companyId, e);
        }
    }

    @Override
    public boolean isVehicleOwnedByCompany(int companyId, String vehicleNumber) {
        String sql = "SELECT EXISTS(SELECT 1 FROM route_vehicles rv JOIN routes r ON rv.route_id = r.id WHERE r.company_id = ? AND rv.vehicle_number = ?)";

        try (Connection connection = ds.getConnection();
        PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, companyId);
            stmt.setString(2, vehicleNumber);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getBoolean(1);
            }
        } catch (SQLException e) {
            logger.error("Failed check is vehicle number: {} owned by company with id: {}", vehicleNumber, companyId, e);
            throw new DataAccessException("Failed to check is vehicle number owned by company", e);
        }
    }
}
