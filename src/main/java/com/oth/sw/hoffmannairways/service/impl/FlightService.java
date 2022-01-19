package com.oth.sw.hoffmannairways.service.impl;

import com.oth.sw.hoffmannairways.dto.AirlineDTO;
import com.oth.sw.hoffmannairways.entity.Airplane;
import com.oth.sw.hoffmannairways.entity.Flight;
import com.oth.sw.hoffmannairways.entity.FlightConnection;
import com.oth.sw.hoffmannairways.entity.Order;
import com.oth.sw.hoffmannairways.entity.util.AccountType;
import com.oth.sw.hoffmannairways.repository.FlightConnectionRepo;
import com.oth.sw.hoffmannairways.repository.FlightRepository;
import com.oth.sw.hoffmannairways.repository.OrderRepository;
import com.oth.sw.hoffmannairways.service.AirplaneServiceIF;
import com.oth.sw.hoffmannairways.service.FlightServiceIF;
import com.oth.sw.hoffmannairways.service.exception.AirplaneException;
import com.oth.sw.hoffmannairways.service.exception.FlightException;
import com.oth.sw.hoffmannairways.web.queue.QueueController;
import com.oth.sw.hoffmannairways.web.rest.tempAirportIF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionInterceptor;

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
        //TODO think about changing to proxy airport when exception occurs?
        try {
            Flight confirmedFlight = airportService.createFlight(flight);
            Airplane plane = airplaneService.assignPlane(confirmedFlight);
            return flightRepo.save(confirmedFlight);
        } catch (AirplaneException e) {
            throw new FlightException(e.getMessage(), flight);
        }
    }

    @Transactional
    public void deleteFlight(Flight flight) throws FlightException {
        //TODO check what happens when errors occur!
        if (flight.getDepartureTime().before(new Date()) && flight.getArrivalTime().after(new Date())) {
            throw new FlightException("Can not delete flight that is currently in transit.", flight);
        }
        try {
            List<Order> orders = orderRepository.findOrdersByFlight_FlightID(flight.getFlightID());
            //TODO return value?
            airportService.cancelFlight(flight);
            if (flight.getArrivalTime() == flight.getAirplane().getUnavailableUntil()) {
                flight.getAirplane().setUnavailableUntil(null);
            }
            notifyCustomer(flight, AirlineDTO.Status.CANCELLED);
            orderRepository.deleteAll(orders);
            flightRepo.delete(flight);
        } catch (IllegalArgumentException e) {
            throw new FlightException("Could not delete orders and flight", flight);
        }
    }


    @Transactional
    public Flight editFlight(Flight flight) throws FlightException {
        if (flight.getDepartureTime().before(new Date()) && flight.getArrivalTime().after(new Date())) {
            throw new FlightException("Can not edit flight that is currently in transit.", flight);
        } else {

            Flight oldFlight = flightRepo.findById(flight.getFlightID()).orElseThrow(() -> new FlightException("Could not find flight to edit!", flight));
            System.out.println(oldFlight.getDepartureTime());
            try {

                Flight confirmedFlight = airportService.editFlight(oldFlight, flight);
                System.out.println(confirmedFlight.getDepartureTime());

                flightRepo.save(confirmedFlight);

                notifyCustomer(confirmedFlight, AirlineDTO.Status.CHANGED);

                return confirmedFlight;

            } catch (FlightException e) {
                TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
                throw e;

            }
        }


    }

    private void notifyCustomer(Flight flight, AirlineDTO.Status status) {
        List<Order> orders = orderRepository.findOrdersByFlight_FlightID(flight.getFlightID());
        for (Order order : orders) {
            if (order.getCustomer().isSendNotification()) {
                AirlineDTO dto = new AirlineDTO(flight, status);
                queueController.sendDTO(dto);
                break;
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
            if (savedOrder.getCustomer() != null) {
                if (savedOrder.getCustomer().getAccountType() == AccountType.STAFF) {
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
        return flightConnectionRepo.findAllByOrderByDepartureAirport();
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


        List<Flight> flightList = flightRepo.findFlightsByAirplane_PlaneID(plane.getPlaneID());
        for (Flight flight : flightList) {
            deleteFlight(flight);
        }
        return airplaneService.repairPlane(plane);

    }

    @Override
    public Flight getFlight(int flightID) throws FlightException {
        Optional<Flight> flightOption = flightRepo.findById(flightID);
        return flightOption.orElseThrow(() -> new FlightException("Could not find flight", new Flight(flightID)));
    }


}
