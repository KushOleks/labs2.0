package ua.model;

import java.time.LocalDate;
import java.util.List;

public class Order {
    private Customer customer;
    private List<Product> products;
    private LocalDate orderDate;
    private double totalAmount;

    public Order(Customer customer, List<Product> products, LocalDate orderDate, double totalAmount) {
        this.customer = customer;
        this.products = products;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
    }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }

    public List<Product> getProducts() { return products; }
    public void setProducts(List<Product> products) { this.products = products; }

    public LocalDate getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDate orderDate) { this.orderDate = orderDate; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    @Override
    public String toString() {
        return "Order{customer=" + customer + ", products=" + products + ", orderDate=" + orderDate + ", totalAmount=" + totalAmount + "}";
    }
}