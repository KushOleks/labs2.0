package ua.model;

import ua.exceptions.InvalidDataException;
import ua.util.Utils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

public class Customer implements Comparable<Customer>, Cloneable {

    private static final Logger logger = Logger.getLogger(Customer.class.getName());

    private String firstName;
    private String lastName;
    private String email;
    protected LocalDate registrationDate;

    public Customer(String firstName, String lastName, String email, LocalDate registrationDate) {
        List<String> errors = new ArrayList<>();

        if (!Utils.isValidString(firstName)) {
            errors.add("firstName: cannot be empty");
        }
        if (!Utils.isValidString(lastName)) {
            errors.add("lastName: cannot be empty");
        }
        if (!isValidEmail(email)) {
            errors.add("email: invalid");
        }
        if (!Utils.isValidDate(registrationDate)) {
            errors.add("registrationDate: invalid");
        }

        if (!errors.isEmpty()) {
            logger.warning("Customer creation failed: " + errors);
            throw new InvalidDataException(errors);
        }

        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.registrationDate = registrationDate;

        logger.info("Customer created: " + this);
    }

    public static Customer create(String firstName, String lastName, String email, LocalDate registrationDate) {
        return new Customer(firstName, lastName, email, registrationDate);
    }

    public String getFirstName() { return firstName; }

    public void setFirstName(String firstName) {
        if (!Utils.isValidString(firstName)) {
            throw new InvalidDataException("firstName: cannot be empty");
        }
        logger.info("Updating firstName from '" + this.firstName + "' to '" + firstName + "'");
        this.firstName = firstName;
    }

    public String getLastName() { return lastName; }

    public void setLastName(String lastName) {
        if (!Utils.isValidString(lastName)) {
            throw new InvalidDataException("lastName: cannot be empty");
        }
        logger.info("Updating lastName from '" + this.lastName + "' to '" + lastName + "'");
        this.lastName = lastName;
    }

    public String getEmail() { return email; }

    public void setEmail(String email) {
        if (!isValidEmail(email)) {
            throw new InvalidDataException("email: invalid");
        }
        logger.info("Updating email from '" + this.email + "' to '" + email + "'");
        this.email = email;
    }

    public LocalDate getRegistrationDate() { return registrationDate; }

    public void setRegistrationDate(LocalDate registrationDate) {
        if (!Utils.isValidDate(registrationDate)) {
            throw new InvalidDataException("registrationDate: invalid");
        }
        logger.info("Updating registrationDate from " + this.registrationDate + " to " + registrationDate);
        this.registrationDate = registrationDate;
    }

    private static boolean isValidEmail(String email) {
        if (email == null || email.isBlank()) return false;
        // Простий, але адекватний варіант
        return email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");
    }

    @Override
    public String toString() {
        return "Customer{firstName='" + firstName + "', lastName='" + lastName +
                "', email='" + email + "', registrationDate=" + registrationDate + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Customer)) return false;
        Customer customer = (Customer) o;
        return Objects.equals(firstName, customer.firstName) &&
                Objects.equals(lastName, customer.lastName) &&
                Objects.equals(email, customer.email) &&
                Objects.equals(registrationDate, customer.registrationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, email, registrationDate);
    }

    @Override
    public int compareTo(Customer other) {
        int cmp = this.lastName.compareToIgnoreCase(other.lastName);
        if (cmp == 0) {
            cmp = this.firstName.compareToIgnoreCase(other.firstName);
        }
        return cmp;
    }

    @Override
    public Customer clone() {
        try {
            return (Customer) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("Cloning not supported", e);
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    protected void finalize() throws Throwable {
        System.out.println("Customer object is being garbage collected: " + this.firstName);
        super.finalize();
    }
}