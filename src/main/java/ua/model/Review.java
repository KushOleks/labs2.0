package ua.model;

import ua.exceptions.InvalidDataException;
import ua.util.Utils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Review {

    private static final Logger logger = Logger.getLogger(Review.class.getName());

    private Product product;
    private Customer customer;
    private int rating;           // 1..5
    private String comment;
    private LocalDate reviewDate;

    public Review(Product product, Customer customer, int rating,
                  String comment, LocalDate reviewDate) {

        List<String> errors = new ArrayList<>();

        if (product == null) {
            errors.add("product: cannot be null");
        }
        if (customer == null) {
            errors.add("customer: cannot be null");
        }
        if (rating < 1 || rating > 5) {
            errors.add("rating: must be between 1 and 5");
        }
        if (!Utils.isValidString(comment)) {
            errors.add("comment: cannot be empty");
        }
        if (!Utils.isValidDate(reviewDate)) {
            errors.add("reviewDate: invalid");
        }

        if (!errors.isEmpty()) {
            logger.warning("Review creation failed: " + errors);
            throw new InvalidDataException(errors);
        }

        this.product = product;
        this.customer = customer;
        this.rating = rating;
        this.comment = comment;
        this.reviewDate = reviewDate;

        logger.info("Review created: " + this);
    }

    public Product getProduct() { return product; }

    public void setProduct(Product product) {
        if (product == null) {
            throw new InvalidDataException("product: cannot be null");
        }
        logger.info("Updating review product");
        this.product = product;
    }

    public Customer getCustomer() { return customer; }

    public void setCustomer(Customer customer) {
        if (customer == null) {
            throw new InvalidDataException("customer: cannot be null");
        }
        logger.info("Updating review customer");
        this.customer = customer;
    }

    public int getRating() { return rating; }

    public void setRating(int rating) {
        if (rating < 1 || rating > 5) {
            throw new InvalidDataException("rating: must be between 1 and 5");
        }
        logger.info("Updating rating from " + this.rating + " to " + rating);
        this.rating = rating;
    }

    public String getComment() { return comment; }

    public void setComment(String comment) {
        if (!Utils.isValidString(comment)) {
            throw new InvalidDataException("comment: cannot be empty");
        }
        logger.info("Updating comment");
        this.comment = comment;
    }

    public LocalDate getReviewDate() { return reviewDate; }

    public void setReviewDate(LocalDate reviewDate) {
        if (!Utils.isValidDate(reviewDate)) {
            throw new InvalidDataException("reviewDate: invalid");
        }
        logger.info("Updating reviewDate from " + this.reviewDate + " to " + reviewDate);
        this.reviewDate = reviewDate;
    }

    @Override
    public String toString() {
        return "Review{product=" + product + ", customer=" + customer +
                ", rating=" + rating + ", comment='" + comment + '\'' +
                ", reviewDate=" + reviewDate + "}";
    }
}