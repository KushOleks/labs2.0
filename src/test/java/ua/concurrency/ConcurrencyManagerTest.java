package ua.concurrency;

import org.junit.jupiter.api.Test;
import ua.model.Product;
import ua.service.ProductService;
import ua.util.DataSerializer;
import ua.util.GenericRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConcurrencyManagerTest {

    @Test
    void testParallelLoadProductsIntoRepository() throws Exception {
        GenericRepository<Product> repo = new GenericRepository<>(Product::getName);

        List<Product> source = List.of(
                new Product("P1", 100, 1, LocalDate.now()),
                new Product("P2", 200, 2, LocalDate.now()),
                new Product("P3", 300, 3, LocalDate.now())
        );

        String path = "data/test_products_concurrency.json";
        DataSerializer.toJSON(source, path);

        ConcurrencyManager manager = new ConcurrencyManager(4);
        manager.loadFromJson(repo, path, Product.class)
                .get(3, TimeUnit.SECONDS);

        assertEquals(source.size(), repo.size());
    }

    @Test
    void testParallelStreamAndExecutorGiveSameResult() throws Exception {
        GenericRepository<Product> repo = new GenericRepository<>(Product::getName);

        for (int i = 0; i < 100; i++) {
            repo.add(new Product("P" + i, i * 10, i, LocalDate.now()));
        }

        ConcurrencyManager manager = new ConcurrencyManager(4);
        ProductService service = new ProductService(repo, manager.getExecutor());

        long parallel = service.countExpensiveParallelStream(500);
        long executor = service.countExpensiveWithExecutor(500)
                .get(3, TimeUnit.SECONDS);

        assertEquals(parallel, executor);
    }
}