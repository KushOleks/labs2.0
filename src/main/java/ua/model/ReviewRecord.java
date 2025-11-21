package ua.model;

import java.time.LocalDate;

public record ReviewRecord(Product product, CustomerRecord customer, int rating, String comment, LocalDate reviewDate) {

    public ReviewRecord {
        if (rating < 1 || rating > 5) throw new IllegalArgumentException("Rating must be 1–5");
        if (comment == null || comment.isBlank()) throw new IllegalArgumentException("Comment cannot be empty");
    }

    public String summary() {
        return customer.fullName() + " rated " + product.getName() + " " + rating + "/5 — " + comment;
    }
}