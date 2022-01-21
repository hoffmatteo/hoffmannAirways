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
import com.oth.sw.hoffmannairways.util.logger.LoggerIF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class Setup {
    @Autowired
    private AirplaneServiceIF airplaneService;

    @Autowired
    private FlightServiceIF flightService;

    @Autowired
    private UserServiceIF userService;

    @Autowired
    @Qualifier("ErrorLogger")
    private LoggerIF errorLogger;

    @PostConstruct
    public void setup() {
        try {
            if (airplaneService.getAllPlanes().isEmpty()) {
                setupAirplanes();
            }
        } catch (AirplaneException e) {
            errorLogger.log("Setup", "Could not setup airplanes!");
        }
        if (flightService.listAllFlightConnections().isEmpty()) {
            setupConnections();
        }
        try {
            if (userService.getAllUsers().isEmpty()) {
                setupUser();
            }
        } catch (UserException e) {
            errorLogger.log("Setup", "Could not setup users!");
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
        Airport("LAX", "America/Los_Angeles","USA","Los_Angeles","Los Angeles International Airport"));x
        Airport("BER", "Europe/Berlin","Germany","Berlin","Flughafen Berlin Brandenburg „Willy Brandt“"));
        Airport("FRA", "Europe/Berlin","Germany","Frankfurt","Flughafen Frankfurt Main"));
        Airport("DUB", "Europe/Dublin","Ireland","Dublin","Aerfort Bhaile Átha Cliath"));x
        Airport("ATL", "America/New_York","USA","Atlanta","Hartsfield–Jackson Atlanta International Airport"));x
        Airport("PEK", "Asia/Shanghai","China","Beijing","Beijing Capital International Airport"));x
        Airport("DXB", "Asia/Dubai","United Arab Emirates","Dubai","Dubai International Airport"));x
        Airport("HND", "Japan","Japan","Tokyo","Tokyo Haneda Airport")); x
        Airport("LHR", "Europe/London","United Kingdom","London","Heathrow Airport")); x
        Airport("CDG", "Europe/Paris","France","Paris","Charles de Gaulle Airport"));x
        Airport("AMS", "Europe/Amsterdam","Netherlands","Amsterdam","Amsterdam Airport Schiphol"));x
        Airport("HKG", "Hongkong","Hongkong","Hongkong","Hong Kong International Airport"));x
        Airport("ICN", "Asia/Seoul","South Korea","Seoul","Seoul Incheon International Airport"));x
        Airport("DEL", "IST","India","Delhi","Indira Gandhi International Airport"));x
        Airport("SIN", "Singapore","Singapore","Singapore","Singapore Changi Airport"));x
        Airport("JFK", "America/New_York","USA","New_York","John F. Kennedy International Airport"));x
        Airport("CPT", "Africa/Johannesburg","South Africa","Capetown","Cape Town International Airport"));x
        Airport("SJO", "America/Costa_Rica","Costa Rica","San José","Juan Santamaría International Airport"));
        Airport("FCO", "Europe/Rome","Italy","Rome","Rome–Fiumicino International Airport "Leonardo da Vinci""));x
         */

        FlightConnection connection = new FlightConnection("HA2370", "MUC", "Munich", "Germany", "DUB", "Dublin", "Ireland", 2.0);
        flightService.createFlightConnection(connection);
        connection = new FlightConnection("HA3208", "DUB", "Dublin", "Ireland", "MUC", "Munich", "Germany", 2.0);
        flightService.createFlightConnection(connection);
        connection = new FlightConnection("HA4389", "MUC", "Munich", "Germany", "ATL", "Atlanta", "United States of America", 11.5);
        flightService.createFlightConnection(connection);
        connection = new FlightConnection("HA2957", "ATL", "Atlanta", "United States of America", "MUC", "Munich", "Germany", 11.5);
        flightService.createFlightConnection(connection);
        connection = new FlightConnection("HA0382", "MUC", "Munich", "Germany", "FCO", "Rome", "Italy", 1.5);
        flightService.createFlightConnection(connection);
        connection = new FlightConnection("HA7897", "FCO", "Rome", "Italy", "MUC", "Munich", "Germany", 1.5);
        flightService.createFlightConnection(connection);
        connection = new FlightConnection("HA2274", "JFK", "New York City", "United States of America", "MUC", "Munich", "Germany", 7.75);
        flightService.createFlightConnection(connection);
        connection = new FlightConnection("HA2790", "MUC", "Munich", "Germany", "JFK", "New York City", "United States of America", 7.75);
        flightService.createFlightConnection(connection);
        connection = new FlightConnection("HA5308", "MUC", "Munich", "Germany", "JFK", "New York City", "United States of America", 7.75);
        flightService.createFlightConnection(connection);
        connection = new FlightConnection("HA2000", "MUC", "Munich", "Germany", "PEK", "Bejing", "China", 12.25);
        flightService.createFlightConnection(connection);
        connection = new FlightConnection("HA7420", "PEK", "Bejing", "China", "MUC", "Munich", "Germany", 12.25);
        flightService.createFlightConnection(connection);
        connection = new FlightConnection("HA1701", "MUC", "Munich", "Germany", "DXB", "Dubai", "United Arab Emirates", 6.0);
        flightService.createFlightConnection(connection);
        connection = new FlightConnection("HA1212", "DXB", "Dubai", "United Arab Emirates", "MUC", "Munich", "Germany", 6.0);
        flightService.createFlightConnection(connection);
        connection = new FlightConnection("HA5555", "HND", "Tokyo", "Japan", "MUC", "Munich", "Germany", 14.5);
        flightService.createFlightConnection(connection);
        connection = new FlightConnection("HA4321", "MUC", "Munich", "Germany", "HND", "Tokyo", "Japan", 14.5);
        flightService.createFlightConnection(connection);
        connection = new FlightConnection("HA3434", "MUC", "Munich", "Germany", "LHR", "London", "United Kingdom", 2.0);
        flightService.createFlightConnection(connection);
        connection = new FlightConnection("HA9999", "LHR", "London", "United Kingdom", "MUC", "Munich", "Germany", 2.0);
        flightService.createFlightConnection(connection);
        connection = new FlightConnection("HA2018", "CDG", "Paris", "France", "MUC", "Munich", "Germany", 1.75);
        flightService.createFlightConnection(connection);
        connection = new FlightConnection("HA5430", "MUC", "Munich", "Germany", "CDG", "Paris", "France", 1.75);
        flightService.createFlightConnection(connection);
        connection = new FlightConnection("HA6230", "MUC", "Munich", "Germany", "AMS", "Amsterdam", "Netherlands", 1.75);
        flightService.createFlightConnection(connection);
        connection = new FlightConnection("HA9067", "AMS", "Amsterdam", "Netherlands", "MUC", "Munich", "Germany", 1.75);
        flightService.createFlightConnection(connection);
        connection = new FlightConnection("HA5732", "MUC", "Munich", "Germany", "HKG", "Hongkong", "Hongkong", 13.75);
        flightService.createFlightConnection(connection);
        connection = new FlightConnection("HA3849", "HKG", "Hong Kong", "Hong Kong", "MUC", "Munich", "Germany", 13.75);
        flightService.createFlightConnection(connection);
        connection = new FlightConnection("HA9854", "ICN", "Seoul", "South Korea", "MUC", "Munich", "Germany", 11.5);
        flightService.createFlightConnection(connection);
        connection = new FlightConnection("HA2345", "MUC", "Munich", "Germany", "ICN", "Seoul", "South Korea", 11.5);
        flightService.createFlightConnection(connection);
        connection = new FlightConnection("HA8762", "MUC", "Munich", "Germany", "DEL", "Dheli", "India", 7.25);
        flightService.createFlightConnection(connection);
        connection = new FlightConnection("HA3920", "DEL", "Dheli", "India", "MUC", "Munich", "Germany", 7.25);
        flightService.createFlightConnection(connection);
        connection = new FlightConnection("HA3021", "SIN", "Singapore", "Singapore", "MUC", "Munich", "Germany", 13.15);
        flightService.createFlightConnection(connection);
        connection = new FlightConnection("HA0329", "MUC", "Munich", "Germany", "SIN", "Singapore", "Singapore", 13.15);
        flightService.createFlightConnection(connection);
        connection = new FlightConnection("HA2847", "MUC", "Munich", "Germany", "CPT", "Capetown", "South Africa", 11.15);
        flightService.createFlightConnection(connection);
        connection = new FlightConnection("HA8946", "CPT", "Capetown", "South Africa", "MUC", "Munich", "Germany", 11.15);
        flightService.createFlightConnection(connection);
        connection = new FlightConnection("HA8943", "SJO", "San José", "Costa Rica", "MUC", "Munich", "Germany", 13.0);
        flightService.createFlightConnection(connection);
        connection = new FlightConnection("HA3093", "MUC", "Munich", "Germany", "SJO", "San José", "Costa Rica", 13.0);
        flightService.createFlightConnection(connection);
        connection = new FlightConnection("HA9845", "AMS", "Amsterdam", "Netherlands", "FRA", "Frankfurt", "Germany", 1.0);
        flightService.createFlightConnection(connection);
        connection = new FlightConnection("HA2784", "FRA", "Frankfurt", "Germany", "AMS", "Amsterdam", "Netherlands", 1.0);
        flightService.createFlightConnection(connection);
        connection = new FlightConnection("HA5097", "BER", "Berlin", "Germany", "LHR", "London", "United Kingdom", 1.5);
        flightService.createFlightConnection(connection);
        connection = new FlightConnection("HA4303", "LHR", "London", "United Kingdom", "BER", "Berlin", "Germany", 1.5);
        flightService.createFlightConnection(connection);


    }

    public void setupUser() throws UserException {
        userService.registerUser(new User("daumen", "123", "DaumDeliveries", AccountType.USER, true));
        userService.registerUser(new User("admin", "123", "Administrator", AccountType.STAFF));

    }

}
