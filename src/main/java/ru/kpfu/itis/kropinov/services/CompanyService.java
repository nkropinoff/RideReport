package ru.kpfu.itis.kropinov.services;

import ru.kpfu.itis.kropinov.dto.CompanySortingDto;
import ru.kpfu.itis.kropinov.dto.PaginatedResult;
import ru.kpfu.itis.kropinov.dto.Result;
import ru.kpfu.itis.kropinov.entities.Company;

public interface CompanyService {
    PaginatedResult<Company> getCompanies(CompanySortingDto dto);
    Result<Void> denyCompany(int companyId);
    Result<Void> approveCompany(int companyId);
}
