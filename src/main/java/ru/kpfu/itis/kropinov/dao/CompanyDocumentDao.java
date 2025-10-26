package ru.kpfu.itis.kropinov.dao;

import ru.kpfu.itis.kropinov.entities.Company;
import ru.kpfu.itis.kropinov.entities.CompanyDocument;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public interface CompanyDocumentDao {
    CompanyDocument save(CompanyDocument companyDocument);
    CompanyDocument saveWithConnection(CompanyDocument companyDocument, Connection connection);
    Optional<CompanyDocument> findByIdWithConnection(int id, Connection connection);
    List<CompanyDocument> findByCompanyIdWithConnection(int companyId, Connection connection);
}
