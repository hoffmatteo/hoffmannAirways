package com.oth.sw.hoffmannairways.dto;

import com.oth.sw.hoffmannairways.entity.Flight;
import com.oth.sw.hoffmannairways.entity.FlightConnection;
import com.oth.sw.hoffmannairways.entity.Order;

import java.util.Collection;

public class AirlineDTO {
    //TODO split up in two queues?
    Collection<Flight> availableFlights;
    Collection<FlightConnection> availableConnections;
    Order currentOrder;
    Flight currentFlight; //contains updated information
    Status status;

    public enum Status {
        CANCELLED,
        CHANGED,
        CONFIRMED,
        INFO //if only information was requested
    }

    public AirlineDTO() {

    }

    public AirlineDTO(Collection<Flight> availableFlights, Collection<FlightConnection> availableConnections, Status status) {
        this.availableFlights = availableFlights;
        this.availableConnections = availableConnections;
        this.status = status;
    }

    public AirlineDTO(Order currentOrder, Flight currentFlight, Status status) {
        this.currentOrder = currentOrder;
        this.currentFlight = currentFlight;
        this.status = status;
    }

    public AirlineDTO(Order currentOrder, Status status) {
        this.currentOrder = currentOrder;
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

    public Order getCurrentOrder() {
        return currentOrder;
    }

    public void setCurrentOrder(Order currentOrder) {
        this.currentOrder = currentOrder;
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
}
