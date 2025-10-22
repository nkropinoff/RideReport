package ru.kpfu.itis.kropinov.services.impl;

import ru.kpfu.itis.kropinov.dao.CompanyDao;
import ru.kpfu.itis.kropinov.dao.impl.CompanyDocumentDaoImpl;
import ru.kpfu.itis.kropinov.dto.CompanySortingDto;
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
    public PaginatedResult<Company> getCompanies(CompanySortingDto dto) {
        List<Company> companies = companyDao.findAll(dto.getPage(), dto.getSize(), dto.getSortOrder(), dto.getStatus());
        int totalCount = companyDao.countAll(dto.getStatus());
        int totalPages = (int) Math.ceil( (double) totalCount / dto.getSize());
        return new PaginatedResult<>(companies, totalPages, dto.getPage());
    }
}
