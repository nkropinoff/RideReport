package ru.kpfu.itis.kropinov.services;

import ru.kpfu.itis.kropinov.dto.*;
import ru.kpfu.itis.kropinov.entities.Company;
import ru.kpfu.itis.kropinov.entities.Vehicle;

import java.util.List;

public interface CompanyService {
    PaginatedResult<Company> getCompanies(CompanySortingDto dto);
    Result<Void> denyCompany(int companyId);
    Result<Void> approveCompany(int companyId);
    Result<Void> deleteCompany(int companyId);
    Result<CompanyDetailsDto> getCompanyDetails(int companyId);
    Result<FileDownloadDto> getFileForDownload(int documentId);
    List<Vehicle> getCompanyVehicles(int companyId);
}
