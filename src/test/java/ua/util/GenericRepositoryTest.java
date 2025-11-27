package ua.util;

import org.junit.jupiter.api.Test;
import ua.model.Product;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GenericRepositoryTest {

    private GenericRepository<Product> createRepo() {
        // identity = name продукту
        IdentityExtractor<Product> extractor = Product::getName;
        GenericRepository<Product> repo = new GenericRepository<>(extractor);

        repo.add(new Product("Phone",   500,  10, LocalDate.of(2024, 1, 1)));
        repo.add(new Product("TV",     1500,   5, LocalDate.of(2023, 5, 10)));
        repo.add(new Product("Laptop", 2500,   2, LocalDate.of(2022, 3, 3)));

        return repo;
    }

    @Test
    void testAddAndGetAll() {
        GenericRepository<Product> repo = createRepo();

        List<Product> all = repo.getAll();
        assertEquals(3, all.size());
        assertTrue(all.stream().anyMatch(p -> p.getName().equals("Phone")));
        assertTrue(all.stream().anyMatch(p -> p.getName().equals("TV")));
        assertTrue(all.stream().anyMatch(p -> p.getName().equals("Laptop")));
    }

    @Test
    void testFindByIdentityFound() {
        GenericRepository<Product> repo = createRepo();

        Product p = repo.findByIdentity("TV");
        assertNotNull(p);
        assertEquals("TV", p.getName());
        assertEquals(1500, p.getPrice());
    }

    @Test
    void testFindByIdentityNotFound() {
        GenericRepository<Product> repo = createRepo();

        Product p = repo.findByIdentity("NonExisting");
        assertNull(p); // метод у тебе повертає null, якщо не знайшов
    }

    @Test
    void testRemove() {
        GenericRepository<Product> repo = createRepo();

        Product phone = repo.findByIdentity("Phone");
        assertNotNull(phone);

        repo.remove(phone);

        List<Product> all = repo.getAll();
        assertEquals(2, all.size());
        assertNull(repo.findByIdentity("Phone"));
    }

    @Test
    void testSortNaturalByPrice() {
        // Product implements Comparable<Product> по price
        GenericRepository<Product> repo = createRepo();

        List<Product> sorted = repo.sortNatural();

        // очікуємо порядок за ціною: Phone(500), TV(1500), Laptop(2500)
        assertEquals("Phone",  sorted.get(0).getName());
        assertEquals("TV",     sorted.get(1).getName());
        assertEquals("Laptop", sorted.get(2).getName());
    }

    @Test
    void testSortByComparatorByName() {
        GenericRepository<Product> repo = createRepo();

        // сортуємо за імʼям
        List<Product> sorted = repo.sortBy(Comparator.comparing(Product::getName));

        // алфавітний порядок: Laptop, Phone, TV
        assertEquals("Laptop", sorted.get(0).getName());
        assertEquals("Phone",  sorted.get(1).getName());
        assertEquals("TV",     sorted.get(2).getName());
    }

    @Test
    void testSortByIdentityAscAndDesc() {
        GenericRepository<Product> repo = createRepo();

        // identity = name, отже sortByIdentity сортує за імʼям
        List<Product> asc = repo.sortByIdentity("asc");
        List<Product> desc = repo.sortByIdentity("desc");

        // ascending: Laptop, Phone, TV
        assertEquals("Laptop", asc.get(0).getName());
        assertEquals("Phone",  asc.get(1).getName());
        assertEquals("TV",     asc.get(2).getName());

        // descending: TV, Phone, Laptop
        assertEquals("TV",     desc.get(0).getName());
        assertEquals("Phone",  desc.get(1).getName());
        assertEquals("Laptop", desc.get(2).getName());
    }
}