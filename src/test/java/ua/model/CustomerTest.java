package ua.model;

import org.junit.jupiter.api.Test;
import ua.exceptions.InvalidDataException;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {

    @Test
    void testValidCreation() {
        Customer c = new Customer("Ivan", "Ivanov",
                "ivan@example.com", LocalDate.now());
        assertEquals("Ivan", c.getFirstName());
        assertEquals("Ivanov", c.getLastName());
        assertEquals("ivan@example.com", c.getEmail());
    }

    @Test
    void testInvalidEmail() {
        assertThrows(InvalidDataException.class,
                () -> new Customer("Ivan", "Ivanov",
                        "bad-email", LocalDate.now()));
    }

    @Test
    void testInvalidFirstName() {
        assertThrows(InvalidDataException.class,
                () -> new Customer("", "Ivanov",
                        "ivan@example.com", LocalDate.now()));
    }

    @Test
    void testSettersValidation() {
        Customer c = new Customer("Ivan", "Ivanov",
                "ivan@example.com", LocalDate.now());
        assertThrows(InvalidDataException.class, () -> c.setEmail("wrong"));
        assertThrows(InvalidDataException.class, () -> c.setFirstName(""));
    }
}