package ru.kpfu.itis.kropinov.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kpfu.itis.kropinov.dao.CompanyDao;
import ru.kpfu.itis.kropinov.dto.CompanySortingDto;
import ru.kpfu.itis.kropinov.dto.PaginatedResult;
import ru.kpfu.itis.kropinov.dto.Result;
import ru.kpfu.itis.kropinov.entities.Company;
import ru.kpfu.itis.kropinov.enums.VerifyStatus;
import ru.kpfu.itis.kropinov.exceptions.DataAccessException;
import ru.kpfu.itis.kropinov.services.CompanyService;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class CompanyServiceImpl implements CompanyService {

    private static final Logger logger = LoggerFactory.getLogger(CompanyServiceImpl.class);
    private final CompanyDao companyDao;
    private final DataSource dataSource;

    public CompanyServiceImpl(DataSource dataSource, CompanyDao companyDao) {
        this.dataSource = dataSource;
        this.companyDao = companyDao;
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
}
