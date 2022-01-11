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
        //TODO handle user
        AirlineDTO dto = new AirlineDTO();
        dto.setStatus(AirlineDTO.Status.ERROR);

        if (message.getUserInfo() != null) {
            UserDTO info = message.getUserInfo();
            User registeredUser = userService.getUserByUsername(info.getUsername());
            if (userService.checkPassword(info.getPassword(), registeredUser)) {
                if (message.getMessage() == CustomerDTO.Message.CREATE_ORDER) {
                    if (message.getOrder() != null) {
                        message.getOrder().setCustomer(registeredUser);
                        Order order = flightService.bookFlight(message.getOrder());
                        if (order != null) {
                            dto.setCurrentOrder(order);
                            dto.setStatus(AirlineDTO.Status.CONFIRMED);
                        }
                    }
                } else if (message.getMessage() == CustomerDTO.Message.UPDATE_INFO) {
                    List<Flight> flights = flightService.listAllFlights();
                    List<FlightConnection> connections = flightService.listAllFlightConnections();
                    dto.setAvailableConnections(connections);
                    dto.setAvailableFlights(flights);
                    dto.setStatus(AirlineDTO.Status.INFO);
                }
            }
        }
        //sendDTO(dto);

    }

    @JmsListener(destination = "sw_matteo_hoffmann_queue_Airline")
    public void receiveTest(AirlineDTO message) {
        System.out.println(message);
    }

    public void sendDTO(AirlineDTO dto) {
        System.out.println("Sending message: " + dto);
        jmsTemplate.convertAndSend("sw_matteo_hoffmann_queue_Airline", dto);
    }


    public void bookAsPartner(Order order) {
        CustomerDTO dto = new CustomerDTO(CustomerDTO.Message.CREATE_ORDER, order);
        dto.setUserInfo(new UserDTO("test", "123"));
        jmsTemplate.convertAndSend("sw_matteo_hoffmann_queue_Customer", dto);

        dto = new CustomerDTO(CustomerDTO.Message.UPDATE_INFO);
        jmsTemplate.convertAndSend("sw_matteo_hoffmann_queue_Customer", dto);

    }


}
