package ua.util;

import ua.model.Product;
import ua.exceptions.InvalidDataException;

import java.io.*;
import java.time.LocalDate;
import java.util.*;
import java.util.logging.*;

public class FileDataReader {

    private static final Logger logger = Logger.getLogger(FileDataReader.class.getName());

    static {
        try {
            LogManager.getLogManager().reset(); // скидаємо стандартні налаштування
            logger.setLevel(Level.ALL);

            FileHandler fileHandler = new FileHandler("app.log", true);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);

            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setLevel(Level.INFO);
            logger.addHandler(consoleHandler);

        } catch (IOException e) {
            System.err.println("Logger initialization failed: " + e.getMessage());
        }
    }

    public static List<Product> readProductsFromFile(String filePath)
            throws IOException, InvalidDataException {

        List<Product> products = new ArrayList<>();
        logger.info("Starting to read products from file: " + filePath);

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {

            String line;
            int lineNumber = 0;

            while ((line = br.readLine()) != null) {
                lineNumber++;
                logger.fine("Reading line " + lineNumber + ": " + line);

                if (line.isBlank()) continue;
                String[] parts = line.split(",");

                if (parts.length < 4) {
                    logger.warning("Incomplete data at line " + lineNumber);
                    throw new InvalidDataException("Incomplete data in line " + lineNumber);
                }

                try {
                    String name = parts[0].trim();
                    double price = Double.parseDouble(parts[1].trim());
                    int stock = Integer.parseInt(parts[2].trim());
                    LocalDate date = LocalDate.parse(parts[3].trim());

                    if (price < 0 || stock < 0) {
                        logger.warning("Negative values found at line " + lineNumber);
                        throw new InvalidDataException("Negative values at line " + lineNumber);
                    }

                    Product product = new Product(name, price, stock, date);
                    products.add(product);
                    logger.info("Product added successfully: " + product);

                } catch (NumberFormatException e) {
                    logger.warning("Invalid number format at line " + lineNumber + ": " + e.getMessage());
                    throw new InvalidDataException("Invalid numeric value in line " + lineNumber);
                }
            }

        } catch (FileNotFoundException e) {
            logger.severe("File not found: " + filePath);
            throw e;

        } catch (IOException e) {
            logger.severe("Error reading file: " + e.getMessage());
            throw e;

        } finally {
            logger.info("Finished reading file (finally block).");
        }

        logger.info("Successfully read " + products.size() + " products.");
        return products;
    }
}