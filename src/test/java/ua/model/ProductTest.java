package ua.model;

import org.junit.jupiter.api.Test;
import ua.exceptions.InvalidDataException;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    void testValidCreation() {
        Product p = new Product("Phone", 500, 10, LocalDate.now());
        assertEquals("Phone", p.getName());
        assertEquals(500, p.getPrice());
        assertEquals(10, p.getStock());
    }

    @Test
    void testInvalidName() {
        assertThrows(InvalidDataException.class,
                () -> new Product("", 500, 10, LocalDate.now()));
    }

    @Test
    void testInvalidPrice() {
        assertThrows(InvalidDataException.class,
                () -> new Product("Phone", -5, 10, LocalDate.now()));
    }

    @Test
    void testInvalidStock() {
        assertThrows(InvalidDataException.class,
                () -> new Product("Phone", 500, -2, LocalDate.now()));
    }

    @Test
    void testCompareByPrice() {
        Product cheap = new Product("A", 100, 1, LocalDate.now());
        Product expensive = new Product("B", 200, 1, LocalDate.now());
        assertTrue(cheap.compareTo(expensive) < 0);
    }

    @Test
    void testEquals() {
        LocalDate date = LocalDate.now();
        Product p1 = new Product("Phone", 500, 10, date);
        Product p2 = new Product("Phone", 500, 10, date);
        assertEquals(p1, p2);
    }
}