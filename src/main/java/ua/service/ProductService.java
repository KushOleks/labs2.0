package ua.service;

import ua.model.Product;
import ua.util.GenericRepository;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.logging.Logger;

public class ProductService {

    private static final Logger logger = Logger.getLogger(ProductService.class.getName());

    private final GenericRepository<Product> repository;
    private final ExecutorService executor;

    public ProductService(GenericRepository<Product> repository, ExecutorService executor) {
        this.repository = repository;
        this.executor = executor;
    }

    /* ---------- CRUD для REST ---------- */

    public List<Product> findAll() {
        return repository.getAll();
    }

    public Product findByName(String name) {
        return repository.findByIdentity(name);
    }

    public void add(Product p) {
        repository.add(p);
    }

    public Product update(String name, Product updated) {
        boolean ok = repository.replaceByIdentity(name, updated);
        return ok ? updated : null;
    }

    public boolean delete(String name) {
        return repository.removeByIdentity(name);
    }

    /* ---------- Твої задачі з паралельних обчислень ---------- */

    /** Підрахунок дорогих товарів через parallelStream */
    public long countExpensiveParallelStream(double minPrice) {
        long start = System.nanoTime();
        long result = repository.getAll()
                .parallelStream()
                .filter(p -> p.getPrice() >= minPrice)
                .count();
        long time = System.nanoTime() - start;
        logger.info(() -> "[ProductService] parallelStream countExpensive >= " +
                minPrice + " -> " + result + " in " + time / 1_000_000 + " ms");
        return result;
    }

    /** Підрахунок дорогих товарів через ExecutorService / CompletableFuture */
    public CompletableFuture<Long> countExpensiveWithExecutor(double minPrice) {
        return CompletableFuture.supplyAsync(() -> {
            long start = System.nanoTime();
            long result = repository.getAll()
                    .stream()
                    .filter(p -> p.getPrice() >= minPrice)
                    .count();
            long time = System.nanoTime() - start;
            logger.info(() -> "[ProductService] executor countExpensive >= " +
                    minPrice + " -> " + result + " in " + time / 1_000_000 + " ms");
            return result;
        }, executor);
    }

    /** Загальна вартість складу: sum(price * stock) */
    public CompletableFuture<Double> totalStockValueAsync() {
        return CompletableFuture.supplyAsync(() -> {
            long start = System.nanoTime();
            double sum = repository.getAll()
                    .stream()
                    .mapToDouble(p -> p.getPrice() * p.getStock())
                    .sum();
            long time = System.nanoTime() - start;
            logger.info(() -> "[ProductService] totalStockValue = " +
                    sum + " in " + time / 1_000_000 + " ms");
            return sum;
        }, executor);
    }

    /** Фільтрація популярних товарів (stock >= threshold) */
    public CompletableFuture<List<Product>> filterByStockAsync(int minStock) {
        return CompletableFuture.supplyAsync(() -> {
            long start = System.nanoTime();
            List<Product> list = repository.getAll()
                    .parallelStream()
                    .filter(p -> p.getStock() >= minStock)
                    .toList();
            long time = System.nanoTime() - start;
            logger.info(() -> "[ProductService] filterByStock >= " +
                    minStock + " -> " + list.size() + " items in " + time / 1_000_000 + " ms");
            return list;
        }, executor);
    }
}