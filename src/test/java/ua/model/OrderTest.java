package ua.model;

import org.junit.jupiter.api.Test;
import ua.exceptions.InvalidDataException;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    private Product createProduct() {
        return new Product("Phone", 500, 10, LocalDate.now());
    }

    private Customer createCustomer() {
        return new Customer("Ivan", "Ivanov",
                "ivan@example.com", LocalDate.now());
    }

    @Test
    void testValidOrder() {
        Customer c = createCustomer();
        Product p = createProduct();

        Order order = new Order(c, List.of(p), LocalDate.now(), 500);
        assertEquals(c, order.getCustomer());
        assertEquals(1, order.getProducts().size());
    }

    @Test
    void testEmptyProducts() {
        Customer c = createCustomer();
        assertThrows(InvalidDataException.class,
                () -> new Order(c, List.of(), LocalDate.now(), 0));
    }

    @Test
    void testNegativeTotalAmount() {
        Customer c = createCustomer();
        Product p = createProduct();
        assertThrows(InvalidDataException.class,
                () -> new Order(c, List.of(p), LocalDate.now(), -10));
    }
}