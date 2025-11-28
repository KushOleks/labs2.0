package ua.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class DataSerializer {

    private static final Logger logger = Logger.getLogger(DataSerializer.class.getName());

    private static final ObjectMapper jsonMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    private static final ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory())
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    public static <T> void toJSON(List<T> data, String filePath) {
        try {
            jsonMapper.writerWithDefaultPrettyPrinter()
                    .writeValue(new File(filePath), data);
            logger.info("JSON saved: " + filePath);
        } catch (IOException e) {
            logger.severe("Failed to save JSON: " + e.getMessage());
            throw new DataSerializationException("Error saving JSON file", e);
        }
    }

    public static <T> List<T> fromJSON(String filePath, Class<T> type) {
        try {
            T[] arr = jsonMapper.readValue(new File(filePath), (Class<T[]>) java.lang.reflect.Array.newInstance(type, 0).getClass());
            return List.of(arr);
        } catch (IOException e) {
            logger.severe("Failed to read JSON: " + e.getMessage());
            throw new DataSerializationException("Error reading JSON file", e);
        }
    }

    public static <T> void toYAML(List<T> data, String filePath) {
        try {
            yamlMapper.writerWithDefaultPrettyPrinter()
                    .writeValue(new File(filePath), data);
        } catch (IOException e) {
            throw new DataSerializationException("Error saving YAML file", e);
        }
    }

    public static <T> List<T> fromYAML(String filePath, Class<T> type) {
        try {
            T[] arr = yamlMapper.readValue(new File(filePath), (Class<T[]>) java.lang.reflect.Array.newInstance(type, 0).getClass());
            return List.of(arr);
        } catch (IOException e) {
            throw new DataSerializationException("Error reading YAML file", e);
        }
    }
    public static <T> List<T> readFromJson(String filePath, Class<T> type) {
        return fromJSON(filePath, type);
    }
}