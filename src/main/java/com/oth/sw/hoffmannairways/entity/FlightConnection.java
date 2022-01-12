package com.oth.sw.hoffmannairways.entity;

import com.oth.sw.hoffmannairways.entity.util.SingleIdEntity;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
public class FlightConnection extends SingleIdEntity<String> {
    @Id
    @Length(min = 5, max = 10)
    private String flightNumber;
    @NotBlank
    private String destination;
    @NotBlank
    private String departure;
    @NotNull
    private double flightTimeHours;


    public FlightConnection(String flightNumber, String destination, String departure, double flightTimeHours) {
        this.flightNumber = flightNumber;
        this.destination = destination;
        this.departure = departure;
        this.flightTimeHours = flightTimeHours;
    }

    @Override
    public String getID() {
        return this.flightNumber;
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
