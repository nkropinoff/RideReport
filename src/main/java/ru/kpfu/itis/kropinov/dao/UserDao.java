package ru.kpfu.itis.kropinov.dao;

import ru.kpfu.itis.kropinov.entities.User;

import java.util.Optional;

public interface UserDao {
    void save(User user);
    Optional<User> findByEmail(String email);
}
