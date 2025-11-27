package ua.model;

import ua.exceptions.InvalidDataException;
import ua.util.Utils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Shipment {

    private static final Logger logger = Logger.getLogger(Shipment.class.getName());

    private Order order;
    private LocalDate shipmentDate;
    private String trackingNumber;

    public Shipment(Order order, LocalDate shipmentDate, String trackingNumber) {

        List<String> errors = new ArrayList<>();

        if (order == null) {
            errors.add("order: cannot be null");
        }
        if (!Utils.isValidDate(shipmentDate)) {
            errors.add("shipmentDate: invalid");
        }
        if (!Utils.isValidString(trackingNumber)) {
            errors.add("trackingNumber: cannot be empty");
        }

        if (!errors.isEmpty()) {
            logger.warning("Shipment creation failed: " + errors);
            throw new InvalidDataException(errors);
        }

        this.order = order;
        this.shipmentDate = shipmentDate;
        this.trackingNumber = trackingNumber;

        logger.info("Shipment created: " + this);
    }

    public Order getOrder() { return order; }

    public void setOrder(Order order) {
        if (order == null) {
            throw new InvalidDataException("order: cannot be null");
        }
        logger.info("Updating shipment order");
        this.order = order;
    }

    public LocalDate getShipmentDate() { return shipmentDate; }

    public void setShipmentDate(LocalDate shipmentDate) {
        if (!Utils.isValidDate(shipmentDate)) {
            throw new InvalidDataException("shipmentDate: invalid");
        }
        logger.info("Updating shipmentDate from " + this.shipmentDate + " to " + shipmentDate);
        this.shipmentDate = shipmentDate;
    }

    public String getTrackingNumber() { return trackingNumber; }

    public void setTrackingNumber(String trackingNumber) {
        if (!Utils.isValidString(trackingNumber)) {
            throw new InvalidDataException("trackingNumber: cannot be empty");
        }
        logger.info("Updating trackingNumber from '" + this.trackingNumber + "' to '" + trackingNumber + "'");
        this.trackingNumber = trackingNumber;
    }

    @Override
    public String toString() {
        return "Shipment{order=" + order + ", shipmentDate=" + shipmentDate +
                ", trackingNumber='" + trackingNumber + '\'' + "}";
    }
}