package ru.kpfu.itis.kropinov.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kpfu.itis.kropinov.dao.CompanyDocumentDao;
import ru.kpfu.itis.kropinov.entities.CompanyDocument;
import ru.kpfu.itis.kropinov.exceptions.DataAccessException;

import javax.sql.DataSource;
import java.sql.*;

public class CompanyDocumentDaoImpl implements CompanyDocumentDao {
    private static final Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);
    private final DataSource ds;

    public CompanyDocumentDaoImpl(DataSource ds) {this.ds = ds;}

    @Override
    public CompanyDocument save(CompanyDocument companyDocument) {
        try (Connection connection = ds.getConnection()) {
            return saveWithConnection(companyDocument, connection);
        } catch (SQLException e) {
            logger.error("Could not obtain a database connection for saving company document");
            throw new DataAccessException("Could not obtain a database connection for saving company document", e);
        }
    }

    @Override
    public CompanyDocument saveWithConnection(CompanyDocument companyDocument, Connection connection) {
        String sql = "insert into company_documents (company_id, storage_id, original_filename, mime_type, size_bytes) values (?, ?, ?, ?, ?)";

        try {
            PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stmt.setInt(1, companyDocument.getCompanyId());
            stmt.setString(2, companyDocument.getStorageId());
            stmt.setString(3, companyDocument.getOriginalFileName());
            stmt.setString(4, companyDocument.getMimeType());
            stmt.setLong(5, companyDocument.getSizeBytes());

            int result = stmt.executeUpdate();
            if (result == 0) {
                logger.error("Company document with storageId {} was not saved, executeUpdate returned 0", companyDocument.getStorageId());
                throw new DataAccessException("Company document was not saved");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    companyDocument.setId(generatedKeys.getInt(1));
                } else {
                    logger.error("Company document was saved, but no id obtained");
                    throw new DataAccessException("Company document was saved, but no id obtained");
                }
            }

            return companyDocument;
        } catch (SQLException e) {
            logger.error("Error while saving company document with storageId {}", companyDocument.getStorageId(), e);
            throw new DataAccessException("Error while saving company document with storageId: " + companyDocument.getStorageId(), e);
        }
    }
}
