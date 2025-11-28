package ua.service;

import ua.model.Order;
import ua.util.GenericRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.logging.Logger;

public class OrderService {

    private static final Logger logger = Logger.getLogger(OrderService.class.getName());

    private final GenericRepository<Order> repository;
    private final ExecutorService executor;

    public OrderService(GenericRepository<Order> repository, ExecutorService executor) {
        this.repository = repository;
        this.executor = executor;
    }

    /**
     * Загальна сума замовлень (послідовно)
     */
    public double totalRevenueSequential() {
        long start = System.nanoTime();
        double sum = repository.getAll()
                .stream()
                .mapToDouble(Order::getTotalAmount)
                .sum();
        long time = System.nanoTime() - start;
        logger.info(() -> "[OrderService] totalRevenueSequential = " +
                sum + " in " + time / 1_000_000 + " ms");
        return sum;
    }

    /**
     * Загальна сума замовлень (асинхронно)
     */
    public CompletableFuture<Double> totalRevenueAsync() {
        return CompletableFuture.supplyAsync(() -> {
            long start = System.nanoTime();
            double sum = repository.getAll()
                    .parallelStream()
                    .mapToDouble(Order::getTotalAmount)
                    .sum();
            long time = System.nanoTime() - start;
            logger.info(() -> "[OrderService] totalRevenueAsync = " +
                    sum + " in " + time / 1_000_000 + " ms");
            return sum;
        }, executor);
    }

    /**
     * Фільтрація замовлень по даті через parallelStream
     */
    public List<Order> filterByDateRangeParallel(LocalDate from, LocalDate to) {
        long start = System.nanoTime();
        List<Order> list = repository.getAll()
                .parallelStream()
                .filter(o -> !o.getOrderDate().isBefore(from)
                        && !o.getOrderDate().isAfter(to))
                .toList();
        long time = System.nanoTime() - start;
        logger.info(() -> "[OrderService] filterByDateRangeParallel " +
                from + " .. " + to + " -> " + list.size() + " in " + time / 1_000_000 + " ms");
        return list;
    }

    /**
     * Підрахунок замовлень дорожчих за суму через ExecutorService
     */
    public CompletableFuture<Long> countExpensiveOrdersAsync(double minAmount) {
        return CompletableFuture.supplyAsync(() -> {
            long start = System.nanoTime();
            long count = repository.getAll()
                    .stream()
                    .filter(o -> o.getTotalAmount() >= minAmount)
                    .count();
            long time = System.nanoTime() - start;
            logger.info(() -> "[OrderService] countExpensiveOrders >= " +
                    minAmount + " -> " + count + " in " + time / 1_000_000 + " ms");
            return count;
        }, executor);
    }
}