package ua;

import ua.model.*;
import ua.util.GenericRepository;

import java.time.LocalDate;

public class MainGenerics {
    public static void main(String[] args) {

        System.out.println("=== Demonstration of GenericRepository ===\n");

        GenericRepository<Product> productRepo =
                new GenericRepository<>(Product::getName);

        productRepo.add(new Product("Laptop", 1500, 5, LocalDate.of(2025, 1, 5)));
        productRepo.add(new Product("Mouse", 25, 100, LocalDate.of(2025, 2, 1)));
        productRepo.add(new Product("Keyboard", 40, 50, LocalDate.of(2025, 2, 10)));

        System.out.println("All products:");
        productRepo.getAll().forEach(System.out::println);

        System.out.println("\nFind by name 'Mouse':");
        System.out.println(productRepo.findByIdentity("Mouse"));

        GenericRepository<CustomerRecord> customerRepo =
                new GenericRepository<>(CustomerRecord::email);

        CustomerRecord c1 = new CustomerRecord("Ivan", "Petrov", "ivan@mail.com", LocalDate.of(2025, 1, 10));
        CustomerRecord c2 = new CustomerRecord("Oleh", "Shevchenko", "oleh@mail.com", LocalDate.of(2025, 3, 2));

        customerRepo.add(c1);
        customerRepo.add(c2);

        System.out.println("\nAll customers:");
        customerRepo.getAll().forEach(System.out::println);

        System.out.println("\nFind by email 'ivan@mail.com':");
        System.out.println(customerRepo.findByIdentity("ivan@mail.com"));
    }
}