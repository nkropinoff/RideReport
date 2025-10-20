package ru.kpfu.itis.kropinov.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kpfu.itis.kropinov.dao.CompanyDao;
import ru.kpfu.itis.kropinov.dao.UserDao;
import ru.kpfu.itis.kropinov.dto.Result;
import ru.kpfu.itis.kropinov.dto.UserSessionDto;
import ru.kpfu.itis.kropinov.entities.Company;
import ru.kpfu.itis.kropinov.entities.User;
import ru.kpfu.itis.kropinov.enums.Role;
import ru.kpfu.itis.kropinov.exceptions.DataAccessException;
import ru.kpfu.itis.kropinov.services.UserService;
import ru.kpfu.itis.kropinov.utils.PasswordUtil;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserDao userDao;
    private final DataSource dataSource;
    private final CompanyDao companyDao;

    public UserServiceImpl(DataSource dataSource, UserDao userDao, CompanyDao companyDao) {
        this.dataSource = dataSource;
        this.userDao = userDao;
        this.companyDao = companyDao;
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
        if (!isValidEmail(email)) return Result.error("Email не соответствует формату.");
        if (!isValidPassword(password)) return Result.error("Длина пароля должна быть не менее 8 символов.");
        if (!isValidInn(inn)) return Result.error("ИНН должен состоять из 10 или 12 цифр.");
        if (!isValidCompanyName(companyName)) return Result.error("Название компании не может быть пустым.");

        if (isEmailTaken(email)) {
            logger.warn("User with email {} connected to company already exist.", email);
            return Result.error("Пользователь с таким email, представляющий компанию, уже существует.");
        }

        try (Connection connection = dataSource.getConnection()) {
            try {
                connection.setAutoCommit(false);

                String hashedPassword = PasswordUtil.encrypt(password);
                User companyUser = new User(email, hashedPassword, Role.COMPANY);
                User savedUser = userDao.saveWithConnection(companyUser, connection);

                Company company = new Company(savedUser.getId(), companyName, inn);
                companyDao.saveWithConnection(company, connection);

                connection.commit();
                return Result.success();
            } catch (SQLException | DataAccessException e) {
                rollback(connection);
                logger.error("Failed during company registration transaction.", e);
                throw new DataAccessException("Failed during company registration transaction.", e);
            }
        } catch (SQLException e) {
            logger.error("Could not obtain database connection.", e);
            throw new DataAccessException("Could not obtain database connection.", e);
        }
    }

    @Override
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
                Optional<Company> companyOptional = companyDao.findByUserId(user.getId());
                if (companyOptional.isEmpty()) {
                    logger.error("User id {} has COMPANY role but no company record", user.getId());
                    throw new DataAccessException("Inconsistent data: COMPANY user without company record");
                }
                Company company = companyOptional.get();
                yield UserSessionDto.forCompany(user.getId(), user.getEmail(), company.getUserId(), company.getCompanyName(), company.getStatus());
            }
        };
    }

    private void rollback(Connection connection) {
        if (connection != null) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                logger.error ("Cound not rollback connection", e);
            }
        }
    }

    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
    }

    private boolean isValidPassword(String password) {
        return !(password == null || password.length() < 8);
    }

    private boolean isValidInn(String inn) {
        return inn != null && inn.matches("\\d{10}|\\d{12}");
    }

    private boolean isValidCompanyName(String companyName) {
        return companyName != null && !companyName.isBlank();
    }

    public boolean isEmailTaken(String email) {
        return userDao.findByEmail(email).isPresent();
    }
}