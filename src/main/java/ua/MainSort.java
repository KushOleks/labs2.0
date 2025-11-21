package ua;

import ua.model.Product;
import ua.util.StudentRepository;
import ua.util.GroupRepository;

import java.time.LocalDate;
import java.util.logging.Logger;

public class MainSort {
    private static final Logger logger = Logger.getLogger(MainSort.class.getName());

    public static void main(String[] args) {
        logger.info("=== demonstrationnn ===");

        StudentRepository studentRepo = new StudentRepository();
        GroupRepository groupRepo = new GroupRepository();

        studentRepo.add(new Product("Laptop", 1500, 5, LocalDate.of(2025, 1, 5)));
        studentRepo.add(new Product("Mouse", 25, 100, LocalDate.of(2025, 2, 1)));
        studentRepo.add(new Product("Keyboard", 40, 50, LocalDate.of(2025, 2, 10)));

        groupRepo.add(new Product("Gaming Bundle", 800, 12, LocalDate.of(2025, 3, 1)));
        groupRepo.add(new Product("Office Set", 300, 20, LocalDate.of(2025, 3, 5)));
        groupRepo.add(new Product("Creator Pack", 1200, 8, LocalDate.of(2025, 3, 10)));

        System.out.println("\n🔹 Сортування товарів за назвою:");
        studentRepo.sortByName().forEach(System.out::println);

        System.out.println("\n🔹 Сортування товарів за ціною:");
        studentRepo.sortByPrice().forEach(System.out::println);

        System.out.println("\n🔹 Сортування товарів за датою створення:");
        studentRepo.sortByDate().forEach(System.out::println);

        System.out.println("\n🔹 Сортування груп товарів за кількістю на складі:");
        groupRepo.sortByStock().forEach(System.out::println);

        System.out.println("\n🔹 Сортування груп товарів за датою створення (спадання):");
        groupRepo.sortByDateDesc().forEach(System.out::println);

        System.out.println("\n🔹 Сортування груп за identity (назвою):");
        groupRepo.sortByIdentityAsc().forEach(System.out::println);
    }
}