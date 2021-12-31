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
        Airplane plane = airplaneService.assignPlane(flight);
        if (plane != null) {
            Flight overlappingFlight = flightRepo.findFlightByAirplane_PlaneID(flight.getAirplane().getPlaneID());
            if (overlappingFlight != null && overlappingFlight.getFlightID() != flight.getFlightID()) {
                overlappingFlight.setAirplane(null);
            }
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




    //TODO
    @Transactional
    public Flight editFlight(Flight flight) {
        Optional<Flight> oldFlightOption = flightRepo.findById(flight.getFlightID());
        if (oldFlightOption.isPresent()) {
            Flight oldFlight = oldFlightOption.get();
            //TODO notify customers, call airport etc.
            Flight savedFlight = flightRepo.save(flight);
            return savedFlight;
        } else {
            System.out.println("Flight not found " + flight.getFlightID());
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
                return flight;

            }
        }
        //TODO throw exception
        return null;


    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public List<Order> getAllPastOrders(String username) {
        return orderRepository.findOrdersByCustomer_UsernameAndFlight_DepartureTimeBeforeOrderByFlight_DepartureTime(username, new Date());
    }

    public List<Order> getAllFutureOrders(String username) {
        return orderRepository.findOrdersByCustomer_UsernameAndFlight_DepartureTimeAfterOrderByFlight_DepartureTime(username, new Date());
    }

//TODO change name
    public List<Flight> listAllFlights() {
        return flightRepo.getAllByDepartureTimeAfterOrderByDepartureTime(new Date());
    }

    public List<FlightConnection> listAllFlightConnections() {
        return flightConnectionRepo.findAll();
    }

    public List<Flight> getFlightsForConnection(FlightConnection conn) {
        return flightRepo.getAllByConnection(conn);

    }
    @Transactional
    public FlightConnection createFlightConnection(FlightConnection flightConnection) {
        return flightConnectionRepo.save(flightConnection);
    }
    @Transactional
    public Airplane repairPlane(Airplane plane) {
        Flight flight = flightRepo.findFlightByAirplane_PlaneID(plane.getPlaneID());
        //TODO
        //überlegen: falls deadline vor start des flugs --> nicht löschen?
        if(flight != null) {
            deleteFlight(flight);
        }
        return airplaneService.repairPlane(plane);
    }

    public Flight getFlight(int flightID) {
        Optional<Flight> flightOption = flightRepo.findById(flightID);
        return flightOption.orElse(null);
        //TODO
    }



}
