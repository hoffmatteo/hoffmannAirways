package com.oth.sw.hoffmannairways.service.impl;

import com.oth.sw.hoffmannairways.dto.AirlineDTO;
import com.oth.sw.hoffmannairways.dto.FlightDTO;
import com.oth.sw.hoffmannairways.entity.Airplane;
import com.oth.sw.hoffmannairways.entity.Flight;
import com.oth.sw.hoffmannairways.entity.FlightConnection;
import com.oth.sw.hoffmannairways.entity.Order;
import com.oth.sw.hoffmannairways.repository.FlightConnectionRepo;
import com.oth.sw.hoffmannairways.repository.FlightRepository;
import com.oth.sw.hoffmannairways.repository.OrderRepository;
import com.oth.sw.hoffmannairways.service.AirplaneServiceIF;
import com.oth.sw.hoffmannairways.service.FlightServiceIF;
import com.oth.sw.hoffmannairways.service.exception.AirplaneException;
import com.oth.sw.hoffmannairways.service.exception.FlightException;
import com.oth.sw.hoffmannairways.util.logger.LoggerIF;
import com.oth.sw.hoffmannairways.web.queue.QueueController;
import com.oth.sw.hoffmannairways.web.rest.AirportServiceIF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

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
    AirportServiceIF airportService;

    @Autowired
    @Qualifier("DatabaseLogger")
    LoggerIF databaseLogger;


    @Transactional(Transactional.TxType.REQUIRED)
    public Flight createFlight(Flight flight) throws FlightException {
        try {
            Flight confirmedFlight = airportService.createFlight(flight);
            Airplane plane = airplaneService.assignPlane(confirmedFlight);
            databaseLogger.log("FlightService", "Creating flight " + flight);
            return flightRepo.save(confirmedFlight);
        } catch (AirplaneException e) {
            throw new FlightException(e.getMessage(), flight);
        }
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public void deleteFlight(Flight flight) throws FlightException {
        if (flight.getDepartureTime().before(new Date()) && flight.getArrivalTime().after(new Date())) {
            throw new FlightException("Can not delete flight that is currently in transit.", flight);
        }
        try {
            List<Order> orders = orderRepository.findOrdersByFlight_FlightID(flight.getFlightID());
            airportService.cancelFlight(flight);
            if (flight.getArrivalTime() == flight.getAirplane().getUnavailableUntil()) {
                airplaneService.updateUnavailable(new Date(), flight.getAirplane().getPlaneID());
            }
            notifyCustomer(flight, AirlineDTO.Status.CANCELLED);
            orderRepository.deleteAll(orders);
            flightRepo.delete(flight);
            databaseLogger.log("FlightService", "Deleting flight " + flight);
        } catch (IllegalArgumentException e) {
            throw new FlightException("Could not delete orders and flight", flight);
        } catch (AirplaneException e) {
            throw new FlightException("Could not update plane's availability", flight);
        }
    }


    @Transactional(Transactional.TxType.REQUIRED)
    public Flight editFlight(Flight flight) throws FlightException {
        if (flight.getDepartureTime().before(new Date()) && flight.getArrivalTime().after(new Date())) {
            throw new FlightException("Can not edit flight that is currently in transit.", flight);
        } else {

            Flight oldFlight = flightRepo.findById(flight.getFlightID()).orElseThrow(() -> new FlightException("Could not find flight to edit!", flight));
            try {

                Flight confirmedFlight = airportService.editFlight(oldFlight, flight);

                flightRepo.save(confirmedFlight);
                databaseLogger.log("FlightService", "Updated flight " + flight);

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
                FlightDTO flightDTO = new FlightDTO(flight.getFlightID(), flight.getDepartureTime(), flight.getArrivalTime(), flight.getBookedSeats(), flight.getBookedCargoInKg());
                AirlineDTO dto = new AirlineDTO(flightDTO, status);
                queueController.sendDTO(dto);
                break;
            }
        }
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public Order bookFlight(Order order) throws FlightException {
        Flight flight = flightRepo.findById(order.getFlight().getFlightID()).orElseThrow(() -> new FlightException("Could not find flight.", order.getFlight()));
        int maxSeats = flight.getAirplane().getTotalSeats();
        double maxCargo = flight.getAirplane().getMaxCargo();
        int totalBookedSeats = flight.getBookedSeats() + order.getTotalSeats();
        double totalBookedCargo = flight.getBookedCargoInKg() + order.getTotalCargoInKg();
        if (totalBookedSeats <= maxSeats && totalBookedCargo <= maxCargo) {
            flight.setBookedSeats(totalBookedSeats);
            flight.setBookedCargoInKg(totalBookedCargo);
            flightRepo.save(flight);
            databaseLogger.log("FlightService", "Successfully created order " + order);
            return orderRepository.save(order);


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
    @Transactional(Transactional.TxType.REQUIRED)
    public FlightConnection createFlightConnection(FlightConnection flightConnection) {
        return flightConnectionRepo.save(flightConnection);
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public Airplane repairPlane(Airplane plane) throws FlightException, AirplaneException {

        List<Flight> flightList = flightRepo.findFlightsByAirplane_PlaneID(plane.getPlaneID());
        for (Flight flight : flightList) {
            databaseLogger.log("FlightService", "Deleting flight because plane has to be repaired " + flight);
            deleteFlight(flight);
        }
        return airplaneService.repairPlane(plane);

    }

    @Override
    public Flight getFlight(int flightID) throws FlightException {
        Flight flight = flightRepo.findById(flightID).orElseThrow(() -> new FlightException("Could not find flight", new Flight(flightID)));
        databaseLogger.log("FlightService", "Returning flight " + flight);
        return flight;

    }


}
