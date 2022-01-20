package com.oth.sw.hoffmannairways.web.rest;

import com.oth.sw.hoffmannairways.entity.Flight;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.ConnectException;

@Configuration
public class PartnerServiceFactory {
    //TODO scope
    @Autowired
    private RestTemplate restServiceClient;


    @Bean
    public tempAirportIF createAirport() {
        //TODO

        try {
            Flight flight = restServiceClient.getForObject("http://im-codd.oth-regensburg.de:8935/api/rest/flight",
                    Flight.class,
                    "R-ZY701");
            System.out.println(flight);
            System.out.println("returning true");
            return new AirportService();
        } catch (RestClientException e) {
            if (e.contains(ConnectException.class)) {
                System.out.println("returning false");
                System.out.println(e.getMessage());
                return new ProxyService();
            } else {
                return new AirportService();
            }
        }
    }


}


