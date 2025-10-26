package ru.kpfu.itis.kropinov.services;

import ru.kpfu.itis.kropinov.dto.CloudinaryUploadResult;

import java.io.InputStream;

public interface FileStorageService {
    CloudinaryUploadResult saveFile(InputStream inputStream, String originalFileName, String mimeType, String folder);
    boolean deleteFile(String publicId, String mimeType);
}
