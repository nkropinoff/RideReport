package ru.kpfu.itis.kropinov.dao;

import ru.kpfu.itis.kropinov.entities.Company;
import ru.kpfu.itis.kropinov.enums.VerifyStatus;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public interface CompanyDao {
    Company save(Company company);
    Company saveWithConnection(Company company, Connection connection);
    Optional<Company> findByUserId(int userId);
    List<Company> findAll(int page, int size, String sortOrder, VerifyStatus status);
    int countAll(VerifyStatus status);
}
