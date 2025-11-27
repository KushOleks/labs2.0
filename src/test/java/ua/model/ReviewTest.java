package ua.model;

import org.junit.jupiter.api.Test;
import ua.exceptions.InvalidDataException;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReviewTest {

    private Product createProduct() {
        return new Product("Phone", 500, 10, LocalDate.now());
    }

    private Customer createCustomer() {
        return new Customer("Ivan", "Ivanov",
                "ivan@example.com", LocalDate.now());
    }

    @Test
    void testValidReview() {
        Product p = createProduct();
        Customer c = createCustomer();
        Review r = new Review(p, c, 5, "Cool", LocalDate.now());
        assertEquals(5, r.getRating());
    }

    @Test
    void testInvalidRating() {
        Product p = createProduct();
        Customer c = createCustomer();
        assertThrows(InvalidDataException.class,
                () -> new Review(p, c, 0, "Bad", LocalDate.now()));
        assertThrows(InvalidDataException.class,
                () -> new Review(p, c, 6, "Bad", LocalDate.now()));
    }

    @Test
    void testNullProduct() {
        Customer c = createCustomer();
        assertThrows(InvalidDataException.class,
                () -> new Review(null, c, 3, "Ok", LocalDate.now()));
    }
}