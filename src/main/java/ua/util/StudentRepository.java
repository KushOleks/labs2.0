package ua.util;

import ua.model.Product;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;


public class StudentRepository extends GenericRepository<Product> {
    private static final Logger logger = Logger.getLogger(StudentRepository.class.getName());

    public StudentRepository() {
        super(Product::getName); // identity = назва товару
    }

    // ===== ЛР5 =====
    public List<Product> sortByName() {
        logger.info("Sorting products by name");
        return sortBy(Product.BY_NAME);
    }

    public List<Product> sortByPrice() {
        logger.info("Sorting products by price (Comparable)");
        return sortNatural(); // compareTo у Product = ціна
    }

    public List<Product> sortByDate() {
        logger.info("Sorting products by createdDate");
        return sortBy(Product.BY_DATE);
    }

    // ===== ЛР6 =====
    public List<Product> findByName(String name) {
        logger.info("Searching products by name: " + name);
        return getAll().stream()
                .filter(p -> p.getName().equalsIgnoreCase(name))
                .collect(Collectors.toList());
    }

    public List<Product> findByPriceRange(double min, double max) {
        logger.info("Searching products by price range: " + min + " - " + max);
        return getAll().stream()
                .filter(p -> p.getPrice() >= min && p.getPrice() <= max)
                .collect(Collectors.toList());
    }

    public List<String> getAllProductNames() {
        logger.info("Collecting all product names");
        return getAll().stream()
                .map(Product::getName)
                .collect(Collectors.toList());
    }

    public double averagePrice() {
        logger.info("Calculating average product price via reduce()");
        double sum = getAll().stream()
                .map(Product::getPrice)
                .reduce(0.0, Double::sum);
        return getAll().isEmpty() ? 0.0 : sum / getAll().size();
    }
}