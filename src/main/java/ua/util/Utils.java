package ua.util;

import java.time.LocalDate;

public class Utils {

    public static boolean isValidString(String value) {
        return value != null && !value.trim().isEmpty();
    }

    public static boolean isValidDate(LocalDate date) {
        return date != null && !date.isAfter(LocalDate.now());
    }

    public static void validateEmail(String email) {
        if (email == null ||
                !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new IllegalArgumentException("email: invalid format");
        }
    }
}