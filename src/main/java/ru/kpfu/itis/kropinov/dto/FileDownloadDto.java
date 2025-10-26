package ru.kpfu.itis.kropinov.dto;

public class FileDownloadDto {
    private final String url;
    private final String filename;
    private final String mimeType;

    public FileDownloadDto(String url, String filename, String mimeType) {
        this.url = url;
        this.filename = filename;
        this.mimeType = mimeType;
    }

    public String getUrl() {
        return url;
    }

    public String getFilename() {
        return filename;
    }

    public String getMimeType() {
        return mimeType;
    }
}
