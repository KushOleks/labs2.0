package ua;

import ua.model.*;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {

        OrderStatus orderStatus = OrderStatus.SHIPPED;
        PaymentStatus paymentStatus = PaymentStatus.PAID;

        System.out.println("Order Status: " + orderStatus);
        System.out.println("Payment Status: " + paymentStatus);

        switch (orderStatus) {
            case NEW -> System.out.println("Order created, waiting for processing.");
            case PROCESSING -> System.out.println("Order is being processed.");
            case SHIPPED -> System.out.println("Order has been shipped!");
            case DELIVERED -> System.out.println("Order successfully delivered!");
            case RETURNED -> System.out.println("Order was returned.");
        }

        String paymentMessage = switch (paymentStatus) {
            case PENDING -> "Waiting for payment confirmation.";
            case PAID -> "Payment received!";
            case FAILED -> "Payment failed, try again.";
            case REFUNDED -> "Payment refunded.";
        };
        System.out.println(paymentMessage);


        CustomerRecord customer = new CustomerRecord("Ivan", "Petrov", "ivan@mail.com", LocalDate.of(2025, 1, 10));
        Product product = new Product("Laptop", 1500.0, 5, LocalDate.of(2025, 1, 5));
        ReviewRecord review = new ReviewRecord(product, customer, 5, "Great product!", LocalDate.of(2025, 2, 1));

        System.out.println("\nCustomer: " + customer.fullName());
        System.out.println("Review summary: " + review.summary());
    }
}