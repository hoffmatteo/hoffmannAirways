package com.oth.sw.hoffmannairways.web.queue;

import com.oth.sw.hoffmannairways.dto.*;
import com.oth.sw.hoffmannairways.entity.*;
import com.oth.sw.hoffmannairways.service.AirplaneServiceIF;
import com.oth.sw.hoffmannairways.service.FlightServiceIF;
import com.oth.sw.hoffmannairways.service.UserServiceIF;
import com.oth.sw.hoffmannairways.service.exception.AirplaneException;
import com.oth.sw.hoffmannairways.service.exception.FlightException;
import com.oth.sw.hoffmannairways.service.exception.UserException;
import com.oth.sw.hoffmannairways.util.logger.LoggerIF;
import de.othr.sw.HaberlRepairs.entity.dto.RepairOrderDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.jms.JmsException;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Scope("singleton")
public class QueueController {

    @Autowired
    private FlightServiceIF flightService;

    @Autowired
    private UserServiceIF userService;

    @Autowired
    private AirplaneServiceIF planeService;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    @Qualifier("QueueLogger")
    private LoggerIF queueLogger;

    @Autowired
    @Qualifier("ErrorLogger")
    private LoggerIF errorLogger;

    @Autowired
    @Qualifier("SuccessLogger")
    private LoggerIF successLogger;


    @JmsListener(destination = "sw_matteo_hoffmann_queue_Customer")
    public void receiveMessage(CustomerDTO message) {
        queueLogger.log("QueueController", "Received message from DaumCompany: " + message);
        AirlineDTO dto = new AirlineDTO(AirlineDTO.Status.ERROR);

        if (message.getUserInfo() != null) {
            UserDTO info = message.getUserInfo();
            try {
                User registeredUser = userService.getUserByUsername(info.getUsername());
                if (userService.checkPassword(info.getPassword(), registeredUser)) {
                    switch (message.getMessage()) {
                        case CREATE_ORDER -> dto = createBooking(message, registeredUser);
                        case UPDATE_CONNECTIONS -> dto = listConnections();
                        case UPDATE_FLIGHTS -> dto = listFlightsForConnection(message);
                    }
                }
            } catch (UserException e) {
                errorLogger.log("QueueController", "Could not authenticate Queue Partner!");
            }
        }
        if (message.getMessageID() != 0) {
            dto.setMessageID(message.getMessageID());
        }
        sendDTO(dto);

    }

    private AirlineDTO createBooking(CustomerDTO message, User registeredUser) {
        AirlineDTO dto = new AirlineDTO();
        dto.setMessageID(message.getMessageID());
        OrderDTO orderDTO = message.getOrder();
        FlightDTO flightDTO = message.getOrder().getFlight();
        Flight flight = new Flight(flightDTO.getFlightID());
        if (orderDTO != null) {
            try {
                Order order = new Order(orderDTO.getTotalSeats(), orderDTO.getTotalCargoInKg(), flight, registeredUser);
                Order completedOrder = flightService.bookFlight(order);
                dto.setCurrentFlight(createFlightDTO(completedOrder.getFlight()));
                dto.setStatus(AirlineDTO.Status.CONFIRMED);
                successLogger.log("QueueController", "Successfully created booking for DaumCompany");
            } catch (FlightException e) {
                errorLogger.log("QueueController", "Could not create booking for DaumCompany");
                dto.setStatus(AirlineDTO.Status.ERROR);
            }
        }
        return dto;
    }

    private AirlineDTO listConnections() {
        AirlineDTO dto = new AirlineDTO();

        List<FlightConnection> connections = flightService.listAllFlightConnections();
        if (connections.isEmpty()) {
            errorLogger.log("QueueController", "Could not list connections for DaumCompany, list is empty");
            dto.setStatus(AirlineDTO.Status.ERROR);

        } else {
            dto.setAvailableConnections(connections);
            dto.setStatus(AirlineDTO.Status.INFO_CONNECTIONS);
        }
        return dto;
    }

    private AirlineDTO listFlightsForConnection(CustomerDTO message) {

        AirlineDTO dto = new AirlineDTO();
        if (message.getConnection() == null) {
            dto.setStatus(AirlineDTO.Status.ERROR);
            errorLogger.log("QueueController", "Could not list flights for DaumCompany, connection is null");
        } else {
            List<FlightDTO> flights = createFlightDTOs(flightService.getFlightsForConnection(message.getConnection()));
            if (flights.isEmpty()) {
                dto.setStatus(AirlineDTO.Status.ERROR);
                errorLogger.log("QueueController", "Could not list flights for DaumCompany, list is empty");
            } else {
                dto.setAvailableFlights(flights);
                dto.setStatus(AirlineDTO.Status.INFO_FLIGHTS);
            }
        }
        return dto;
    }

    public List<FlightDTO> createFlightDTOs(List<Flight> flights) {
        List<FlightDTO> dtoList = new ArrayList<>();
        for (Flight f : flights) {
            dtoList.add(createFlightDTO(f));
        }
        return dtoList;
    }

    public FlightDTO createFlightDTO(Flight f) {
        return new FlightDTO(f.getFlightID(), f.getDepartureTime(), f.getArrivalTime(), f.getBookedSeats(), f.getBookedCargoInKg());
    }


    public void sendDTO(AirlineDTO dto) {
        try {
            jmsTemplate.convertAndSend("sw_matteo_hoffmann_queue_Airline", dto);
            queueLogger.log("QueueController", "Sending message to DaumCompany " + dto);
        } catch (JmsException e) {
            errorLogger.log("QueueController", "Could not send message to DaumCompany " + e.getMessage());
        }
    }


    public void requestRepairJob(Airplane plane) {
        RepairOrderDTO dto = new RepairOrderDTO(plane.getPlaneID(), plane.getUnavailableUntil(), "New request from HoffmannAirways", new de.othr.sw.HaberlRepairs.entity.dto.CustomerDTO("test", "test"), plane.getIssues());
        try {
            jmsTemplate.convertAndSend("sw_simon_haberl_queue_RepairOrderInquiry", dto);
            queueLogger.log("QueueController", "Sending message to HaberlRepairs " + dto);
        } catch (JmsException e) {
            errorLogger.log("QueueController", "Could not send message to HaberlRepairs " + e.getMessage());

        }
    }

    @JmsListener(destination = "sw_simon_haberl_queue_RepairOrderReply")
    public void receiveRepairMessage(RepairOrderDTO message) {
        queueLogger.log("QueueController", "Received from HaberlRepairs " + message);
        if (message.isAccepted()) {
            if (message.getPickUpDate() != null && message.getPlaneId() != 0) {
                try {
                    planeService.updateUnavailable(message.getPickUpDate(), message.getPlaneId());
                } catch (AirplaneException e) {
                    errorLogger.log("QueueController", "Could not update unavailability " + e.getMessage());
                }
            }
        }
    }


}
