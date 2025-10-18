package ru.kpfu.itis.kropinov.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kpfu.itis.kropinov.dao.UserDao;
import ru.kpfu.itis.kropinov.dto.Result;
import ru.kpfu.itis.kropinov.entities.User;
import ru.kpfu.itis.kropinov.enums.Role;
import ru.kpfu.itis.kropinov.services.UserService;
import ru.kpfu.itis.kropinov.utils.PasswordUtil;

public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public Result<Void> registerPassenger(String email, String password) {
        if (!isValidEmail(email)) return Result.error("Email не соответствует формату.");
        if (!isValidPassword(password)) return Result.error("Длина пароля должна быть не менее 8 символов.");

        if (isEmailTaken(email)) {
            logger.warn("User with email {} already exist.", email);
            return Result.error("Пользователь с таким email уже существует.");
        }

        String hashedPassword = PasswordUtil.encrypt(password);
        User passenger = new User(email, hashedPassword, Role.PASSENGER);

        userDao.save(passenger);
        return Result.success();
    }

    @Override
    public Result<Void> registerCompany(String email, String password, String companyName, String inn) {
        // TODO: implement company registration
        return null;
    }

    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
    }

    private boolean isValidPassword(String password) {
        return !(password == null || password.length() < 8);
    }

    public boolean isEmailTaken(String email) {
        return userDao.findByEmail(email).isPresent();
    }
}