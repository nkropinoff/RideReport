package ru.kpfu.itis.kropinov.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kpfu.itis.kropinov.dao.CompanyDao;
import ru.kpfu.itis.kropinov.dao.CompanyDocumentDao;
import ru.kpfu.itis.kropinov.dao.UserDao;
import ru.kpfu.itis.kropinov.dto.*;
import ru.kpfu.itis.kropinov.entities.Company;
import ru.kpfu.itis.kropinov.entities.CompanyDocument;
import ru.kpfu.itis.kropinov.entities.User;
import ru.kpfu.itis.kropinov.enums.Role;
import ru.kpfu.itis.kropinov.exceptions.DataAccessException;
import ru.kpfu.itis.kropinov.services.FileStorageService;
import ru.kpfu.itis.kropinov.services.UserService;
import ru.kpfu.itis.kropinov.utils.PasswordUtil;

import javax.servlet.http.Part;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserDao userDao;
    private final DataSource dataSource;
    private final CompanyDao companyDao;
    private final FileStorageService fileStorageService;
    private final CompanyDocumentDao companyDocumentDao;

    public UserServiceImpl(DataSource dataSource, FileStorageService fileStorageService,  UserDao userDao, CompanyDao companyDao, CompanyDocumentDao companyDocumentDao) {
        this.dataSource = dataSource;
        this.userDao = userDao;
        this.companyDao = companyDao;
        this.fileStorageService = fileStorageService;
        this.companyDocumentDao = companyDocumentDao;
    }

    @Override
    public Result<Void> registerPassenger(PassengerRegistrationDto dto) {
        String email = dto.getEmail();
        String password = dto.getPassword();

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
    public Result<Void> registerCompany(CompanyRegistrationDto dto) {
        String email = dto.getEmail();
        String password = dto.getPassword();
        String companyName = dto.getCompanyName();
        String inn = dto.getInn();
        List<Part> companyDocuments = dto.getCompanyDocuments();

        if (!isValidEmail(email)) return Result.error("Email не соответствует формату.");
        if (!isValidPassword(password)) return Result.error("Длина пароля должна быть не менее 8 символов.");
        if (!isValidInn(inn)) return Result.error("ИНН должен состоять из 10 или 12 цифр.");
        if (!isValidCompanyName(companyName)) return Result.error("Название компании не может быть пустым.");
        Result<Void> companyDocumentsValidation = validCompanyDocuments(companyDocuments);
        if (!companyDocumentsValidation.isSuccess()) return companyDocumentsValidation;

        if (isEmailTaken(email)) {
            logger.warn("User with email {} connected to company already exist.", email);
            return Result.error("Пользователь с таким email, представляющий компанию, уже существует.");
        }


        List<String> savedStorageIDs = new ArrayList<>();

        try (Connection connection = dataSource.getConnection()) {
            try {
                connection.setAutoCommit(false);

                String hashedPassword = PasswordUtil.encrypt(password);
                User companyUser = new User(email, hashedPassword, Role.COMPANY);
                User savedUser = userDao.saveWithConnection(companyUser, connection);

                Company company = new Company(savedUser.getId(), companyName, inn);
                companyDao.saveWithConnection(company, connection);

                for (Part part : companyDocuments) {
                   String storageID = fileStorageService.saveFile(part.getInputStream(), part.getSubmittedFileName(), part.getContentType());
                   savedStorageIDs.add(storageID);

                    CompanyDocument document = new CompanyDocument(company.getId(), storageID, part.getSubmittedFileName(), part.getContentType(), part.getSize());
                    companyDocumentDao.saveWithConnection(document, connection);
                }

                connection.commit();
                return Result.success();
            } catch (SQLException | IOException | DataAccessException e) {
                rollback(connection);
                rollbackSavedFiles(savedStorageIDs);
                logger.error("Failed during company registration transaction.", e);
                throw new DataAccessException("Failed during company registration transaction.", e);
            }
        } catch (SQLException e) {
            logger.error("Could not obtain database connection.", e);
            throw new DataAccessException("Could not obtain database connection.", e);
        }
    }

    private void rollbackSavedFiles(List<String> savedStorageIDs) {
        for (String storageId : savedStorageIDs) {
            fileStorageService.deleteFile(storageId);
        }
    }


    @Override
    public Result<UserSessionDto> login(UserLoginDto dto) {
        String email = dto.getEmail();
        String password = dto.getPassword();

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

    private Result<Void> validCompanyDocuments(List<Part> companyDocuments) {
        int maxFiles = 4;
        if (companyDocuments.size() > maxFiles) {
            return Result.error(String.format("Разрешено загружать не более %d файлов.", maxFiles));
        }

        long maxFileSize = 10 * 1024 * 1024;
        for (Part filePart : companyDocuments) {
            if (filePart == null || filePart.getSize() == 0) return Result.error("Обнаружен пустой файл.");
            if (filePart.getSize() > maxFileSize) return Result.error(String.format("Файл '%s' превышает размер 10МБ", filePart.getSubmittedFileName()));

            String contentType = filePart.getContentType();
            if (!contentType.equals("application/pdf") && !contentType.startsWith("image/")) {
                return Result.error(String.format("Недопустимый формат файла '%s'. Разрешены PDF или изображения.", filePart.getSubmittedFileName()));
            }
        }

        return Result.success();
    }
}