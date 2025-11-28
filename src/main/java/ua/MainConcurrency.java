package ua;

import ua.model.Product;
import ua.service.ProductService;
import ua.util.DataSerializer;
import ua.util.GenericRepository;
import ua.util.IdentityExtractor;

import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainConcurrency {
    public static void main(String[] args) throws Exception {
        // 1) Репозиторій (ідентифікатор для Product — name)
        IdentityExtractor<Product> byName = Product::getName;
        GenericRepository<Product> repo = new GenericRepository<>(byName);

        // 2) Пул потоків для сервісу
        ExecutorService executor = Executors.newFixedThreadPool(
                Math.max(2, Runtime.getRuntime().availableProcessors() / 2)
        );

        try {
            // 3) Завантаження даних (можеш взяти свій файл)
            String productsPath = Paths.get("data", "products_concurrency.json").toString();
            List<Product> products = DataSerializer.fromJSON(productsPath, Product.class);

            // 4) Додати в репо (синхронно або асинхронно)
            repo.addAll(products);
            // або асинхронно:
            // repo.addAllAsync(products).join();

            // 5) Сервіс
            ProductService ps = new ProductService(repo, executor);

            // 6) Декілька перевірок
            System.out.println("Total loaded: " + repo.size());
            long expensivePS = ps.countExpensiveParallelStream(1000.0);
            long expensiveExec = ps.countExpensiveWithExecutor(1000.0).join();
            double totalStockValue = ps.totalStockValueAsync().join();
            int minStock = 5;
            int filtered = ps.filterByStockAsync(minStock).join().size();

            System.out.println("[parallelStream] expensive >= 1000: " + expensivePS);
            System.out.println("[executor]       expensive >= 1000: " + expensiveExec);
            System.out.println("totalStockValue: " + totalStockValue);
            System.out.println("filterByStock >= " + minStock + ": " + filtered);

            // 7) Перевірка CRUD: find / update / delete
            Product p = repo.findByIdentity("Phone");
            if (p != null) {
                System.out.println("Found by name 'Phone': " + p);
                // оновимо (імітуємо PUT)
                Product updated = new Product("Phone", p.getPrice() + 100, p.getStock(), p.getCreatedDate());
                boolean ok = repo.replaceByIdentity("Phone", updated);
                System.out.println("Replace 'Phone' -> " + ok);
                // видалимо (DELETE)
                boolean removed = repo.removeByIdentity("Phone");
                System.out.println("Remove 'Phone' -> " + removed);
            }
        } finally {
            executor.shutdown();
        }
    }
}