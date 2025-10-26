package ru.kpfu.itis.kropinov.entities;

public class CompanyDocument {
    private Integer id;
    private Integer companyId;
    private String url;
    private String publicId;
    private String originalFilename;
    private String mimeType;
    private Long sizeBytes;

    public CompanyDocument(Integer id, Integer companyId, String url, String publicId, String originalFilename, String mimeType, Long sizeBytes) {
        this.id = id;
        this.companyId = companyId;
        this.url = url;
        this.publicId = publicId;
        this.originalFilename = originalFilename;
        this.mimeType = mimeType;
        this.sizeBytes = sizeBytes;
    }

    public CompanyDocument(Integer companyId, String url, String publicId, String originalFilename, String mimeType, Long sizeBytes) {
        this.companyId = companyId;
        this.publicId = publicId;
        this.url = url;
        this.originalFilename = originalFilename;
        this.mimeType = mimeType;
        this.sizeBytes = sizeBytes;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public String getUrl() {
        return url;
    }

    public String getPublicId() {
        return publicId;
    }

    public String getOriginalFilename() {
        return originalFilename;
    }

    public String getMimeType() {
        return mimeType;
    }

    public Long getSizeBytes() {
        return sizeBytes;
    }
}