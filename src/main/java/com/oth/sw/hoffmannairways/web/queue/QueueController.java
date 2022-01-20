package com.oth.sw.hoffmannairways.web.queue;

import com.oth.sw.hoffmannairways.dto.AirlineDTO;
import com.oth.sw.hoffmannairways.dto.CustomerDTO;
import com.oth.sw.hoffmannairways.dto.UserDTO;
import com.oth.sw.hoffmannairways.entity.Flight;
import com.oth.sw.hoffmannairways.entity.FlightConnection;
import com.oth.sw.hoffmannairways.entity.Order;
import com.oth.sw.hoffmannairways.entity.User;
import com.oth.sw.hoffmannairways.service.FlightServiceIF;
import com.oth.sw.hoffmannairways.service.UserServiceIF;
import com.oth.sw.hoffmannairways.service.exception.FlightException;
import com.oth.sw.hoffmannairways.service.exception.UserException;
import de.othr.sw.HaberlRepairs.entity.dto.RepairOrderDTO;
import de.othr.sw.HaberlRepairs.entity.dto.SingleRepairOrderDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.jms.JmsException;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@Scope("singleton")
public class QueueController {

    @Autowired
    private FlightServiceIF flightService;

    @Autowired
    private UserServiceIF userService;

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
                    if (message.getMessage() == CustomerDTO.Message.CREATE_ORDER) {
                        dto = createBooking(message, registeredUser);
                    } else if (message.getMessage() == CustomerDTO.Message.UPDATE_CONNECTIONS) {
                        dto = listConnections();
                    } else if (message.getMessage() == CustomerDTO.Message.UPDATE_FLIGHTS) {
                        if (message.getConnection() != null) {
                            dto = listFlightsForConnection(message.getConnection());
                        }
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
        Order order = message.getOrder();
        if (order != null) {
            order.setCustomer(registeredUser);
            try {
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

        /*dto = new CustomerDTO(CustomerDTO.Message.UPDATE_INFO);
        dto.setUserInfo(new UserDTO("daumen", "123"));
        jmsTemplate.convertAndSend("sw_matteo_hoffmann_queue_Customer", dto);

         */

    }

    public void testQueue() {
        RepairOrderDTO dto = new RepairOrderDTO(0, new Date(), "desc", new de.othr.sw.HaberlRepairs.entity.dto.CustomerDTO("test", "test"), new ArrayList<SingleRepairOrderDTO>());
        try {
            jmsTemplate.convertAndSend("sw_simon_haberl_queue_RepairOrderInquiry", dto);
        } catch (JmsException e) {
            System.out.println(e.getMessage());
        }
    }

    @JmsListener(destination = "sw_simon_haberl_queue_RepairOrderReply")
    public void receiveMessage(RepairOrderDTO message) {
        System.out.println(message);
    }


}
