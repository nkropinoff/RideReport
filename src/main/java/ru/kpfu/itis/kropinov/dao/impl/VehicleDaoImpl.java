package ru.kpfu.itis.kropinov.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kpfu.itis.kropinov.dao.VehicleDao;
import ru.kpfu.itis.kropinov.exceptions.DataAccessException;

import javax.sql.DataSource;
import java.sql.*;

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
}
