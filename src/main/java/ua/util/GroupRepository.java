package ua.util;

import ua.model.Product;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;


public class GroupRepository extends GenericRepository<Product> {
    private static final Logger logger = Logger.getLogger(GroupRepository.class.getName());

    public GroupRepository() {
        super(Product::getName); // identity = назва товару/групи товарів
    }

    // ===== ЛР5 =====
    public List<Product> sortByStock() {
        logger.info("Sorting products by stock");
        return sortBy(Product.BY_STOCK);
    }

    public List<Product> sortByDateDesc() {
        logger.info("Sorting products by createdDate (desc)");
        return sortBy(Product.BY_DATE.reversed());
    }

    public List<Product> sortByIdentityAsc() {
        logger.info("Sorting by identity (name asc)");
        return sortByIdentity("asc");
    }

    // ===== ЛР6 =====
    public List<Product> findWithMinStock(int min) {
        logger.info("Searching products with stock > " + min);
        return getAll().stream()
                .filter(p -> p.getStock() > min)
                .collect(Collectors.toList());
    }

    public List<String> getAllNames() {
        logger.info("Collecting product names");
        return getAll().stream()
                .map(Product::getName)
                .collect(Collectors.toList());
    }

    public String joinNames() {
        logger.info("Joining product names via Collectors.joining()");
        return getAll().stream()
                .map(Product::getName)
                .collect(Collectors.joining(", "));
    }

    public long countHighStockParallel(int threshold) {
        logger.info("Counting products with high stock using parallelStream");
        return getAll().parallelStream()
                .filter(p -> p.getStock() > threshold)
                .count();
    }
}