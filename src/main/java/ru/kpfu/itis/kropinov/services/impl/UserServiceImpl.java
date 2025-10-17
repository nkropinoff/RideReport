package ru.kpfu.itis.kropinov.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kpfu.itis.kropinov.dao.UserDao;
import ru.kpfu.itis.kropinov.dto.OperationResult;
import ru.kpfu.itis.kropinov.entities.User;
import ru.kpfu.itis.kropinov.enums.Role;
import ru.kpfu.itis.kropinov.exceptions.UserNotSavedException;
import ru.kpfu.itis.kropinov.services.UserService;
import ru.kpfu.itis.kropinov.utils.PasswordUtil;

public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public OperationResult registerPassenger(String email, String password) {
        if (!isValidEmail(email)) return OperationResult.error("Email не соответствует формату.");
        if (!isValidPassword(password)) return OperationResult.error("Длина пароля должна быть не менее 8 символов.");

        if (userDao.findByEmail(email).isPresent()) {
            logger.warn("User with email {} exist already.", email);
            return OperationResult.error("Пользователь с таким email уже существует.");
        }

        String hashedPassword = PasswordUtil.encrypt(password);
        User passenger = new User(email, hashedPassword, Role.PASSENGER);

        try {
            userDao.save(passenger);
        } catch (UserNotSavedException e) {
            logger.error("Error while user with email {} registration.", email);
            return OperationResult.error("Ошибка при регистрации пользователя.");
        }

        return OperationResult.success();
    }

    @Override
    public OperationResult registerCompany(String email, String password, String companyName, String inn) {
        // TODO: implement company registration
        return null;
    }

    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
    }

    private boolean isValidPassword(String password) {
        return !(password == null || password.length() < 8);
    }
}