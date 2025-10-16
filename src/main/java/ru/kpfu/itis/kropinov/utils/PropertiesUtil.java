package ru.kpfu.itis.kropinov.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class PropertiesUtil {

    private PropertiesUtil() {};

    public static Properties getProperties(String filename) {
        Properties properties = new Properties();
        try (InputStream is = PropertiesUtil.class.getClassLoader().getResourceAsStream(filename)) {
            if (is == null) {
                throw new IllegalArgumentException("Properties file not found in classpath: " + filename);
            }
            properties.load(is);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load properties file: " + filename, e);
        }
        return properties;
    }
}
