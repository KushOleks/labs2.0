package ua.model;

import java.time.LocalDate;

public record CustomerRecord(String firstName, String lastName, String email, LocalDate registrationDate) {

    public String fullName() {
        return firstName + " " + lastName;
    }

    public CustomerRecord {
        if (firstName == null || firstName.isBlank()) throw new IllegalArgumentException("Invalid first name");
        if (lastName == null || lastName.isBlank()) throw new IllegalArgumentException("Invalid last name");
        if (email == null || !email.contains("@")) throw new IllegalArgumentException("Invalid email");
        if (registrationDate == null || registrationDate.isAfter(LocalDate.now()))
            throw new IllegalArgumentException("Invalid registration date");
    }
}