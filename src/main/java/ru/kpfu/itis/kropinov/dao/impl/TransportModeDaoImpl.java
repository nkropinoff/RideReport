package ru.kpfu.itis.kropinov.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kpfu.itis.kropinov.dao.CityDao;
import ru.kpfu.itis.kropinov.dao.TransportModeDao;
import ru.kpfu.itis.kropinov.entities.TransportMode;
import ru.kpfu.itis.kropinov.exceptions.DataAccessException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TransportModeDaoImpl implements TransportModeDao {
    private static final Logger logger = LoggerFactory.getLogger(TransportModeDaoImpl.class);
    private final DataSource ds;

    public TransportModeDaoImpl(DataSource ds) {
        this.ds = ds;
    }

    @Override
    public List<TransportMode> findAll() {
        String sql = "SELECT id, name FROM transport_modes";
        List<TransportMode> transportModes = new ArrayList<>();

        try (Connection connection = ds.getConnection();
             Statement stmt = connection.createStatement()) {

            try (ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    transportModes.add(new TransportMode(
                            rs.getInt("id"),
                            rs.getString("name")
                    ));
                }
            }

            return transportModes;
        } catch (SQLException e) {
            logger.error("Failed to fetch transport modes", e);
            throw new DataAccessException("Failed to fetch transport modes", e);
        }
    }
}
