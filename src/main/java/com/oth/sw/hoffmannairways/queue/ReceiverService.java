package com.oth.sw.hoffmannairways.queue;

import com.oth.sw.hoffmannairways.dto.AirlineDTO;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class ReceiverService {
    @JmsListener(destination = "sw_matteo_hoffmann_queue_Airline")
    public void receiveMessage(AirlineDTO message) {
        System.out.println("Received: " + message.getCurrentOrder());
    }


    public ReceiverService() {

    }
}

