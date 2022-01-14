package com.oth.sw.hoffmannairways.web.rest;

import com.oth.sw.hoffmannairways.entity.Flight;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Configuration
public class PartnerServiceFactory {
    //TODO scope
    @Autowired
    private RestTemplate restServiceClient;


    @Bean
    public tempAirportIF createAirport() {
        try {
            Flight flight = restServiceClient.getForObject("https://twitter.com/home",
                    Flight.class,
                    "R-ZY701");
            System.out.println(flight);
            System.out.println("returning true");
            return new AirportService();
        } catch (RestClientException e) {
            System.out.println("returning false");
            System.out.println(e.getMessage());
            return new ProxyService();
        }
    }
    /*
    @Bean
    @ConditionalOnMissingBean
    public tempAirportIF getProxyAirport() {
        System.out.println("returning fake airport");
        return new ProxyService();
    }
     */


}


