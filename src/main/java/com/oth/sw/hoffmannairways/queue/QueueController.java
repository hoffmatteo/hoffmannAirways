package com.oth.sw.hoffmannairways.queue;

import com.oth.sw.hoffmannairways.dto.AirlineDTO;
import com.oth.sw.hoffmannairways.dto.CustomerDTO;
import com.oth.sw.hoffmannairways.dto.UserDTO;
import com.oth.sw.hoffmannairways.entity.Flight;
import com.oth.sw.hoffmannairways.entity.FlightConnection;
import com.oth.sw.hoffmannairways.entity.Order;
import com.oth.sw.hoffmannairways.entity.User;
import com.oth.sw.hoffmannairways.service.FlightServiceIF;
import com.oth.sw.hoffmannairways.service.UserServiceIF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
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
        if (message.getOrder() != null) {
            message.getOrder().setFlight(new Flight());
        }

        if (message.getUserInfo() != null) {
            UserDTO info = message.getUserInfo();
            User registeredUser = userService.getUserByUsername(info.getUsername());
            if (userService.checkPassword(info.getPassword(), registeredUser)) {
                if (message.getMessage() == CustomerDTO.Message.CREATE_ORDER) {
                    sendDTO(createBooking(message.getOrder(), registeredUser));
                } else if (message.getMessage() == CustomerDTO.Message.UPDATE_INFO) {
                    sendDTO(listInfo());
                }
            }
        }
    }

    private AirlineDTO createBooking(Order order, User registeredUser) {
        AirlineDTO dto = new AirlineDTO();
        dto.setStatus(AirlineDTO.Status.ERROR);
        if (order != null) {
            order.setCustomer(registeredUser);
            Order completedOrder = flightService.bookFlight(order);
            if (completedOrder != null) {
                dto.setCurrentOrder(completedOrder);
                dto.setStatus(AirlineDTO.Status.CONFIRMED);
            }
        }
        return dto;
    }

    private AirlineDTO listInfo() {
        AirlineDTO dto = new AirlineDTO();
        dto.setStatus(AirlineDTO.Status.ERROR);

        List<Flight> flights = flightService.listAllFlights();
        List<FlightConnection> connections = flightService.listAllFlightConnections();
        dto.setAvailableConnections(connections);
        dto.setAvailableFlights(flights);
        dto.setStatus(AirlineDTO.Status.INFO);
        return dto;

    }

    @JmsListener(destination = "sw_matteo_hoffmann_queue_Airline")
    public void receiveTest(AirlineDTO message) {
        System.out.println("Received from Airline: " + message);
    }

    public void sendDTO(AirlineDTO dto) {
        System.out.println("Sending message to Customer: " + dto);
        jmsTemplate.convertAndSend("sw_matteo_hoffmann_queue_Airline", dto);
    }


    public void bookAsPartner(Order order) {
        //order.setFlight(new Flight());
        CustomerDTO dto = new CustomerDTO(CustomerDTO.Message.CREATE_ORDER, order);
        dto.setUserInfo(new UserDTO("ingo", "123"));
        System.out.println("Sending message to Airline: " + dto);
        jmsTemplate.convertAndSend("sw_matteo_hoffmann_queue_Customer", dto);

        /*dto = new CustomerDTO(CustomerDTO.Message.UPDATE_INFO);
        dto.setUserInfo(new UserDTO("ingo", "123"));
        jmsTemplate.convertAndSend("sw_matteo_hoffmann_queue_Customer", dto);

         */


    }


}
