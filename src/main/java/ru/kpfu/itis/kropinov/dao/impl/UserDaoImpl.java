package ru.kpfu.itis.kropinov.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kpfu.itis.kropinov.dao.UserDao;
import ru.kpfu.itis.kropinov.entities.User;
import ru.kpfu.itis.kropinov.enums.Role;
import ru.kpfu.itis.kropinov.exceptions.DataAccessException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class UserDaoImpl implements UserDao {
    private static final Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);
    private final DataSource ds;

    public UserDaoImpl(DataSource ds) {
        this.ds = ds;
    }

    @Override
    public User save(User user) {
        try (Connection connection = ds.getConnection()) {
            return saveWithConnection(user, connection);
        } catch (SQLException e) {
            logger.error("Could not obtain a database connection for saving user");
            throw new DataAccessException("Could not obtain a database connection for saving user", e);
        }
    }

    @Override
    public User saveWithConnection(User user, Connection connection) {
        String sql = "insert into users (email, password_hash, role) values (?, ?, ?::user_role)";

        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getHashedPassword());
            stmt.setString(3, user.getRole().name());

            int result = stmt.executeUpdate();
            if (result == 0) {
                logger.warn("User with email {} was not saved, executeUpdate returned 0.", user.getEmail());
                throw new DataAccessException("User was not saved");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getInt(1));
                } else {
                    logger.error("User was saved, but no id obtained");
                    throw new DataAccessException("User was saved, but no id obtained");
                }
            }

            return user;
        } catch (SQLException e) {
            logger.error("Error while saving user with email {}", user.getEmail(), e);
            throw new DataAccessException("Error while saving user with email: " + user.getEmail(), e);
        }

    }

    @Override
    public Optional<User> findByEmail(String email) {
        String sql = "select * from users where email = ?";
        try (Connection connection = ds.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);

            ResultSet rs = stmt.executeQuery();
            User user = null;
            if (rs != null && rs.next()) {
                user = new User(
                        rs.getInt(1),
                        rs.getString("email"),
                        rs.getString("password_hash"),
                        Role.valueOf(rs.getString("role"))
                );
            }
            return user == null ? Optional.empty() : Optional.of(user);
        } catch (SQLException e) {
            logger.error("Error while finding user by email {}", email, e);
            throw new DataAccessException("Error while finding user by email: " + email, e);
        }
    }
}
