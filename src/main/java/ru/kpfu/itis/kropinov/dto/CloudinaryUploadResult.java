package ru.kpfu.itis.kropinov.dto;

public class CloudinaryUploadResult {
    private final String url;
    private final String publicId;
    private final String mimeType;

    public CloudinaryUploadResult(String url, String publicId, String mimeType) {
        this.url = url;
        this.publicId = publicId;
        this.mimeType = mimeType;
    }

    public String getUrl() {
        return url;
    }

    public String getPublicId() {
        return publicId;
    }

    public String getMimeType() {
        return mimeType;
    }
}
