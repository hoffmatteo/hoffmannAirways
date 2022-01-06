package com.oth.sw.hoffmannairways.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "order_table")
public class Order implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    //TODO orderID
    private int oderID;
    private int totalSeats;
    private double totalCargoInKg;

    @ManyToOne
    private Flight flight;

    @ManyToOne
    private User customer;

    public Order() {

    }

    public Order(int totalSeats, double totalCargoInKg, Flight flight, User customer) {
        this.totalSeats = totalSeats;
        this.totalCargoInKg = totalCargoInKg;
        this.flight = flight;
        this.customer = customer;
    }

    public int getOderID() {
        return oderID;
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

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public User getCustomer() {
        return customer;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
    }

    @Override
    public String toString() {
        return "Order{" +
                "oderID=" + oderID +
                ", totalSeats=" + totalSeats +
                ", totalCargoInKg=" + totalCargoInKg +
                ", flight=" + flight +
                ", customer=" + customer +
                '}';
    }
}
