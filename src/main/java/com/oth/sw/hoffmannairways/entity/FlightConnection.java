package com.oth.sw.hoffmannairways.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class FlightConnection {
    @Id
    private String flightNumber;
    private String destination;
    private String departure;
    private double flightTimeHours;


    public FlightConnection(String flightNumber, String destination, String departure, double flightTimeHours) {
        this.flightNumber = flightNumber;
        this.destination = destination;
        this.departure = departure;
        this.flightTimeHours = flightTimeHours;
    }


    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightnumber) {
        this.flightNumber = flightnumber;
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

    @Override
    public String toString() {
        return "FlightConnection{" +
                "flightNumber='" + flightNumber + '\'' +
                ", destination='" + destination + '\'' +
                ", departure='" + departure + '\'' +
                ", flightTimeHours=" + flightTimeHours +
                '}';
    }
}
