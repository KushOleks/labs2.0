package ua;

import ua.model.*;
import ua.exceptions.InvalidDataException;
import ua.util.GenericRepository;
import ua.util.IdentityExtractor;

import java.time.LocalDate;
import java.util.List;

public class MainValidation {
    public static void main(String[] args) {

        GenericRepository<Product> productRepo =
                new GenericRepository<>(Product::getName);

        System.out.println("=== DEMO: Validation & Repositories ===\n");

        // 1. Valid Product
        try {
            Product phone = new Product("Phone", 500, 10, LocalDate.now());
            productRepo.add(phone);
            System.out.println("VALID: " + phone);
        } catch (InvalidDataException e) {
            System.out.println("Unexpected validation error: " + e.getMessage());
        }

        // 2. Invalid Product (multiple errors)
        try {
            new Product("", -100, -5, null);
        } catch (InvalidDataException e) {
            System.out.println("INVALID PRODUCT → " + e.getMessage());
        }

        // 3. Customer validation
        try {
            Customer ivan = new Customer("Ivan", "Ivanov", "ivan@example.com", LocalDate.now());
            System.out.println("VALID CUSTOMER: " + ivan);
        } catch (InvalidDataException e) {
            System.out.println("Unexpected: " + e.getMessage());
        }

        try {
            new Customer("Ivan", "", "wrong-email", null);
        } catch (InvalidDataException e) {
            System.out.println("INVALID CUSTOMER → " + e.getMessage());
        }

        // 4. Trying Review with invalid rating
        try {
            Product p = new Product("TV", 1500, 3, LocalDate.now());
            Customer c = new Customer("Anna", "Petrova", "anna@mail.com", LocalDate.now());

            new Review(p, c, 10, "Too big", LocalDate.now());
        } catch (InvalidDataException e) {
            System.out.println("INVALID REVIEW → " + e.getMessage());
        }

        // 5. Valid Review
        try {
            Product tv = new Product("TV", 1500, 5, LocalDate.now());
            Customer anna = new Customer("Anna", "Petrova", "anna@mail.com", LocalDate.now());

            Review review = new Review(tv, anna, 5, "Excellent!", LocalDate.now());
            System.out.println("VALID REVIEW: " + review);
        } catch (InvalidDataException e) {
            System.out.println("Unexpected: " + e.getMessage());
        }

        // 6. Final repository output
        System.out.println("\n--- Repository content ---");
        productRepo.getAll().forEach(System.out::println);

        System.out.println("\n=== END OF DEMO ===");
    }
}