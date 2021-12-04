package com.oth.sw.hoffmannairways.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class FlightConnection {
    @Id
    private String flightnumber;
    private String destination;
    private String departure;
    private double flightTimeHours;


    public FlightConnection(String flightnumber, String destination, String departure, double flightTimeHours) {
        this.flightnumber = flightnumber;
        this.destination = destination;
        this.departure = departure;
        this.flightTimeHours = flightTimeHours;
    }


    public String getFlightnumber() {
        return flightnumber;
    }

    public void setFlightnumber(String flightnumber) {
        this.flightnumber = flightnumber;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public double getFlightTimeHours() {
        return flightTimeHours;
    }

    public void setFlightTimeHours(double flightTimeHours) {
        this.flightTimeHours = flightTimeHours;
    }

    public FlightConnection() {

    }
}
