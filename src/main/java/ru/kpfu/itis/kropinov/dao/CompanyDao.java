package ru.kpfu.itis.kropinov.dao;

import ru.kpfu.itis.kropinov.dto.CompanySortingDto;
import ru.kpfu.itis.kropinov.entities.Company;
import ru.kpfu.itis.kropinov.enums.VerifyStatus;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public interface CompanyDao {
    Company save(Company company);
    Company saveWithConnection(Company company, Connection connection);
    Optional<Company> findByUserId(int userId);
    Optional<Company> findByIdWithConnection(int companyId, Connection connection);
    List<Company> findAll(CompanySortingDto dto);
    List<Company> findAllWithConnection(CompanySortingDto dto, Connection connection);
    int countAll(VerifyStatus status);
    int countAllWithConnection(VerifyStatus status, Connection connection);
    void setCompanyStatus(int companyId, VerifyStatus status);

}
