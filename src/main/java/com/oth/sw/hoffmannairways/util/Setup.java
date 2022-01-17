package com.oth.sw.hoffmannairways.util;

import com.oth.sw.hoffmannairways.entity.Airplane;
import com.oth.sw.hoffmannairways.entity.FlightConnection;
import com.oth.sw.hoffmannairways.entity.User;
import com.oth.sw.hoffmannairways.entity.util.AccountType;
import com.oth.sw.hoffmannairways.service.AirplaneServiceIF;
import com.oth.sw.hoffmannairways.service.FlightServiceIF;
import com.oth.sw.hoffmannairways.service.UserServiceIF;
import com.oth.sw.hoffmannairways.service.exception.AirplaneException;
import com.oth.sw.hoffmannairways.service.exception.UserException;
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
        try {
            if (airplaneService.getAllPlanes().isEmpty()) {
                setupAirplanes();
            }
        } catch (AirplaneException e) {
            System.out.println(e.getMessage());
        }
        if (flightService.listAllFlightConnections().isEmpty()) {
            setupConnections();
        }
        try {
            if (userService.getAllUsers().isEmpty()) {
                setupUser();
            }
        } catch (UserException e) {
            System.out.println(e.getMessage());
        }


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
        /*
        Airport("MUC", "Europe/Berlin","Germany","Munich","Flughafen München „Franz Josef Strauß“"));
        Airport("LAX", "America/Los_Angeles","USA","Los_Angeles","Los Angeles International Airport"));
        Airport("BER", "Europe/Berlin","Germany","Berlin","Flughafen Berlin Brandenburg „Willy Brandt“"));
        Airport("FRA", "Europe/Berlin","Germany","Frankfurt","Flughafen Frankfurt Main"));
        Airport("DUB", "Europe/Dublin","Ireland","Dublin","Aerfort Bhaile Átha Cliath"));
        Airport("ATL", "America/New_York","USA","Atlanta","Hartsfield–Jackson Atlanta International Airport"));
        Airport("PEK", "Asia/Shanghai","China","Beijing","Beijing Capital International Airport"));
        Airport("DXB", "Asia/Dubai","United Arab Emirates","Dubai","Dubai International Airport"));
        Airport("HND", "Japan","Japan","Tokyo","Tokyo Haneda Airport"));
        Airport("LHR", "Europe/London","United Kingdom","London","Heathrow Airport"));
        Airport("CDG", "Europe/Paris","France","Paris","Charles de Gaulle Airport"));
        Airport("AMS", "Europe/Amsterdam","Netherlands","Amsterdam","Amsterdam Airport Schiphol"));
        Airport("HKG", "Hongkong","Hongkong","Hongkong","Hong Kong International Airport"));
        Airport("ICN", "Asia/Seoul","South Korea","Seoul","Seoul Incheon International Airport"));
        Airport("DEL", "IST","India","Delhi","Indira Gandhi International Airport"));
        Airport("SIN", "Singapore","Singapore","Singapore","Singapore Changi Airport"));
        Airport("JFK", "America/New_York","USA","New_York","John F. Kennedy International Airport"));
        Airport("CPT", "Africa/Johannesburg","South Africa","Capetown","Cape Town International Airport"));
        Airport("SJO", "America/Costa_Rica","Costa Rica","San José","Juan Santamaría International Airport"));
        Airport("FCO", "Europe/Rome","Italy","Rome","Rome–Fiumicino International Airport "Leonardo da Vinci""));
         */

        FlightConnection connection = new FlightConnection("MH2370", "MUC", "DUB", "Germany", "Ireland", 2.0);
        flightService.createFlightConnection(connection);
        connection = new FlightConnection("MH3208", "DUB", "MUC", "Ireland", "Germany", 2.0);
        flightService.createFlightConnection(connection);
        connection = new FlightConnection("MH4389", "MUC", "ATL", "Germany", "United States of America", 11.5);
        flightService.createFlightConnection(connection);
        connection = new FlightConnection("MH2957", "ATL", "MUC", "United States of America", "Germany", 11.5);
        flightService.createFlightConnection(connection);
        connection = new FlightConnection("MH0382", "MUC", "FCO", "Germany", "Italy", 1.5);
        flightService.createFlightConnection(connection);
        connection = new FlightConnection("MH7897", "FCO", "MUC", "Italy", "Germany", 1.5);
        flightService.createFlightConnection(connection);
        connection = new FlightConnection("MH2274", "LAX", "MUC", "United States of America", "Germany", 11.25);
        flightService.createFlightConnection(connection);
        connection = new FlightConnection("MH2790", "MUC", "LAX", "Germany", "United States of America", 11.25);
        flightService.createFlightConnection(connection);

    }

    public void setupUser() throws UserException {
        userService.registerUser(new User("daumen", "123", "DaumDeliveries", AccountType.USER));
        userService.registerUser(new User("admin", "123", "Administrator", AccountType.STAFF));

    }

}
