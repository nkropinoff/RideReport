package ru.kpfu.itis.kropinov.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kpfu.itis.kropinov.dao.UserDao;
import ru.kpfu.itis.kropinov.dto.Result;
import ru.kpfu.itis.kropinov.dto.UserSessionDto;
import ru.kpfu.itis.kropinov.entities.User;
import ru.kpfu.itis.kropinov.enums.Role;
import ru.kpfu.itis.kropinov.services.UserService;
import ru.kpfu.itis.kropinov.utils.PasswordUtil;

import java.util.Optional;

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

    public Result<UserSessionDto> login(String email, String password) {
        if (!isValidEmail(email)) return Result.error("Email не соответствует формату.");
        if (!isValidPassword(password)) return Result.error("Длина пароля должна быть не менее 8 символов.");

        Optional<User> userOptional = userDao.findByEmail(email);
        if (userOptional.isEmpty()) {
            logger.warn("User with email: {} failed login - wrong email of password", email);
            return Result.error("Неверный email или пароль.");
        }

        User user = userOptional.get();
        if (!PasswordUtil.check(password, user.getHashedPassword())) {
            logger.warn("User with email: {} failed login - wrong email of password", email);
            return Result.error("Неверный email или пароль.");
        }

        return Result.success(createSessionDto(user));
    }

    private UserSessionDto createSessionDto(User user) {
        return switch (user.getRole()) {
            case ADMIN -> UserSessionDto.forAdmin(user.getId(), user.getEmail());
            case PASSENGER -> UserSessionDto.forPassenger(user.getId(), user.getEmail());
            case COMPANY -> {
                // TODO: add companyInfo data by using CompanyDao
                UserSessionDto.CompanyInfo companyInfo = null;
                yield UserSessionDto.forCompany(user.getId(), user.getEmail(), companyInfo.getCompanyId(), companyInfo.getCompanyName(), companyInfo.getStatus());
            }
        };
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