package ru.kpfu.itis.kropinov.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kpfu.itis.kropinov.dao.CompanyDao;
import ru.kpfu.itis.kropinov.dao.CompanyDocumentDao;
import ru.kpfu.itis.kropinov.dao.UserDao;
import ru.kpfu.itis.kropinov.dao.VehicleDao;
import ru.kpfu.itis.kropinov.db.CustomDataSource;
import ru.kpfu.itis.kropinov.dto.*;
import ru.kpfu.itis.kropinov.entities.Company;
import ru.kpfu.itis.kropinov.entities.CompanyDocument;
import ru.kpfu.itis.kropinov.entities.Vehicle;
import ru.kpfu.itis.kropinov.enums.VerifyStatus;
import ru.kpfu.itis.kropinov.exceptions.DataAccessException;
import ru.kpfu.itis.kropinov.services.CompanyService;
import ru.kpfu.itis.kropinov.services.FileStorageService;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;

public class CompanyServiceImpl implements CompanyService {

    private static final Logger logger = LoggerFactory.getLogger(CompanyServiceImpl.class);
    private final CompanyDao companyDao;
    private final CompanyDocumentDao companyDocumentDao;
    private final UserDao userDao;
    private final VehicleDao vehicleDao;
    private final DataSource dataSource;
    private final FileStorageService fileStorageService;

    private static final String DOWNLOAD_URL_PATTERN = "/admin/companies/download/%d";

    public CompanyServiceImpl(CompanyDao companyDao, CompanyDocumentDao companyDocumentDao, UserDao userDao, VehicleDao vehicleDao, DataSource dataSource, FileStorageService fileStorageService) {
        this.companyDao = companyDao;
        this.companyDocumentDao = companyDocumentDao;
        this.userDao = userDao;
        this.vehicleDao = vehicleDao;
        this.dataSource = dataSource;
        this.fileStorageService = fileStorageService;
    }

    @Override
    public PaginatedResult<Company> getCompanies(CompanySortingDto dto) {
        try (Connection connection = dataSource.getConnection()) {
            List<Company> companies = companyDao.findAllWithConnection(dto, connection);
            int totalCount = companyDao.countAllWithConnection(dto.getStatus(), connection);
            int totalPages = (int) Math.ceil( (double) totalCount / dto.getSize());
            return new PaginatedResult<>(companies, totalPages, dto.getPage());
        } catch (SQLException e) {
            logger.error("Failed to fetch companies", e);
            throw new DataAccessException("Failed to fetch companies", e);
        }
    }

    @Override
    public Result<Void> denyCompany(int companyId) {
        try {
            companyDao.setCompanyStatus(companyId, VerifyStatus.DENIED);
            return Result.success();
        } catch (DataAccessException e) {
            logger.error("Failed to deny company {}", companyId, e);
            throw e;
        }

    }

    @Override
    public Result<Void> approveCompany(int companyId) {
        try {
            companyDao.setCompanyStatus(companyId, VerifyStatus.APPROVED);
            return Result.success();
        } catch (DataAccessException e) {
            logger.error("Failed to approve company {}", companyId, e);
            throw e;
        }

    }

    @Override
    public Result<Void> deleteCompany(int companyId) {
        try (Connection connection = dataSource.getConnection()) {
            try {
                connection.setAutoCommit(false);

                Optional<Company> companyOptional = companyDao.findByIdWithConnection(companyId, connection);
                if (companyOptional.isEmpty()) {
                    logger.warn("Company with id: {} was not found", companyId);
                    return Result.error("Company was not found");
                }

                Company company = companyOptional.get();

                List<CompanyDocument> companyDocumentsToDelete = companyDocumentDao.findByCompanyIdWithConnection(companyId, connection);
                userDao.deleteByIdWithConnection(company.getUserId(), connection);
                connection.commit();

                for (CompanyDocument doc : companyDocumentsToDelete) {
                    try {
                        fileStorageService.deleteFile(doc.getPublicId(), doc.getMimeType());
                    } catch (Exception e) {
                        logger.error("Failed to delete file: {}", doc.getPublicId(), e);
                    }
                }

                return Result.success();
            } catch (SQLException | DataAccessException e) {
                CustomDataSource.rollback(connection);
                logger.error("Failed to delete company {}", companyId, e);
                throw new DataAccessException("Failed to delete company", e);
            }
        } catch (SQLException e) {
            logger.error("Could not obtain database connection", e);
            throw new DataAccessException("Could not obtain database connection", e);
        }
    }

    @Override
    public Result<CompanyDetailsDto> getCompanyDetails(int companyId) {
        try (Connection connection = dataSource.getConnection()) {
            Optional<CompanyWithUserDto> companyWithUserDtoOptional = companyDao.findByIdWithUserWithConnection(companyId, connection);
            if (companyWithUserDtoOptional.isEmpty()) {
                logger.warn("Company with id: {} was not found", companyId);
                return Result.error("Company was not found");
            }

            CompanyWithUserDto companyWithUserDto = companyWithUserDtoOptional.get();
            List<CompanyDocument> companyDocuments = companyDocumentDao.findByCompanyIdWithConnection(companyId, connection);

            List<CompanyDetailsDto.CompanyDocumentDto> companyDocumentDtos = companyDocuments.stream()
                    .map(doc -> new CompanyDetailsDto.CompanyDocumentDto(
                            doc.getOriginalFilename(),
                            doc.getMimeType(),
                            doc.getSizeBytes(),
                            formatSizeBytes(doc.getSizeBytes()),
                            buildDownloadUrl(doc.getId())))
                    .toList();

            return Result.success(new CompanyDetailsDto(
                    companyWithUserDto.getUserEmail(),
                    companyWithUserDto.getCompanyName(),
                    companyWithUserDto.getInn(),
                    companyWithUserDto.getStatus(),
                    companyDocumentDtos
            ));

        } catch (SQLException e) {
            logger.error("Could not obtain database connection", e);
            throw new DataAccessException("Could not obtain database connection", e);
        }
    }

    private String buildDownloadUrl(int documentId) {
        return String.format(DOWNLOAD_URL_PATTERN, documentId);
    }

    private String formatSizeBytes(Long sizeBytes) {
        if (sizeBytes == null || sizeBytes == 0) {
            return "0 Б";
        }

        final String[] units = {"Б", "КБ", "МБ", "ГБ"};
        int unitIndex = 0;
        double size = sizeBytes.doubleValue();

        while (size >= 1024 && unitIndex < units.length - 1) {
            size /= 1024;
            unitIndex++;
        }

        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(size) + " " + units[unitIndex];
    }

    @Override
    public Result<FileDownloadDto> getFileForDownload(int documentId) {
        try (Connection conn = dataSource.getConnection()) {
            Optional<CompanyDocument> documentOptional = companyDocumentDao.findByIdWithConnection(documentId, conn);

            if (documentOptional.isEmpty()) {
                return Result.error("File not found");
            }

            CompanyDocument doc = documentOptional.get();

            return Result.success(new FileDownloadDto(
                    doc.getUrl(),
                    doc.getOriginalFilename(),
                    doc.getMimeType()
            ));
        } catch (SQLException e) {
            logger.error("Failed to get file for download", e);
            return Result.error("Failed to get file");
        }
    }

    @Override
    public List<Vehicle> getCompanyVehicles(int companyId) {
        return vehicleDao.findVehiclesByCompanyId(companyId);
    }
}
