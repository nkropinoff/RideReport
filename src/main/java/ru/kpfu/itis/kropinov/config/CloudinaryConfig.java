package ru.kpfu.itis.kropinov.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import java.util.Map;
import java.util.Properties;

public class CloudinaryConfig {
    public static Cloudinary createCloudinary(Properties properties) {
        String cloudName = properties.getProperty("cloudinary.cloud.name");
        String apiKey = properties.getProperty("cloudinary.api.key");
        String apiSecret = properties.getProperty("cloudinary.api.secret");

        if (cloudName == null || apiKey == null || apiSecret == null) {
            throw new IllegalStateException(
                    "Cloudinary configuration missing. Required properties: cloudinary.cloud.name, cloudinary.api.key, cloudinary.api.secret"
            );
        }

        Map<String, String> config = ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret,
                "secure", "true"
        );

        return new Cloudinary(config);
    }
}
