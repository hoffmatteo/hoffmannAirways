package com.oth.sw.hoffmannairways.entity;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

@Entity
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int flightID;
    //difference, naming
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private Date departureTime;
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private Date arrivalTime;
    private int bookedSeats = 0;
    private double bookedCargoInKg = 0.0;

    //cascade stuff
    @OneToOne(cascade = CascadeType.ALL)
    private Airplane airplane;
    @ManyToOne
    private User creator;

    @ManyToOne
    private FlightConnection connection;
    //TODO diagramm
    /*
    @OneToMany(cascade = {CascadeType.ALL})
    private List<Order> orders;

     */

    public Flight(Date departureTime, Airplane airplane, User creator, FlightConnection connection) {
        this.departureTime = departureTime;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(departureTime);
        calendar.add(Calendar.MINUTE, (int) (connection.getFlightTimeHours() * 60));

        this.arrivalTime = calendar.getTime();
        this.bookedSeats = 0;
        this.bookedCargoInKg = 0;
        this.airplane = airplane;
        this.creator = creator;
        this.connection = connection;
    }

    public Flight() {

    }


    public Airplane getAirplane() {
        return airplane;
    }

    public void setAirplane(Airplane airplane) {
        this.airplane = airplane;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public FlightConnection getConnection() {
        return connection;
    }

    public void setConnection(FlightConnection connection) {
        this.connection = connection;
    }

    public int getFlightID() {
        return this.flightID;
    }

    public void setFlightID(int flightID) {
        this.flightID = flightID;
    }

    public Date getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Date departureTime) {
        this.departureTime = departureTime;
    }

    public Date getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(Date arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getBookedSeats() {
        return bookedSeats;
    }

    public void setBookedSeats(int bookedSeats) {
        this.bookedSeats = bookedSeats;
    }

    public double getBookedCargoInKg() {
        return bookedCargoInKg;
    }

    public void setBookedCargoInKg(double bookedCargoInKg) {
        this.bookedCargoInKg = bookedCargoInKg;
    }

    @Override
    public String toString() {
        return "Flight{" +
                "flightID=" + flightID +
                ", departureTime=" + departureTime +
                ", arrivalTime=" + arrivalTime +
                ", bookedSeats=" + bookedSeats +
                ", bookedCargoInKg=" + bookedCargoInKg +
                ", airplane=" + airplane.getPlaneID() +
                ", creator=" + creator +
                ", connection=" + connection +
                '}';
    }
}
