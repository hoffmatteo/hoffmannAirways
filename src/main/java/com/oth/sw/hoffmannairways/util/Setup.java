package com.oth.sw.hoffmannairways.util;

import com.oth.sw.hoffmannairways.entity.Airplane;
import com.oth.sw.hoffmannairways.entity.FlightConnection;
import com.oth.sw.hoffmannairways.service.AirplaneServiceIF;
import com.oth.sw.hoffmannairways.service.FlightServiceIF;
import com.oth.sw.hoffmannairways.service.UserServiceIF;
import com.oth.sw.hoffmannairways.service.exception.AirplaneException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class Setup {
    @Autowired
    AirplaneServiceIF airplaneService;

    @Autowired
    FlightServiceIF flightService;

    @Autowired
    UserServiceIF userService;

    @PostConstruct
    public void setup() {


    }

    private void setupAirplanes() throws AirplaneException {
        for (int i = 0; i < 10; i++) {
            Airplane plane = new Airplane("A380-800", 509, 5000);
            airplaneService.createPlane(plane);
            plane = new Airplane("A320-200", 168, 1500);
            airplaneService.createPlane(plane);
            plane = new Airplane("737-MAX-7", 153, 1000);
            airplaneService.createPlane(plane);
            plane = new Airplane("747-8", 364, 4000);
            airplaneService.createPlane(plane);
        }
    }

    private void setupConnections() {
        //LAX, FRA, BER, MUC
        FlightConnection connection = new FlightConnection("MH2370", "MUC", "DUB", "Germany", "Ireland", 2.0);
        flightService.createFlightConnection(connection);
        connection = new FlightConnection("MH3208", "DUB", "MUC", "Ireland", "Germany", 2.0);
        flightService.createFlightConnection(connection);
        connection = new FlightConnection("MH2370", "MUC", "DUB", "Germany", "Ireland", 2.0);
        flightService.createFlightConnection(connection);
        connection = new FlightConnection("MH3208", "DUB", "MUC", "Ireland", "Germany", 2.0);


    }

}
