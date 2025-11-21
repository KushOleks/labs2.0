package ua.model;

import ua.util.Utils;
import java.time.LocalDate;
import java.util.Objects;

public class Customer implements Comparable<Customer>, Cloneable {

    private String firstName;
    private String lastName;
    private String email;
    protected LocalDate registrationDate;

    public Customer(String firstName, String lastName, String email, LocalDate registrationDate) {
        setFirstName(firstName);
        setLastName(lastName);
        setEmail(email);
        setRegistrationDate(registrationDate);
    }

    public static Customer create(String firstName, String lastName, String email, LocalDate registrationDate) {
        return new Customer(firstName, lastName, email, registrationDate);
    }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) {
        if (!Utils.isValidString(firstName)) {
            throw new IllegalArgumentException("Invalid first name");
        }
        this.firstName = firstName;
    }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) {
        if (!Utils.isValidString(lastName)) {
            throw new IllegalArgumentException("Invalid last name");
        }
        this.lastName = lastName;
    }

    public String getEmail() { return email; }
    public void setEmail(String email) {
        Utils.validateEmail(email);
        this.email = email;
    }

    public LocalDate getRegistrationDate() { return registrationDate; }
    public void setRegistrationDate(LocalDate registrationDate) {
        if (!Utils.isValidDate(registrationDate)) {
            throw new IllegalArgumentException("Invalid registration date");
        }
        this.registrationDate = registrationDate;
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
    protected void finalize() throws Throwable {
        System.out.println("Customer object is being garbage collected: " + this.firstName);
        super.finalize();
    }
}