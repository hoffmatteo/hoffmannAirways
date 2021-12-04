package com.oth.sw.hoffmannairways.service;

import com.oth.sw.hoffmannairways.entity.Flight;
import com.oth.sw.hoffmannairways.entity.FlightConnection;
import com.oth.sw.hoffmannairways.entity.Order;

import java.util.List;

public interface FlightServiceIF {

    Flight createFlight(Flight flight);

    void deleteFlight(Flight flight);

    Flight editFlight(Flight flight);

    Flight bookFlight(Order order);

    List<Flight> listAllFlights();

    List<Flight> getFlightsForConnection(FlightConnection connection);

    List<FlightConnection> listAllFlightConnections();


}
