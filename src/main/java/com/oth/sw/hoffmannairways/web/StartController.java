package com.oth.sw.hoffmannairways.web;

import com.oth.sw.hoffmannairways.entity.Airplane;
import com.oth.sw.hoffmannairways.entity.Flight;
import com.oth.sw.hoffmannairways.entity.FlightConnection;
import com.oth.sw.hoffmannairways.service.impl.AirplaneService;
import com.oth.sw.hoffmannairways.service.impl.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collection;
import java.util.Date;

@Controller
public class StartController {
    @Autowired
    private FlightService flightService;

    @Autowired
    private AirplaneService airplaneService;

    @RequestMapping("/")
    //Principal als parameter
    public String start(Model model) {

        /*Airplane plane = new Airplane("A380", 100, 5000);
        Airplane savedPlane = airplaneService.createPlane(plane);
        FlightConnection connection = new FlightConnection("LH2370", "MUC", "DUB", 2.0);
        FlightConnection conn = flightService.createFlightConnection(connection);
        Flight a = new Flight(new Date(), savedPlane, null, conn);
        flightService.createFlight(a);

         */


        Collection<Flight> flightList = flightService.listAllFlights();
        System.out.println(flightList.size());
        model.addAttribute("flights", flightList);
        return "index";
    }


}
