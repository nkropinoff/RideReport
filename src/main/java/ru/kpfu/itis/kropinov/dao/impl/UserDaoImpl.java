package ru.kpfu.itis.kropinov.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kpfu.itis.kropinov.dao.UserDao;
import ru.kpfu.itis.kropinov.entities.User;
import ru.kpfu.itis.kropinov.enums.Role;
import ru.kpfu.itis.kropinov.exceptions.UserLookupByEmailException;
import ru.kpfu.itis.kropinov.exceptions.UserNotSavedException;

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
    public void save(User user) {
        String sql = "insert into users (email, password_hash, role) values (?, ?, ?::user_role)";

        try (Connection conn = ds.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getHashedPassword());
            stmt.setString(3, user.getRole().name());

            int result = stmt.executeUpdate();
            if (result == 0) {
                logger.warn("User with email {} was not saved, executeUpdate returned 0.", user.getEmail());
                throw new UserNotSavedException("User was not saved");
            }
        } catch (SQLException e) {
            logger.error("Error while saving user with email {}", user.getEmail(), e);
            throw new UserNotSavedException("Error while saving user", e);
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        String sql = "select * from users where email = ?";
        try (Connection conn = ds.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);

            ResultSet rs = stmt.executeQuery();
            User user = null;
            if (rs != null && rs.next()) {
                user = new User(
                        rs.getString("email"),
                        rs.getString("password_hash"),
                        Role.valueOf(rs.getString("role"))
                );
            }
            return user == null ? Optional.empty() : Optional.of(user);
        } catch (SQLException e) {
            logger.error("Error while finding user by email {}", email, e);
            throw new UserLookupByEmailException("Error while finding user by email: " + email, e);
        }
    }
}
