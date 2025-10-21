package ru.kpfu.itis.kropinov.services;

import ru.kpfu.itis.kropinov.dto.Result;

import java.io.InputStream;
import java.io.OutputStream;

public interface FileStorageService {
    String saveFile(InputStream inputStream, String originalFileName, String contentType);
    void downloadFile(String storageId, OutputStream outputStream);
    void deleteFile(String storageId);
}
