package com.oth.sw.hoffmannairways.service.impl;

import com.oth.sw.hoffmannairways.dto.AirlineDTO;
import com.oth.sw.hoffmannairways.entity.Airplane;
import com.oth.sw.hoffmannairways.entity.Flight;
import com.oth.sw.hoffmannairways.entity.FlightConnection;
import com.oth.sw.hoffmannairways.entity.Order;
import com.oth.sw.hoffmannairways.entity.util.AccountType;
import com.oth.sw.hoffmannairways.queue.QueueController;
import com.oth.sw.hoffmannairways.repository.FlightConnectionRepo;
import com.oth.sw.hoffmannairways.repository.FlightRepository;
import com.oth.sw.hoffmannairways.repository.OrderRepository;
import com.oth.sw.hoffmannairways.service.AirplaneServiceIF;
import com.oth.sw.hoffmannairways.service.FlightServiceIF;
import com.oth.sw.hoffmannairways.service.exception.AirplaneException;
import com.oth.sw.hoffmannairways.service.exception.FlightException;
import com.oth.sw.hoffmannairways.web.rest.tempAirportIF;
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

    @Autowired
    QueueController queueController;

    @Autowired
    tempAirportIF airportService;


    @Transactional
    public Flight createFlight(Flight flight) throws FlightException {
        try {
            Airplane plane = airplaneService.assignPlane(flight);
            Optional<Flight> overlappingFlightOption = flightRepo.findFlightByAirplane_PlaneID(plane.getPlaneID());
            if (overlappingFlightOption.isPresent()) {
                Flight overlappingFlight = overlappingFlightOption.get();
                if (overlappingFlight.getFlightID() != flight.getFlightID()) {
                    overlappingFlight.setAirplane(null);
                }
            }
            Flight savedFlight = flightRepo.save(flight);
            Flight confirmedFlight = airportService.createFlight(savedFlight);
            return flightRepo.save(confirmedFlight);
        } catch (AirplaneException e) {
            throw new FlightException(e.getMessage(), flight);
        }
    }

    //TODO implement
    @Transactional
    public void deleteFlight(Flight flight) throws FlightException {
        if (flight.getDepartureTime().before(new Date()) && flight.getArrivalTime().after(new Date())) {
            throw new FlightException("Can not delete flight that is currently in transit.", flight);
        }
        try {
            List<Order> orders = orderRepository.findOrdersByFlight_FlightID(flight.getFlightID());
            notifyCustomer(flight, AirlineDTO.Status.CANCELLED);
            orderRepository.deleteAll(orders);
            //TODO notify airport here if necessary
            flight.getAirplane().setUnavailableUntil(null);
            flightRepo.delete(flight);
        } catch (IllegalArgumentException e) {
            throw new FlightException("Could not delete orders and flight", flight);
        }
    }


    //TODO
    @Transactional
    public Flight editFlight(Flight flight) throws FlightException {
        Flight oldFlight = flightRepo.findById(flight.getFlightID()).orElseThrow(() -> new FlightException("Could not find flight to edit!", flight));
        Flight savedFlight = flightRepo.save(flight);
        notifyCustomer(savedFlight, AirlineDTO.Status.CHANGED);
        return savedFlight;
    }

    private void notifyCustomer(Flight flight, AirlineDTO.Status status) {
        List<Order> orders = orderRepository.findOrdersByFlight_FlightID(flight.getFlightID());
        for (Order order : orders) {
            if (order.getCustomer().isSendNotification()) {
                //TODO does order already contain new information? --> probably depends on if save is called before or after!
                System.out.println("Order flight: " + order.getFlight());
                System.out.println("New flight: " + flight);

                AirlineDTO dto = new AirlineDTO(order, flight, status);
                queueController.sendDTO(dto);
            }
        }
    }

    @Transactional
    public Order bookFlight(Order order) throws FlightException {
        //TODO customer checking
        Flight flight = flightRepo.findById(order.getFlight().getFlightID()).orElseThrow(() -> new FlightException("Could not find flight.", order.getFlight()));
        int maxSeats = flight.getAirplane().getTotalSeats();
        double maxCargo = flight.getAirplane().getMaxCargo();
        int totalBookedSeats = flight.getBookedSeats() + order.getTotalSeats();
        double totalBookedCargo = flight.getBookedCargoInKg() + order.getTotalCargoInKg();
        if (totalBookedSeats <= maxSeats && totalBookedCargo <= maxCargo) {
            flight.setBookedSeats(totalBookedSeats);
            flight.setBookedCargoInKg(totalBookedCargo);
            flightRepo.save(flight);
            Order savedOrder = orderRepository.save(order);
            //TODO remove
            if (order.getCustomer() != null) {
                if (order.getCustomer().getAccountType() == AccountType.STAFF) {
                    queueController.bookAsPartner(savedOrder);
                }
            }
            return savedOrder;

        } else {
            throw new FlightException("Could not book flight as there are is no more space available.", flight);
        }

    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public List<Order> getAllPastOrders() {
        return orderRepository.findOrdersByFlight_DepartureTimeBeforeOrderByFlight_DepartureTime(new Date());

    }

    @Override
    public List<Order> getAllFutureOrders() {
        return orderRepository.findOrdersByFlight_DepartureTimeAfterOrderByFlight_DepartureTime(new Date());
    }

    @Override
    public List<Order> getAllPastOrders(String username) {
        return orderRepository.findOrdersByCustomer_UsernameAndFlight_DepartureTimeBeforeOrderByFlight_DepartureTime(username, new Date());
    }

    @Override
    public List<Order> getAllFutureOrders(String username) {
        return orderRepository.findOrdersByCustomer_UsernameAndFlight_DepartureTimeAfterOrderByFlight_DepartureTime(username, new Date());
    }

    //TODO change name
    @Override
    public List<Flight> listAllFlights() {
        return flightRepo.getAllByDepartureTimeAfterOrderByDepartureTime(new Date());
    }

    @Override
    public List<FlightConnection> listAllFlightConnections() {
        return flightConnectionRepo.findAll();
    }

    @Override
    public List<Flight> getFlightsForConnection(FlightConnection conn) {
        return flightRepo.getAllByConnection(conn);

    }

    @Override
    @Transactional
    public FlightConnection createFlightConnection(FlightConnection flightConnection) {
        return flightConnectionRepo.save(flightConnection);
    }

    @Override
    @Transactional
    public Airplane repairPlane(Airplane plane) throws FlightException, AirplaneException {
        Optional<Flight> flightOptional = flightRepo.findFlightByAirplane_PlaneID(plane.getPlaneID());
        if (flightOptional.isPresent()) {
            deleteFlight(flightOptional.get());
        }
        return airplaneService.repairPlane(plane);

    }

    @Override
    public Flight getFlight(int flightID) throws FlightException {
        Optional<Flight> flightOption = flightRepo.findById(flightID);
        return flightOption.orElseThrow(() -> new FlightException("Could not find flight", new Flight(flightID)));
    }


}
