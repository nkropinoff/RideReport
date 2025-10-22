package ru.kpfu.itis.kropinov.services.impl;

import ru.kpfu.itis.kropinov.dao.CompanyDao;
import ru.kpfu.itis.kropinov.dto.PaginatedResult;
import ru.kpfu.itis.kropinov.entities.Company;
import ru.kpfu.itis.kropinov.enums.VerifyStatus;
import ru.kpfu.itis.kropinov.services.CompanyService;

import java.util.List;

public class CompanyServiceImpl implements CompanyService {

    private final CompanyDao companyDao;

    public CompanyServiceImpl(CompanyDao companyDao) {
        this.companyDao = companyDao;
    }

    @Override
    public PaginatedResult<Company> getCompanies(int page, int size, String sortOrder, VerifyStatus status) {
        List<Company> companies = companyDao.findAll(page, size, sortOrder, status);
        int totalCount = companyDao.countAll(status);
        int totalPages = (int) Math.ceil( (double) totalCount / size);
        return new PaginatedResult<>(companies, totalPages, page);
    }
}
