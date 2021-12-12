package com.oth.sw.hoffmannairways.entity;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
    @OneToOne
    private Airplane airplane;
    @ManyToOne
    private Staff creator;

    @ManyToOne
    private FlightConnection connection;
    //TODO diagramm
    /*
    @OneToMany(cascade = {CascadeType.ALL})
    private List<Order> orders;

     */

    public Flight(Date departureTime, Airplane airplane, Staff creator, FlightConnection connection) {
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

    public String getFormattedDate(Date oldDate) {
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        return format.format(oldDate);
    }

    public Airplane getAirplane() {
        return airplane;
    }

    public void setAirplane(Airplane airplane) {
        this.airplane = airplane;
    }

    public Staff getCreator() {
        return creator;
    }

    public void setCreator(Staff creator) {
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

}
