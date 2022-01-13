package com.oth.sw.hoffmannairways.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.oth.sw.hoffmannairways.entity.util.SingleIdEntity;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Flight flight;

    @ManyToOne
    @JsonIgnore
    private User customer;

    public Order() {

    }

    public Order(int totalSeats, double totalCargoInKg, Flight flight, User customer) {
        //TODO if 0 seats and 0 cargo is booked?
        this.totalSeats = totalSeats;
        this.totalCargoInKg = totalCargoInKg;
        this.flight = flight;
        this.customer = customer;
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
