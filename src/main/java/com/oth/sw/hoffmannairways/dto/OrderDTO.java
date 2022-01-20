package com.oth.sw.hoffmannairways.dto;

public class OrderDTO {
    private int orderID;
    private int totalSeats = 0;
    private double totalCargoInKg = 0.0;
    private FlightDTO flight;

    public OrderDTO(int totalSeats, double totalCargoInKg, FlightDTO flight) {
        this.totalSeats = totalSeats;
        this.totalCargoInKg = totalCargoInKg;
        this.flight = flight;
    }

    public OrderDTO() {
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(int totalSeats) {
        this.totalSeats = totalSeats;
    }

    public double getTotalCargoInKg() {
        return totalCargoInKg;
    }

    public void setTotalCargoInKg(double totalCargoInKg) {
        this.totalCargoInKg = totalCargoInKg;
    }

    public FlightDTO getFlight() {
        return flight;
    }

    public void setFlight(FlightDTO flight) {
        this.flight = flight;
    }
}
