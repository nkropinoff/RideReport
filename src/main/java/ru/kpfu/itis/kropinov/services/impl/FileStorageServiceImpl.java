package ru.kpfu.itis.kropinov.services.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kpfu.itis.kropinov.dto.CloudinaryUploadResult;
import ru.kpfu.itis.kropinov.exceptions.FileStorageException;
import ru.kpfu.itis.kropinov.services.FileStorageService;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class FileStorageServiceImpl implements FileStorageService {

    private final static Logger logger = LoggerFactory.getLogger(FileStorageServiceImpl.class);
    private final Cloudinary cloudinary;

    public FileStorageServiceImpl(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @Override
    public CloudinaryUploadResult saveFile(InputStream inputStream, String originalFileName, String mimeType, String folder) {
        try {
            byte[] fileBytes = inputStream.readAllBytes();

            String resourceType = determineResourceType(mimeType);

            Map<String, String> config = ObjectUtils.asMap(
                    "folder", folder,
                    "resource_type", resourceType,
                    "use_filename", true,
                    "unique_filename", true
            );

            Map uploadResult = cloudinary.uploader().upload(fileBytes, config);

            String publicId = (String) uploadResult.get("public_id");
            String secureUrl = (String) uploadResult.get("secure_url");

            return new CloudinaryUploadResult(secureUrl, publicId, mimeType);
        } catch (IOException e) {
            logger.error("Failed upload file: {}", originalFileName, e);
            throw new FileStorageException("Failed upload file: " + originalFileName, e);
        }
    }

    @Override
    public boolean deleteFile(String publicId, String mimeType) {
        try {
            Map result = cloudinary.uploader().destroy(publicId, ObjectUtils.asMap(
                    "resource_type", determineResourceType(mimeType),
                    "invalidate", true
            ));

            String resultStatus = (String) result.get("result");
            boolean deleted = "ok".equals(resultStatus);

            if (!deleted) {
                logger.warn("File with publicId was not deleted: {}, status: {}", publicId, resultStatus);
            }

            return deleted;
        } catch (IOException e) {
            logger.error("Failed delete file with publicId: {}", publicId, e);
            throw new FileStorageException("Failed delete file with publicId: " + publicId, e);
        }
    }

    private String determineResourceType(String mimeType) {
        if (mimeType != null && mimeType.startsWith("image/")) {
            return "image";
        }
        return "raw";
    }
}
