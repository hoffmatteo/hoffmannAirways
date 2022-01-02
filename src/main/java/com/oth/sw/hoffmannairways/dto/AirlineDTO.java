package com.oth.sw.hoffmannairways.dto;

import com.oth.sw.hoffmannairways.entity.Flight;
import com.oth.sw.hoffmannairways.entity.FlightConnection;
import com.oth.sw.hoffmannairways.entity.Order;

import java.util.Collection;

public class AirlineDTO {
    Collection<Flight> availableFlights;
    Collection<FlightConnection> availableConnections;
    Order currentOrder;
    Status status;

    enum Status {
        CANCELLED,
        CHANGED,
        CONFIRMED,
        INFO //if only information was requested
    }
}
