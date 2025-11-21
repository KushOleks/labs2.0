package ua;

import ua.model.Product;
import ua.util.FileDataReader;
import ua.exceptions.InvalidDataException;

import java.io.IOException;
import java.util.List;
import java.util.logging.*;

public class MainExceptions {

    private static final Logger logger = Logger.getLogger(MainExceptions.class.getName());

    static {
        try {
            LogManager.getLogManager().reset();
            logger.setLevel(Level.ALL);

            FileHandler fileHandler = new FileHandler("app.log", true);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);

            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setLevel(Level.INFO);
            logger.addHandler(consoleHandler);

        } catch (IOException e) {
            System.err.println("Logger setup failed: " + e.getMessage());
        }
    }

    public static void main(String[] args) {

        String path = "data/products.csv"; // шлях до CSV з даними

        try {
            logger.info("Program started.");
            List<Product> products = FileDataReader.readProductsFromFile(path);
            logger.info("Products successfully loaded: " + products.size());

            System.out.println("Loaded products:");
            products.forEach(System.out::println);

        } catch (InvalidDataException e) {
            logger.warning("Invalid data exception: " + e.getMessage());
            System.err.println("❌ Invalid data: " + e.getMessage());

        } catch (IOException e) {
            logger.severe("IO exception occurred: " + e.getMessage());
            System.err.println("❌ File error: " + e.getMessage());

        } catch (Exception e) {
            logger.severe("Unexpected error: " + e.getMessage());
            System.err.println("❌ Unexpected error: " + e.getMessage());

        } finally {
            logger.info("Program finished (finally block).");
        }
    }
}