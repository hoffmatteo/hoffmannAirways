package com.oth.sw.hoffmannairways.service.impl;

import com.oth.sw.hoffmannairways.entity.Airplane;
import com.oth.sw.hoffmannairways.entity.Flight;
import com.oth.sw.hoffmannairways.entity.FlightConnection;
import com.oth.sw.hoffmannairways.entity.Order;
import com.oth.sw.hoffmannairways.repository.FlightConnectionRepo;
import com.oth.sw.hoffmannairways.repository.FlightRepository;
import com.oth.sw.hoffmannairways.repository.OrderRepository;
import com.oth.sw.hoffmannairways.service.AirplaneServiceIF;
import com.oth.sw.hoffmannairways.service.FlightServiceIF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class FlightService implements FlightServiceIF {
    @Autowired
    FlightRepository flightRepo;
    @Autowired
    FlightConnectionRepo flightConnectionRepo;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    AirplaneServiceIF airplaneService;


    @Transactional
    public Flight createFlight(Flight flight) {
        //TODO
        if (airplaneService.assignPlane(flight) != null) {
            Flight overlappingFlight = flightRepo.findFlightByAirplane_PlaneID(flight.getAirplane().getPlaneID());
            if (overlappingFlight != null) {
                overlappingFlight.setAirplane(null);
            }
            Airplane plane = airplaneService.assignPlane(flight);
            //TODO necessary? I already save the new information in airplaneService --> look at Cascade
            flight.setAirplane(plane);
            //TODO here: call airport
        } else {
            return null;
        }
        return flightRepo.save(flight);
    }

    //TODO change diagram
    //TODO maybe DTO? But only for rest?
    @Transactional
    public void deleteFlight(Flight flight) {
        List<Order> orders = orderRepository.findOrdersByFlight_FlightID(flight.getFlightID());
        orderRepository.deleteAll(orders);
        //TODO notify customer here
        //TODO notify airport here if necessary
        flightRepo.delete(flight);


    }

    public void deleteFlight(int flightID) {
        ////orderRepository.deleteAll(orders);
        //TODO notify customer here
        //TODO notify airport here if necessary
        Flight flight = flightRepo.findById(flightID).get();
        flightRepo.delete(flight);


    }


    //TODO
    @Transactional
    public Flight editFlight(Flight flight) {
        Optional<Flight> oldFlightOption = flightRepo.findById(flight.getFlightID());
        if (oldFlightOption.isPresent()) {
            Flight oldFlight = oldFlightOption.get();
            deleteFlight(oldFlight);
            createFlight(flight);
            return flight;
        } else {
            return null;
        }
    }

    @Transactional

    public Flight bookFlight(Order order) {
        //TODO customer checking
        Optional<Flight> flightOption = flightRepo.findById(order.getFlight().getFlightID());
        if (flightOption.isPresent()) {
            Flight flight = flightOption.get();
            //TODO call airplanerepo here?
            int maxSeats = flight.getAirplane().getTotalSeats();
            double maxCargo = flight.getAirplane().getMaxCargo();
            int totalBookedSeats = flight.getBookedSeats() + order.getTotalSeats();
            double totalBookedCargo = flight.getBookedCargoInKg() + order.getTotalCargoInKg();
            if (totalBookedSeats <= maxSeats && totalBookedCargo <= maxCargo) {
                flight.setBookedSeats(totalBookedSeats);
                flight.setBookedCargoInKg(totalBookedCargo);
                orderRepository.save(order);
                flightRepo.save(flight);
                return flight;

            }
        }
        //TODO throw exception
        return null;


    }

    public List<Flight> listAllFlights() {
        return flightRepo.getAllByDepartureTimeAfter(new Date());
    }

    public List<FlightConnection> listAllFlightConnections() {
        return flightConnectionRepo.findAll();
    }

    public List<Flight> getFlightsForConnection(FlightConnection conn) {
        return flightRepo.getAllByConnection(conn);

    }

    public FlightConnection createFlightConnection(FlightConnection flightConnection) {
        return flightConnectionRepo.save(flightConnection);
    }

    public void repairPlane(Airplane plane) {
        Flight flight = flightRepo.findFlightByAirplane_PlaneID(plane.getPlaneID());
        deleteFlight(flight);
        airplaneService.repairPlane(plane);
    }


}