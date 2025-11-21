package ua.model;

import java.time.LocalDate;

public class Review {
    private Product product;
    private Customer customer;
    private int rating;
    private String comment;
    private LocalDate reviewDate;

    public Review(Product product, Customer customer, int rating, String comment, LocalDate reviewDate) {
        this.product = product;
        this.customer = customer;
        this.rating = rating;
        this.comment = comment;
        this.reviewDate = reviewDate;
    }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public LocalDate getReviewDate() { return reviewDate; }
    public void setReviewDate(LocalDate reviewDate) { this.reviewDate = reviewDate; }

    @Override
    public String toString() {
        return "Review{product=" + product + ", customer=" + customer + ", rating=" + rating + ", comment='" + comment + '\'' + ", reviewDate=" + reviewDate + "}";
    }
}