package com.oth.sw.hoffmannairways.service;

import com.oth.sw.hoffmannairways.entity.Airplane;
import com.oth.sw.hoffmannairways.entity.Flight;
import com.oth.sw.hoffmannairways.entity.FlightConnection;
import com.oth.sw.hoffmannairways.entity.Order;
import com.oth.sw.hoffmannairways.service.exception.AirplaneException;
import com.oth.sw.hoffmannairways.service.exception.FlightException;

import java.util.List;

public interface FlightServiceIF {
    FlightConnection createFlightConnection(FlightConnection flightConnection);

    Airplane repairPlane(Airplane plane) throws FlightException, AirplaneException;

    Flight getFlight(int flightID) throws FlightException;

    Flight createFlight(Flight flight) throws FlightException;

    void deleteFlight(Flight flight) throws FlightException;

    Flight editFlight(Flight flight) throws FlightException;

    Order bookFlight(Order order) throws FlightException;

    List<Order> getAllOrders();

    List<Order> getAllPastOrders(String username);

    List<Order> getAllFutureOrders(String username);

    List<Order> getAllPastOrders();

    List<Order> getAllFutureOrders();

    List<Flight> listAllFlights();

    List<Flight> getFlightsForConnection(FlightConnection connection);

    List<FlightConnection> listAllFlightConnections();


}
