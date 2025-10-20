package ru.kpfu.itis.kropinov.dao;

import ru.kpfu.itis.kropinov.entities.Company;

import java.sql.Connection;
import java.util.Optional;

public interface CompanyDao {
    Company save(Company company);
    Company saveWithConnection(Company company, Connection connection);
    Optional<Company> findByUserId(int userId);
}
