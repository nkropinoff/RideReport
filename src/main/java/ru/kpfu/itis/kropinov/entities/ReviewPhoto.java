package ru.kpfu.itis.kropinov.entities;

public class ReviewPhoto {
    private Integer id;
    private Integer reviewId;
    private String url;
    private String publicId;
    private String originalFilename;
    private String mimeType;
    private Long sizeBytes;

    public ReviewPhoto(Integer id, Integer reviewId, String url, String publicId, String originalFilename, String mimeType, Long sizeBytes) {
        this.id = id;
        this.reviewId = reviewId;
        this.url = url;
        this.publicId = publicId;
        this.originalFilename = originalFilename;
        this.mimeType = mimeType;
        this.sizeBytes = sizeBytes;
    }

    public ReviewPhoto(Integer reviewId, String url, String publicId, String originalFilename, String mimeType, Long sizeBytes) {
        this.reviewId = reviewId;
        this.url = url;
        this.publicId = publicId;
        this.originalFilename = originalFilename;
        this.mimeType = mimeType;
        this.sizeBytes = sizeBytes;
    }

    public Integer getId() {
        return id;
    }

    public Integer getReviewId() {
        return reviewId;
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

    public void setId(Integer id) {
        this.id = id;
    }
}
