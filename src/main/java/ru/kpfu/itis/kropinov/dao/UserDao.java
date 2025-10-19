package ru.kpfu.itis.kropinov.dao;

import ru.kpfu.itis.kropinov.entities.User;

import java.sql.Connection;
import java.util.Optional;

public interface UserDao {
    User save(User user);
    Optional<User> findByEmail(String email);
    User saveWithConnection(User user, Connection connection);
}
