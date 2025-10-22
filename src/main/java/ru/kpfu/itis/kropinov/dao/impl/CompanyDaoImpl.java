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
import java.util.ArrayList;
import java.util.List;
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
        String sql = "insert into companies (user_id, name, inn, status) values (?, ?, ?, ?::verify_status)";

        try {
            PreparedStatement stmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

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

    @Override
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

    @Override
    public List<Company> findAll(int page, int size, String sortOrder, VerifyStatus status) {
        StringBuilder sql = new StringBuilder("SELECT * FROM companies ");

        if (status != null) {
            sql.append("WHERE status = ?::verify_status ");
        }

        String finalSortOrder = "desc".equalsIgnoreCase(sortOrder) ? "DESC" : "ASC";
        sql.append("ORDER BY created_at ").append(finalSortOrder);
        sql.append("LIMIT ? OFFSET ?");

        List<Company> companies = new ArrayList<>();
        try (Connection connection = ds.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql.toString())) {

            int paramIndex = 1;

            if (status != null) {
                stmt.setString(paramIndex++, status.name());
            }

            stmt.setInt(paramIndex++, size);
            stmt.setInt(paramIndex, (page - 1) * size);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                companies.add(new Company(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getString("name"),
                        rs.getString("inn"),
                        VerifyStatus.valueOf(rs.getString("status"))
                ));
            }

            return companies;
        } catch (SQLException e) {
            logger.error("Error fetching companies with pagination", e);
            throw new DataAccessException("Error fetching companies with pagination", e);
        }
    }

    public int countAll(VerifyStatus status) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM companies ");

        if (status != null) {
            sql.append("WHERE status = ?::verify_status");
        }

        try (Connection connection = ds.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql.toString())) {

            if (status != null) {
                stmt.setString(1, status.name());
            }

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            logger.error("Error counting companies", e);
            throw new DataAccessException("Error counting companies", e);
        }

        return 0;
    }
}
