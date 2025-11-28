package ua.util;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Predicate;
import java.util.logging.Logger;

public class GenericRepository<T> {

    private static final Logger logger = Logger.getLogger(GenericRepository.class.getName());

    // потокобезпечне сховище
    private final CopyOnWriteArrayList<T> storage = new CopyOnWriteArrayList<>();

    // як дістати ідентифікатор (name, id тощо)
    private final IdentityExtractor<T> identityExtractor;

    public GenericRepository(IdentityExtractor<T> identityExtractor) {
        this.identityExtractor = identityExtractor;
    }

    /* ---------- базові операції ---------- */

    public void add(T item) {
        logger.info(() -> thread() + "Adding: " + item);
        storage.add(item);
    }

    // додано: масове додавання (синхронно)
    public void addAll(Collection<T> items) {
        if (items == null || items.isEmpty()) return;
        logger.info(() -> thread() + "addAll: " + items.size() + " items");
        storage.addAll(items);
    }

    public void remove(T item) {
        logger.info(() -> thread() + "Removing: " + item);
        storage.remove(item);
    }

    // додано: видалення за identity
    public boolean removeByIdentity(String identity) {
        T found = findByIdentity(identity);
        if (found == null) return false;
        logger.info(() -> thread() + "Removing by identity: " + identity);
        return storage.remove(found);
    }

    public List<T> getAll() {
        return List.copyOf(storage);
    }

    public T findByIdentity(String identity) {
        return storage.parallelStream()
                .filter(i -> Objects.equals(identityExtractor.extractIdentity(i), identity))
                .findFirst()
                .orElse(null);
    }

    // додано: заміна елемента за identity (для PUT)
    public boolean replaceByIdentity(String identity, T newValue) {
        for (int i = 0; i < storage.size(); i++) {
            T cur = storage.get(i);
            if (Objects.equals(identityExtractor.extractIdentity(cur), identity)) {
                logger.info(() -> thread() + "Replacing " + identity + " -> " + newValue);
                storage.set(i, newValue);
                return true;
            }
        }
        return false;
    }

    // додано: upsert — якщо є такий identity, замінюємо; якщо ні — додаємо
    public void upsertByIdentity(String identity, T value) {
        if (!replaceByIdentity(identity, value)) {
            add(value);
        }
    }

    /* ---------- запити/агрегації ---------- */

    public long count(Predicate<T> predicate) {
        return storage.parallelStream().filter(predicate).count();
    }

    public List<T> filter(Predicate<T> predicate) {
        return storage.parallelStream().filter(predicate).toList();
    }

    @SuppressWarnings({"rawtypes","unchecked"})
    public List<T> sortNatural() {
        try {
            List<T> sorted = new ArrayList<>(storage);
            Collections.sort((List) sorted);
            logger.info(() -> thread() + "Sorted using natural Comparable");
            return sorted;
        } catch (Exception e) {
            logger.warning(() -> thread() + "Sorting failed: " + e.getMessage());
            return List.of();
        }
    }

    public List<T> sortBy(Comparator<T> comparator) {
        List<T> sorted = new ArrayList<>(storage);
        sorted.sort(comparator);
        logger.info(() -> thread() + "Sorting using comparator");
        return sorted;
    }

    public List<T> sortByIdentity(String order) {
        List<T> sorted = new ArrayList<>(storage);
        Comparator<T> c = Comparator.comparing(identityExtractor::extractIdentity,
                Comparator.nullsFirst(Comparator.naturalOrder()));
        if ("desc".equalsIgnoreCase(order)) c = c.reversed();
        sorted.sort(c);
        logger.info(() -> thread() + "Sorting by identity (" + order + ")");
        return sorted;
    }

    /* ---------- асинхронні bulk-операції ---------- */

    public CompletableFuture<Void> addAllAsync(List<T> items) {
        return CompletableFuture.runAsync(() -> {
            logger.info(() -> thread() + "Parallel addAll: " + (items == null ? 0 : items.size()) + " items");
            if (items != null && !items.isEmpty()) storage.addAll(items);
        });
    }

    public void clear() { storage.clear(); }

    public int size() { return storage.size(); }

    private String thread() {
        return "[THREAD: " + Thread.currentThread().getName() + "] ";
    }
}