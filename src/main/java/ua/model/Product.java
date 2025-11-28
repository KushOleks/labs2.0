package ua.model;

import ua.exceptions.InvalidDataException;
import ua.util.Utils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

public class Product implements Comparable<Product> {

    private static final Logger logger = Logger.getLogger(Product.class.getName());

    private String name;
    private double price;
    private int stock;
    private LocalDate createdDate;

    // Порожній конструктор залишаємо для Jackson
    public Product() {
    }

    public Product(String name, double price, int stock, LocalDate createdDate) {
        List<String> errors = new ArrayList<>();

        if (!Utils.isValidString(name)) {
            errors.add("name: cannot be empty");
        }
        if (price < 0) {
            errors.add("price: must be >= 0");
        }
        if (stock < 0) {
            errors.add("stock: must be >= 0");
        }
        if (!Utils.isValidDate(createdDate)) {
            errors.add("createdDate: invalid");
        }

        if (!errors.isEmpty()) {
            logger.warning("Product creation failed: " + errors);
            throw new InvalidDataException(errors);
        }

        this.name = name;
        this.price = price;
        this.stock = stock;
        this.createdDate = createdDate;

        logger.info("Product created: " + this);
    }

    public static Product create(String name, double price, int stock, LocalDate createdDate) {
        return new Product(name, price, stock, createdDate);
    }

    public String getName() { return name; }

    public void setName(String name) {
        if (!Utils.isValidString(name)) {
            throw new InvalidDataException("name: cannot be empty");
        }
        logger.info("Updating product name from '" + this.name + "' to '" + name + "'");
        this.name = name;
    }

    public double getPrice() { return price; }

    public void setPrice(double price) {
        if (price < 0) {
            throw new InvalidDataException("price: must be >= 0");
        }
        logger.info("Updating product price from " + this.price + " to " + price);
        this.price = price;
    }

    public int getStock() { return stock; }

    public void setStock(int stock) {
        if (stock < 0) {
            throw new InvalidDataException("stock: must be >= 0");
        }
        logger.info("Updating product stock from " + this.stock + " to " + stock);
        this.stock = stock;
    }

    public LocalDate getCreatedDate() { return createdDate; }

    public void setCreatedDate(LocalDate createdDate) {
        if (!Utils.isValidDate(createdDate)) {
            throw new InvalidDataException("createdDate: invalid");
        }
        logger.info("Updating product createdDate from " + this.createdDate + " to " + createdDate);
        this.createdDate = createdDate;
    }

    @Override
    public int compareTo(Product other) {
        return Double.compare(this.price, other.price);
    }

    public static final Comparator<Product> BY_NAME =
            Comparator.comparing(Product::getName);

    public static final Comparator<Product> BY_STOCK =
            Comparator.comparingInt(Product::getStock);

    public static final Comparator<Product> BY_DATE =
            Comparator.comparing(Product::getCreatedDate);

    @Override
    public String toString() {
        return "Product{name='" + name + "', price=" + price +
                ", stock=" + stock + ", createdDate=" + createdDate + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        Product product = (Product) o;
        return Double.compare(product.price, price) == 0 &&
                stock == product.stock &&
                Objects.equals(name, product.name) &&
                Objects.equals(createdDate, product.createdDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price, stock, createdDate);
    }
}