package ua.concurrency;

import ua.util.DataSerializer;
import ua.util.GenericRepository;

import java.util.List;
import java.util.concurrent.*;
import java.util.logging.Logger;

public class ConcurrencyManager {

    private static final Logger logger = Logger.getLogger(ConcurrencyManager.class.getName());

    private final ExecutorService executor;

    public ConcurrencyManager() {
        this(Runtime.getRuntime().availableProcessors());
    }

    public ConcurrencyManager(int threads) {
        this.executor = Executors.newFixedThreadPool(threads);
        logger.info(() -> "[ConcurrencyManager] Executor with " + threads + " threads created");
    }

    public ExecutorService getExecutor() {
        return executor;
    }

    /**
     * Паралельне завантаження даних з JSON-файлу в репозиторій.
     */
    public <T> CompletableFuture<Void> loadFromJson(GenericRepository<T> repository,
                                                    String filePath,
                                                    Class<T> type) {
        return CompletableFuture.runAsync(() -> {
            String thread = threadName();
            try {
                logger.info(() -> thread + "Loading " + type.getSimpleName() + " from " + filePath);
                List<T> data = DataSerializer.fromJSON(filePath, type);
                logger.info(() -> thread + "Loaded " + data.size() + " objects, adding to repository...");
                data.forEach(repository::add);
                logger.info(() -> thread + "Repository size now: " + repository.size());
            } catch (Exception e) {
                logger.severe(() -> thread + "Error loading from " + filePath + ": " + e.getMessage());
                throw new CompletionException(e);
            }
        }, executor);
    }

    public void shutdown() {
        logger.info("[ConcurrencyManager] Shutting down executor");
        executor.shutdown();
    }

    public void shutdownNow() {
        logger.warning("[ConcurrencyManager] Forcing executor shutdown");
        executor.shutdownNow();
    }

    private String threadName() {
        return "[THREAD: " + Thread.currentThread().getName() + "] ";
    }
}