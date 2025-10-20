package ru.kpfu.itis.kropinov.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kpfu.itis.kropinov.dao.CompanyDao;
import ru.kpfu.itis.kropinov.dto.Result;
import ru.kpfu.itis.kropinov.entities.Company;
import ru.kpfu.itis.kropinov.enums.VerifyStatus;
import ru.kpfu.itis.kropinov.exceptions.DataAccessException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class CompanyDaoImpl implements CompanyDao {
    private static final Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);
    private final DataSource ds;

    public CompanyDaoImpl(DataSource ds) {
        this.ds = ds;
    }

    @Override
    public Company save(Company company) {
        try (Connection connection = ds.getConnection()) {
            return saveWithConnection(company, connection);
        } catch (SQLException e) {
            logger.error("Could not obtain a database connection for saving company");
            throw new DataAccessException("Could not obtain a database connection for saving company", e);
        }
    }

    @Override
    public Company saveWithConnection(Company company, Connection connection) {
        String sql = "insert into companies (user_id, name, inn, verify_status) values (?, ?, ?, ?::verify_status)";

        try {
            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setInt(1, company.getUserId());
            stmt.setString(2, company.getCompanyName());
            stmt.setString(3, company.getInn());
            stmt.setString(4, company.getStatus().name());

            int result = stmt.executeUpdate();
            if (result == 0) {
                logger.error("Company with user_id {} was not saved, executeUpdate returned 0", company.getUserId());
                throw new DataAccessException("Company was not saved");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    company.setId(generatedKeys.getInt(1));
                } else {
                    logger.error("Company was saved, but no id obtained");
                    throw new DataAccessException("Company was saved, but no id obtained");
                }
            }

            return company;
        } catch (SQLException e) {
            logger.error("Error while saving company with user_id {}", company.getUserId(), e);
            throw new DataAccessException("Error while saving company with user_id: " + company.getUserId(), e);
        }
    }

    public Optional<Company> findByUserId(int userId) {
        String sql = "select * from companies where user_id = ?";
        try (Connection connection = ds.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);

            ResultSet rs = stmt.executeQuery();
            Company company = null;
            if (rs.next()) {
                company = new Company(
                        rs.getInt(1),
                        rs.getInt(2),
                        rs.getString(3),
                        rs.getString(4),
                        VerifyStatus.valueOf(rs.getString(5))

                );
            }
            return Optional.ofNullable(company);
        } catch (SQLException e) {
            logger.error("Error while finding company by userId: {}", userId, e);
            throw new DataAccessException("Error while finding company by userId: " + userId, e);
        }
    };
}
