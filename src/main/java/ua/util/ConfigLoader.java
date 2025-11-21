package ua.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

public class ConfigLoader {
    private static final Logger logger = Logger.getLogger(ConfigLoader.class.getName());
    private static final Properties props = new Properties();

    public static void load(String path) {
        try (FileInputStream fis = new FileInputStream(path)) {
            props.load(fis);
            logger.info("✅ Config loaded from " + path);
        } catch (IOException e) {
            logger.severe("❌ Failed to load config: " + e.getMessage());
            throw new RuntimeException("Error reading config file", e);
        }
    }

    public static String get(String key) {
        return props.getProperty(key);
    }
}