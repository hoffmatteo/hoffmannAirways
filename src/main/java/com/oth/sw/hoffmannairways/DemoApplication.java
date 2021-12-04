package com.oth.sw.hoffmannairways;

import com.oth.sw.hoffmannairways.service.AirplaneService;
import com.oth.sw.hoffmannairways.service.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class DemoApplication {
    @Autowired
    FlightService flightService;
    @Autowired
    AirplaneService airplaneService;


    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);

    }

    @GetMapping("/")
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
        //Flight a = new Flight(departureTime, flightTime, shipment, destination, departure);
        //a.setArtikelNR(187);
        //a = repo.save(a);

        /*Airplane plane = new Airplane("A380", 100, 5000);
        Airplane savedPlane = airplaneService.createPlane(plane);
        FlightConnection connection = new FlightConnection("LH2370", "MUC", "DUB", 2.0);
        FlightConnection conn = flightService.createFlightConnection(connection);
        Flight a = new Flight(new Date(), new Date(), savedPlane, null, conn);
        flightService.createFlight(a);

         */


        flightService.deleteFlight(0);


        return String.format("Hello %s!", name);

    }

}
