package ua.model;

import org.junit.jupiter.api.Test;
import ua.exceptions.InvalidDataException;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ShipmentTest {

    private Product createProduct() {
        return new Product("Phone", 500, 10, LocalDate.now());
    }

    private Customer createCustomer() {
        return new Customer("Ivan", "Ivanov",
                "ivan@example.com", LocalDate.now());
    }

    private Order createOrder() {
        return new Order(createCustomer(), List.of(createProduct()),
                LocalDate.now(), 500);
    }

    @Test
    void testValidShipment() {
        Order order = createOrder();
        Shipment shipment = new Shipment(order, LocalDate.now(), "TRACK123");
        assertEquals(order, shipment.getOrder());
        assertEquals("TRACK123", shipment.getTrackingNumber());
    }

    @Test
    void testInvalidTrackingNumber() {
        Order order = createOrder();
        assertThrows(InvalidDataException.class,
                () -> new Shipment(order, LocalDate.now(), ""));
    }

    @Test
    void testNullOrder() {
        assertThrows(InvalidDataException.class,
                () -> new Shipment(null, LocalDate.now(), "TRACK"));
    }
}