package ru.kpfu.itis.kropinov.entities;

public class CompanyDocument {
    private Integer id;
    private Integer companyId;
    private String storageId;
    private String originalFileName;
    private String mimeType;
    private Long sizeBytes;


    public CompanyDocument(Integer id, Integer companyId, String storageId, String originalFileName, String mimeType, Long sizeBytes) {
        this.id = id;
        this.companyId = companyId;
        this.storageId = storageId;
        this.originalFileName = originalFileName;
        this.mimeType = mimeType;
        this.sizeBytes = sizeBytes;
    }

    public CompanyDocument(Integer companyId, String storageId, String originalFileName, String mimeType, Long sizeBytes) {
        this.companyId = companyId;
        this.storageId = storageId;
        this.originalFileName = originalFileName;
        this.mimeType = mimeType;
        this.sizeBytes = sizeBytes;
    }

    public Integer getId() {
        return id;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public String getStorageId() {
        return storageId;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public String getMimeType() {
        return mimeType;
    }

    public Long getSizeBytes() {
        return sizeBytes;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
