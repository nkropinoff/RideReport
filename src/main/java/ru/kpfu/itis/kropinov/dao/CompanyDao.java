package ru.kpfu.itis.kropinov.dao;

import ru.kpfu.itis.kropinov.entities.Company;

import java.sql.Connection;

public interface CompanyDao {
    Company save(Company company);
    Company saveWithConnection(Company company, Connection connection);
}
