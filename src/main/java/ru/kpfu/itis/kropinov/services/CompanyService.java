package ru.kpfu.itis.kropinov.services;

import ru.kpfu.itis.kropinov.dto.PaginatedResult;
import ru.kpfu.itis.kropinov.entities.Company;
import ru.kpfu.itis.kropinov.enums.VerifyStatus;

public interface CompanyService {
    PaginatedResult<Company> getCompanies(int page, int size, String sortOrder, VerifyStatus status);
}
