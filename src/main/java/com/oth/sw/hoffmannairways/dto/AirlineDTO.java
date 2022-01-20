package com.oth.sw.hoffmannairways.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.oth.sw.hoffmannairways.entity.Flight;
import com.oth.sw.hoffmannairways.entity.FlightConnection;

import java.io.Serializable;
import java.util.Collection;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AirlineDTO implements Serializable {
    int messageID; //dictated by customer
    Collection<Flight> availableFlights;
    Collection<FlightConnection> availableConnections;
    Flight currentFlight; //contains updated information
    Status status;


    public AirlineDTO(Status status) {
        this.status = status;
    }

    public enum Status {
        CANCELLED,
        CHANGED,
        CONFIRMED,
        INFO_CONNECTIONS,
        INFO_FLIGHTS,
        ERROR //previous message threw error
    }

    public AirlineDTO() {

    }

    public AirlineDTO(int messageID, Status status) {
        this.messageID = messageID;
        this.status = status;
    }

    public AirlineDTO(Collection<Flight> availableFlights, Collection<FlightConnection> availableConnections, Status status) {
        this.availableFlights = availableFlights;
        this.availableConnections = availableConnections;
        this.status = status;
    }

    public AirlineDTO(int messageID, Flight currentFlight, Status status) {
        this.messageID = messageID;
        this.currentFlight = currentFlight;
        this.status = status;
    }

    public AirlineDTO(Flight currentFlight, Status status) {
        this.currentFlight = currentFlight;
        this.status = status;
    }

    public Collection<Flight> getAvailableFlights() {
        return availableFlights;
    }

    public void setAvailableFlights(Collection<Flight> availableFlights) {
        this.availableFlights = availableFlights;
    }

    public Collection<FlightConnection> getAvailableConnections() {
        return availableConnections;
    }

    public void setAvailableConnections(Collection<FlightConnection> availableConnections) {
        this.availableConnections = availableConnections;
    }


    public Flight getCurrentFlight() {
        return currentFlight;
    }

    public void setCurrentFlight(Flight currentFlight) {
        this.currentFlight = currentFlight;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getMessageID() {
        return messageID;
    }

    public void setMessageID(int messageID) {
        this.messageID = messageID;
    }

    @Override
    public String toString() {
        return "AirlineDTO{" +
                "availableFlights=" + availableFlights +
                ", availableConnections=" + availableConnections +
                ", currentFlight=" + currentFlight +
                ", status=" + status +
                '}';
    }
}
