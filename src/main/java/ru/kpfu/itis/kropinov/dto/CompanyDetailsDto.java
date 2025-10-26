package ru.kpfu.itis.kropinov.dto;

import ru.kpfu.itis.kropinov.enums.VerifyStatus;

import java.util.List;

public class CompanyDetailsDto {
    private final String email;
    private final String companyName;
    private final String inn;
    private final VerifyStatus status;
    private final List<CompanyDocumentDto> documents;

    public CompanyDetailsDto(String email, String companyName, String inn, VerifyStatus status, List<CompanyDocumentDto> documents) {
        this.email = email;
        this.companyName = companyName;
        this.inn = inn;
        this.status = status;
        this.documents = documents;
    }

    public static class CompanyDocumentDto {
        private final String originalFilename;
        private final String fileType;
        private final Long sizeBytes;
        private final String fileSizeFormatted;
        private final String downloadUrl;

        public CompanyDocumentDto(String originalFilename, String fileType, Long sizeBytes, String fileSizeFormatted, String downloadUrl) {
            this.originalFilename = originalFilename;
            this.fileType = fileType;
            this.sizeBytes = sizeBytes;
            this.fileSizeFormatted = fileSizeFormatted;
            this.downloadUrl = downloadUrl;
        }

        public String getOriginalFilename() {
            return originalFilename;
        }

        public String getFileType() {
            return fileType;
        }

        public Long getSizeBytes() {
            return sizeBytes;
        }

        public String getFileSizeFormatted() {
            return fileSizeFormatted;
        }

        public String getDownloadUrl() {
            return downloadUrl;
        }
    }

    public String getEmail() {
        return email;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getInn() {
        return inn;
    }

    public VerifyStatus getStatus() {
        return status;
    }

    public List<CompanyDocumentDto> getDocuments() {
        return documents;
    }
}
