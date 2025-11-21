package ua.util;

import java.time.LocalDate;

class ValidationHelper {
    static boolean isValidString(String value) {
        return value != null && !value.trim().isEmpty();
    }

    static boolean isValidPrice(double price) {
        return price >= 0;
    }

    static boolean isValidStock(int stock) {
        return stock >= 0;
    }

    static boolean isValidEmail(String email) {
        return email != null && email.contains("@");
    }

    static boolean isValidDate(LocalDate date) {
        return date != null && !date.isAfter(LocalDate.now());
    }

    static boolean isValidRating(int rating) {
        return rating >= 1 && rating <= 5;
    }
}