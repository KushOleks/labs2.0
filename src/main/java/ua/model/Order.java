package ua.model;

import ua.exceptions.InvalidDataException;
import ua.util.Utils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Order {

    private static final Logger logger = Logger.getLogger(Order.class.getName());

    private Customer customer;
    private List<Product> products;
    private LocalDate orderDate;
    private double totalAmount;

    public Order(Customer customer, List<Product> products,
                 LocalDate orderDate, double totalAmount) {

        List<String> errors = new ArrayList<>();

        if (customer == null) {
            errors.add("customer: cannot be null");
        }
        if (products == null || products.isEmpty()) {
            errors.add("products: cannot be null or empty");
        }
        if (!Utils.isValidDate(orderDate)) {
            errors.add("orderDate: invalid");
        }
        if (totalAmount < 0) {
            errors.add("totalAmount: must be >= 0");
        }

        if (!errors.isEmpty()) {
            logger.warning("Order creation failed: " + errors);
            throw new InvalidDataException(errors);
        }

        this.customer = customer;
        this.products = new ArrayList<>(products);
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;

        logger.info("Order created: " + this);
    }

    public Customer getCustomer() { return customer; }

    public void setCustomer(Customer customer) {
        if (customer == null) {
            throw new InvalidDataException("customer: cannot be null");
        }
        logger.info("Updating order customer");
        this.customer = customer;
    }

    public List<Product> getProducts() { return new ArrayList<>(products); }

    public void setProducts(List<Product> products) {
        if (products == null || products.isEmpty()) {
            throw new InvalidDataException("products: cannot be null or empty");
        }
        logger.info("Updating order products");
        this.products = new ArrayList<>(products);
    }

    public LocalDate getOrderDate() { return orderDate; }

    public void setOrderDate(LocalDate orderDate) {
        if (!Utils.isValidDate(orderDate)) {
            throw new InvalidDataException("orderDate: invalid");
        }
        logger.info("Updating orderDate from " + this.orderDate + " to " + orderDate);
        this.orderDate = orderDate;
    }

    public double getTotalAmount() { return totalAmount; }

    public void setTotalAmount(double totalAmount) {
        if (totalAmount < 0) {
            throw new InvalidDataException("totalAmount: must be >= 0");
        }
        logger.info("Updating totalAmount from " + this.totalAmount + " to " + totalAmount);
        this.totalAmount = totalAmount;
    }

    @Override
    public String toString() {
        return "Order{customer=" + customer + ", products=" + products +
                ", orderDate=" + orderDate + ", totalAmount=" + totalAmount + "}";
    }
}