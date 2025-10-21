package ru.kpfu.itis.kropinov.services.impl;

import ru.kpfu.itis.kropinov.services.FileStorageService;

import java.io.InputStream;
import java.io.OutputStream;

public class FileStorageServiceImpl implements FileStorageService {
    @Override
    public String saveFile(InputStream inputStream, String originalFileName, String contentType) {
        //TODO: saveFile()
        return "";
    }

    @Override
    public void downloadFile(String storageId, OutputStream outputStream) {
        //TODO: downloadFile()
    }

    @Override
    public void deleteFile(String storageId) {
        //TODO: deleteFile()
    }
}
