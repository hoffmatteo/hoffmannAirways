package com.oth.sw.hoffmannairways.dto;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

public class FlightDTO {
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

    public FlightDTO() {
    }

    public FlightDTO(int flightID, Date departureTime, Date arrivalTime, int bookedSeats, double bookedCargoInKg) {
        this.flightID = flightID;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.bookedSeats = bookedSeats;
        this.bookedCargoInKg = bookedCargoInKg;
    }

    public int getFlightID() {
        return flightID;
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
