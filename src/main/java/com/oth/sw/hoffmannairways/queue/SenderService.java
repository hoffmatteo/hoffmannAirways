package com.oth.sw.hoffmannairways.queue;

import com.oth.sw.hoffmannairways.dto.AirlineDTO;
import com.oth.sw.hoffmannairways.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class SenderService {
    @Autowired
    private JmsTemplate jmsTemplate;

    public void sendDTO(//AirlineDTO dto
    ) {
        System.out.println("hello " + jmsTemplate);

        AirlineDTO dto = new AirlineDTO(new Order(), AirlineDTO.Status.CONFIRMED);


        jmsTemplate.convertAndSend("sw_matteo_hoffmann_queue_Airline", dto);

    }

    public SenderService() {

    }
}
