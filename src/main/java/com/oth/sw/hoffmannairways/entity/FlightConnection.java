package com.oth.sw.hoffmannairways.entity;

import com.oth.sw.hoffmannairways.entity.util.SingleIdEntity;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
public class FlightConnection extends SingleIdEntity<String> {
    //TODO destinationCity, departureCity!
    @Id
    @Length(min = 5, max = 10)
    private String flightNumber;
    @NotBlank
    private String destinationAirport;
    @NotBlank
    private String departureAirport;
    @NotBlank
    private String destinationCountry;
    @NotBlank
    private String departureCountry;
    @NotNull
    private double flightTimeHours;


    public FlightConnection(String flightNumber, String destinationAirport, String departureAirport, String destinationCountry, String departureCountry, double flightTimeHours) {
        this.flightNumber = flightNumber;
        this.destinationAirport = destinationAirport;
        this.departureAirport = departureAirport;
        this.flightTimeHours = flightTimeHours;
        this.departureCountry = departureCountry;
        this.destinationCountry = destinationCountry;
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

    public String getDestinationAirport() {
        return destinationAirport;
    }

    public void setDestinationAirport(String destination) {
        this.destinationAirport = destination;
    }

    public String getDepartureAirport() {
        return departureAirport;
    }

    public void setDepartureAirport(String departure) {
        this.departureAirport = departure;
    }

    public double getFlightTimeHours() {
        return flightTimeHours;
    }

    public void setFlightTimeHours(double flightTimeHours) {
        this.flightTimeHours = flightTimeHours;
    }

    public String getDestinationCountry() {
        return destinationCountry;
    }

    public void setDestinationCountry(String destinationCountry) {
        this.destinationCountry = destinationCountry;
    }

    public String getDepartureCountry() {
        return departureCountry;
    }

    public void setDepartureCountry(String departureCountry) {
        this.departureCountry = departureCountry;
    }

    public FlightConnection() {

    }

    @Override
    public String toString() {
        return "FlightConnection{" +
                "flightNumber='" + flightNumber + '\'' +
                ", destinationAirport='" + destinationAirport + '\'' +
                ", departureAirport='" + departureAirport + '\'' +
                ", destinationCountry='" + destinationCountry + '\'' +
                ", departureCountry='" + departureCountry + '\'' +
                ", flightTimeHours=" + flightTimeHours +
                "} " + super.toString();
    }
}
