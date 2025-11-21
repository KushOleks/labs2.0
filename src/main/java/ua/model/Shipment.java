package ua.model;

import java.time.LocalDate;

public class Shipment {
    private Order order;
    private LocalDate shipmentDate;
    private String trackingNumber;

    public Shipment(Order order, LocalDate shipmentDate, String trackingNumber) {
        this.order = order;
        this.shipmentDate = shipmentDate;
        this.trackingNumber = trackingNumber;
    }

    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }

    public LocalDate getShipmentDate() { return shipmentDate; }
    public void setShipmentDate(LocalDate shipmentDate) { this.shipmentDate = shipmentDate; }

    public String getTrackingNumber() { return trackingNumber; }
    public void setTrackingNumber(String trackingNumber) { this.trackingNumber = trackingNumber; }

    @Override
    public String toString() {
        return "Shipment{order=" + order + ", shipmentDate=" + shipmentDate + ", trackingNumber='" + trackingNumber + '\'' + "}";
    }
}