package com.oth.sw.hoffmannairways.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.oth.sw.hoffmannairways.entity.util.SingleIdEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "order_table")
public class Order extends SingleIdEntity<Integer> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int orderID;
    @NotNull
    private int totalSeats = 0;
    @NotNull
    private double totalCargoInKg = 0.0;

    @ManyToOne
    private Flight flight;

    @ManyToOne
    @JsonIgnore
    private User customer;

    public Order() {

    }

    public Order(int totalSeats, double totalCargoInKg, Flight flight, User customer) {
        this.totalSeats = totalSeats;
        this.totalCargoInKg = totalCargoInKg;
        this.flight = flight;
        this.customer = customer;
    }

    public Order(int totalSeats, double totalCargoInKg, Flight flight) {
        this.totalSeats = totalSeats;
        this.totalCargoInKg = totalCargoInKg;
        this.flight = flight;
    }

    @Override
    public Integer getID() {
        return this.orderID;
    }

    public int getOrderID() {
        return orderID;
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
                "oderID=" + orderID +
                ", totalSeats=" + totalSeats +
                ", totalCargoInKg=" + totalCargoInKg +
                ", flight=" + flight +
                ", customer=" + customer +
                '}';
    }
}
