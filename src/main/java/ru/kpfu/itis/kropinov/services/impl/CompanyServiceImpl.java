package ru.kpfu.itis.kropinov.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kpfu.itis.kropinov.dao.CompanyDao;
import ru.kpfu.itis.kropinov.dao.CompanyDocumentDao;
import ru.kpfu.itis.kropinov.dao.UserDao;
import ru.kpfu.itis.kropinov.dto.CompanySortingDto;
import ru.kpfu.itis.kropinov.dto.PaginatedResult;
import ru.kpfu.itis.kropinov.dto.Result;
import ru.kpfu.itis.kropinov.entities.Company;
import ru.kpfu.itis.kropinov.entities.CompanyDocument;
import ru.kpfu.itis.kropinov.enums.VerifyStatus;
import ru.kpfu.itis.kropinov.exceptions.DataAccessException;
import ru.kpfu.itis.kropinov.services.CompanyService;
import ru.kpfu.itis.kropinov.services.FileStorageService;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CompanyServiceImpl implements CompanyService {

    private static final Logger logger = LoggerFactory.getLogger(CompanyServiceImpl.class);
    private final CompanyDao companyDao;
    private final CompanyDocumentDao companyDocumentDao;
    private final UserDao userDao;
    private final DataSource dataSource;
    private final FileStorageService fileStorageService;

    public CompanyServiceImpl(DataSource dataSource, CompanyDao companyDao, CompanyDocumentDao companyDocumentDao, UserDao userDao, FileStorageService fileStorageService) {
        this.dataSource = dataSource;
        this.companyDao = companyDao;
        this.companyDocumentDao = companyDocumentDao;
        this.userDao = userDao;
        this.fileStorageService = fileStorageService;
    }

    @Override
    public PaginatedResult<Company> getCompanies(CompanySortingDto dto) {
        try (Connection connection = dataSource.getConnection()) {
            List<Company> companies = companyDao.findAllWithConnection(dto, connection);
            int totalCount = companyDao.countAllWithConnection(dto.getStatus(), connection);
            int totalPages = (int) Math.ceil( (double) totalCount / dto.getSize());
            return new PaginatedResult<>(companies, totalPages, dto.getPage());
        } catch (SQLException e) {
            logger.error("Failed to fetch companies", e);
            throw new DataAccessException("Failed to fetch companies", e);
        }
    }

    @Override
    public Result<Void> denyCompany(int companyId) {
        companyDao.setCompanyStatus(companyId, VerifyStatus.DENIED);
        return Result.success();
    }

    @Override
    public Result<Void> approveCompany(int companyId) {
        companyDao.setCompanyStatus(companyId, VerifyStatus.APPROVED);
        return Result.success();
    }

    @Override
    public Result<Void> deleteCompany(int companyId) {
        try (Connection connection = dataSource.getConnection()) {
            try {
                connection.setAutoCommit(false);

                Optional<Company> companyOptional = companyDao.findByIdWithConnection(companyId, connection);
                if (companyOptional.isEmpty()) {
                    logger.warn("Company with id: {} was not found", companyId);
                    return Result.error("Company was not found");
                }

                Company company = companyOptional.get();

                List<CompanyDocument> companyDocuments = companyDocumentDao.findByCompanyIdWithConnection(companyId, connection);
                List<String> deletedStorageIds = new ArrayList<>();

                for (CompanyDocument doc : companyDocuments) {
                    try {
                        fileStorageService.deleteFile(doc.getStorageId());
                        deletedStorageIds.add(doc.getStorageId());
                    } catch (Exception e) {
                        logger.error("Failed to delete file: {}", doc.getStorageId());
                        rollback(connection);
                        rollbackDeletedFiles(deletedStorageIds);
                        return Result.error("Failed delete company documents");
                    }
                }

                userDao.deleteByIdWithConnection(company.getUserId(), connection);

                connection.commit();
                return Result.success();
            } catch (SQLException | DataAccessException e) {
                rollback(connection);
                logger.error("Failed to delete company {}", companyId, e);
                throw new DataAccessException("Failed to delete company", e);
            }
        } catch (SQLException e) {
            logger.error("Could not obtain database connection", e);
            throw new DataAccessException("Could not obtain database connection", e);
        }
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

    private void rollbackDeletedFiles(List<String> deletedStorageIds) {
        logger.error("Transaction rolled back. Files deleted but need restoration: {}", deletedStorageIds);
    }
}
