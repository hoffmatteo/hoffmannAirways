package com.oth.sw.hoffmannairways.service;

import com.oth.sw.hoffmannairways.entity.Airplane;
import com.oth.sw.hoffmannairways.entity.Flight;
import com.oth.sw.hoffmannairways.entity.FlightConnection;
import com.oth.sw.hoffmannairways.entity.Order;
import com.oth.sw.hoffmannairways.service.exception.FlightException;

import java.util.List;

public interface FlightServiceIF {
    FlightConnection createFlightConnection(FlightConnection flightConnection);

    Airplane repairPlane(Airplane plane) throws FlightException;

    Flight getFlight(int flightID) throws FlightException;

    Flight createFlight(Flight flight) throws FlightException;

    void deleteFlight(Flight flight) throws FlightException;

    Flight editFlight(Flight flight) throws FlightException;

    Order bookFlight(Order order) throws FlightException;

    List<Order> getAllOrders() throws FlightException;

    List<Order> getAllPastOrders(String username) throws FlightException;

    List<Order> getAllFutureOrders(String username) throws FlightException;

    List<Order> getAllPastOrders() throws FlightException;

    List<Order> getAllFutureOrders() throws FlightException;

    List<Flight> listAllFlights() throws FlightException;

    List<Flight> getFlightsForConnection(FlightConnection connection) throws FlightException;

    List<FlightConnection> listAllFlightConnections() throws FlightException;


}
