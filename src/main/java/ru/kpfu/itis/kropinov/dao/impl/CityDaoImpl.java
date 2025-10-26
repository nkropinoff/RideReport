package ru.kpfu.itis.kropinov.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kpfu.itis.kropinov.dao.CityDao;
import ru.kpfu.itis.kropinov.entities.City;
import ru.kpfu.itis.kropinov.exceptions.DataAccessException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CityDaoImpl implements CityDao {

    private static final Logger logger = LoggerFactory.getLogger(CityDaoImpl.class);
    private final DataSource ds;

    public CityDaoImpl(DataSource ds) {
        this.ds = ds;
    }

    @Override
    public List<City> findAll() {
        String sql = "SELECT id, name FROM cities";
        List<City> cities = new ArrayList<>();

        try (Connection connection = ds.getConnection();
             Statement stmt = connection.createStatement()) {

            try (ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    cities.add(new City(
                            rs.getInt("id"),
                            rs.getString("name")
                    ));
                }
            }

            return cities;
        } catch (SQLException e) {
            logger.error("Failed to fetch cities", e);
            throw new DataAccessException("Failed to fetch cities", e);
        }
    }
}
