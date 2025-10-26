package ru.kpfu.itis.kropinov.services;

import ru.kpfu.itis.kropinov.dto.*;
import ru.kpfu.itis.kropinov.entities.Company;

public interface CompanyService {
    PaginatedResult<Company> getCompanies(CompanySortingDto dto);
    Result<Void> denyCompany(int companyId);
    Result<Void> approveCompany(int companyId);
    Result<Void> deleteCompany(int companyId);
    Result<CompanyDetailsDto> getCompanyDetails(int companyId);
    Result<FileDownloadDto> getFileForDownload(int documentId);
}
