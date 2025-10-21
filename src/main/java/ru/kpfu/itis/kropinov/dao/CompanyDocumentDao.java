package ru.kpfu.itis.kropinov.dao;

import ru.kpfu.itis.kropinov.entities.Company;
import ru.kpfu.itis.kropinov.entities.CompanyDocument;

import java.sql.Connection;

public interface CompanyDocumentDao {
    CompanyDocument save(CompanyDocument companyDocument);
    CompanyDocument saveWithConnection(CompanyDocument companyDocument, Connection connection);
}
