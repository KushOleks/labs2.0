package ua;

import ua.model.Product;
import ua.util.StudentRepository;
import ua.util.GroupRepository;

import java.time.LocalDate;
import java.util.logging.Logger;

public class MainStream {
    private static final Logger logger = Logger.getLogger(MainStream.class.getName());

    public static void main(String[] args) {
        logger.info("=== demonstration ===");

        StudentRepository studentRepo = new StudentRepository();
        GroupRepository groupRepo = new GroupRepository();

        studentRepo.add(new Product("Laptop", 1500, 5, LocalDate.of(2025, 1, 5)));
        studentRepo.add(new Product("Mouse", 25, 100, LocalDate.of(2025, 2, 1)));
        studentRepo.add(new Product("Keyboard", 40, 50, LocalDate.of(2025, 2, 10)));
        studentRepo.add(new Product("Monitor", 300, 20, LocalDate.of(2025, 2, 20)));
        studentRepo.add(new Product("Headphones", 120, 40, LocalDate.of(2025, 3, 1)));

        groupRepo.add(new Product("Gaming Bundle", 800, 12, LocalDate.of(2025, 3, 1)));
        groupRepo.add(new Product("Office Set", 300, 20, LocalDate.of(2025, 3, 5)));
        groupRepo.add(new Product("Creator Pack", 1200, 8, LocalDate.of(2025, 3, 10)));

        System.out.println("\n🔸 Пошук товару за назвою 'Mouse':");
        studentRepo.findByName("Mouse").forEach(System.out::println);

        System.out.println("\n🔸 Пошук товарів за діапазоном ціни 100 - 1000:");
        studentRepo.findByPriceRange(100, 1000).forEach(System.out::println);

        System.out.println("\n🔸 Усі назви товарів:");
        System.out.println(studentRepo.getAllProductNames());

        System.out.println("\n🔸 Середня ціна товарів:");
        System.out.println(studentRepo.averagePrice());

        System.out.println("\n🔸 Товари з кількістю на складі > 10:");
        groupRepo.findWithMinStock(10).forEach(System.out::println);

        System.out.println("\n🔸 Назви всіх груп товарів:");
        System.out.println(groupRepo.getAllNames());

        System.out.println("\n🔸 Об'єднані назви груп товарів:");
        System.out.println(groupRepo.joinNames());

        System.out.println("\n🔸 Кількість товарів із великим запасом (parallel stream):");
        System.out.println(groupRepo.countHighStockParallel(10));
    }
}