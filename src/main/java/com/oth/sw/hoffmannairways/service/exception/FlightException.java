package com.oth.sw.hoffmannairways.service.exception;

import com.oth.sw.hoffmannairways.entity.Flight;

public class FlightException extends Exception {
    private Flight flight;


    public FlightException(String message, Flight flight) {
        super(message);
        this.flight = flight;
    }

    public FlightException(String message) {
        super(message);
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }
}
