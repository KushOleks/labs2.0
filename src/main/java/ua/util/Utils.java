package ua.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Utils {

    public static boolean isValidString(String value) {
        return value != null && !value.trim().isEmpty();
    }

    public static boolean isValidDate(LocalDate date) {
        return date != null && !date.isAfter(LocalDate.now());
    }

    public static void validateEmail(String email) {
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Invalid email: " + email);
        }
    }

    // Форматування дати
    public static String formatDate(LocalDate date) {
        if (!isValidDate(date)) {
            throw new IllegalArgumentException("Invalid date: " + date);
        }
        return date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }
}