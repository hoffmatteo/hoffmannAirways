package com.oth.sw.hoffmannairways.web.queue;

import com.oth.sw.hoffmannairways.dto.*;
import com.oth.sw.hoffmannairways.entity.*;
import com.oth.sw.hoffmannairways.service.AirplaneServiceIF;
import com.oth.sw.hoffmannairways.service.FlightServiceIF;
import com.oth.sw.hoffmannairways.service.UserServiceIF;
import com.oth.sw.hoffmannairways.service.exception.AirplaneException;
import com.oth.sw.hoffmannairways.service.exception.FlightException;
import com.oth.sw.hoffmannairways.service.exception.UserException;
import de.othr.sw.HaberlRepairs.entity.dto.RepairOrderDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.jms.JmsException;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

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
    JmsTemplate jmsTemplate;


    @JmsListener(destination = "sw_matteo_hoffmann_queue_Customer")
    public void receiveMessage(CustomerDTO message) {
        System.out.println("Received from customer: " + message);
        AirlineDTO dto = new AirlineDTO(AirlineDTO.Status.ERROR);

        if (message.getUserInfo() != null) {
            UserDTO info = message.getUserInfo();
            try {
                //TODO switch
                User registeredUser = userService.getUserByUsername(info.getUsername());
                if (userService.checkPassword(info.getPassword(), registeredUser)) {
                    switch (message.getMessage()) {
                        case CREATE_ORDER -> dto = createBooking(message, registeredUser);
                        case UPDATE_CONNECTIONS -> dto = listConnections();
                        case UPDATE_FLIGHTS -> dto = listFlightsForConnection(message.getConnection());
                    }
                }
            } catch (UserException e) {
                System.out.println("Could not authenticate queue partner!");
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
        Flight flight = new Flight(flightDTO.getFlightID(), );
        if (orderDTO != null) {
            try {
                Order order = new Order(orderDTO.getTotalSeats(), orderDTO.getTotalCargoInKg(), orderDTO.getFlight(), registeredUser);
                Order completedOrder = flightService.bookFlight(order);
                dto.setStatus(AirlineDTO.Status.CONFIRMED);
            } catch (FlightException e) {
                dto.setStatus(AirlineDTO.Status.ERROR);
            }
        }
        return dto;
    }

    private AirlineDTO listConnections() {
        AirlineDTO dto = new AirlineDTO();

        List<FlightConnection> connections = flightService.listAllFlightConnections();
        if (connections.isEmpty()) {
            dto.setStatus(AirlineDTO.Status.ERROR);

        } else {
            dto.setAvailableConnections(connections);
            dto.setStatus(AirlineDTO.Status.INFO_CONNECTIONS);
        }
        return dto;
    }

    private AirlineDTO listFlightsForConnection(FlightConnection conn) {
        AirlineDTO dto = new AirlineDTO();
        List<Flight> flights = flightService.getFlightsForConnection(conn);
        if (flights.isEmpty()) {
            dto.setStatus(AirlineDTO.Status.ERROR);
        } else {
            dto.setAvailableFlights(flights);
            dto.setStatus(AirlineDTO.Status.INFO_FLIGHTS);
        }
        return dto;
    }


    public void sendDTO(AirlineDTO dto) {
        System.out.println("Sending message to Customer: " + dto);
        try {
            jmsTemplate.convertAndSend("sw_matteo_hoffmann_queue_Airline", dto);
        } catch (JmsException e) {
            System.out.println(e.getMessage());
        }
    }


    public void bookAsPartner(Order order) {
        CustomerDTO dto = new CustomerDTO(CustomerDTO.Message.CREATE_ORDER, order);
        dto.setUserInfo(new UserDTO("daumen", "123"));
        System.out.println("Sending message to Airline: " + dto);
        try {
            jmsTemplate.convertAndSend("sw_matteo_hoffmann_queue_Customer", dto);
        } catch (JmsException e) {
            //TODO
        }
    }

    public void requestRepairJob(Airplane plane) { //TODO desc
        RepairOrderDTO dto = new RepairOrderDTO(plane.getPlaneID(), plane.getUnavailableUntil(), "desc", new de.othr.sw.HaberlRepairs.entity.dto.CustomerDTO("test", "test"), plane.getIssues());
        System.out.println("Sending to haberl repairs " + dto);
        try {
            jmsTemplate.convertAndSend("sw_simon_haberl_queue_RepairOrderInquiry", dto);
        } catch (JmsException e) {
            System.out.println(e.getMessage());
        }
    }

    @JmsListener(destination = "sw_simon_haberl_queue_RepairOrderReply")
    public void receiveRepairMessage(RepairOrderDTO message) {
        System.out.println("Received from haberlRepairs: " + message);
        if (message.isAccepted()) {
            if (message.getPickUpDate() != null && message.getPlaneId() != 0) {
                try {
                    planeService.updateUnavailable(message.getPickUpDate(), message.getPlaneId());
                } catch (AirplaneException e) {
                    System.out.println("Queue: could not update date: " + e.getMessage());
                }
            }
        }
    }


}
