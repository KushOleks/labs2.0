package ua.util;

import java.util.*;
import java.util.logging.Logger;

public class GenericRepository<T> {
    private static final Logger logger = Logger.getLogger(GenericRepository.class.getName());
    private final List<T> storage = new ArrayList<>();
    private final IdentityExtractor<T> identityExtractor;

    public GenericRepository(IdentityExtractor<T> identityExtractor) {
        this.identityExtractor = identityExtractor;
    }

    public void add(T item) {
        logger.info("Adding: " + item);
        storage.add(item);
    }

    public void remove(T item) {
        logger.info("Removing: " + item);
        storage.remove(item);
    }

    public List<T> getAll() {
        return new ArrayList<>(storage);
    }

    public T findByIdentity(String identity) {
        for (T item : storage) {
            if (identityExtractor.extractIdentity(item).equals(identity)) {
                logger.info("Found: " + item);
                return item;
            }
        }
        logger.warning("Object with identity '" + identity + "' not found");
        return null;
    }



    public List<T> sortNatural() {
        if (storage.isEmpty() || !(storage.get(0) instanceof Comparable)) {
            logger.warning("Sorting failed: elements are not Comparable");
            return List.of();
        }
        List<T> sorted = new ArrayList<>(storage);
        Collections.sort((List) sorted);
        logger.info("Sorted collection using natural order (Comparable)");
        return sorted;
    }

    public List<T> sortBy(Comparator<T> comparator) {
        List<T> sorted = new ArrayList<>(storage);
        sorted.sort(comparator);
        logger.info("Sorted collection using comparator: " + comparator);
        return sorted;
    }
    public List<T> sortByIdentity(String order) {
        List<T> sorted = new ArrayList<>(storage);
        Comparator<T> comparator = Comparator.comparing(identityExtractor::extractIdentity);

        if ("desc".equalsIgnoreCase(order)) {
            comparator = comparator.reversed();
            logger.info("Sorting by identity in descending order");
        } else {
            logger.info("Sorting by identity in ascending order");
        }

        sorted.sort(comparator);
        return sorted;
    }

}