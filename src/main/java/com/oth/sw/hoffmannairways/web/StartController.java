package com.oth.sw.hoffmannairways.web;

import com.oth.sw.hoffmannairways.entity.Airplane;
import com.oth.sw.hoffmannairways.entity.Flight;
import com.oth.sw.hoffmannairways.entity.FlightConnection;
import com.oth.sw.hoffmannairways.entity.Order;
import com.oth.sw.hoffmannairways.service.impl.AirplaneService;
import com.oth.sw.hoffmannairways.service.impl.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Collection;
import java.util.Date;
import java.util.List;

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
        model.addAttribute("flights", flightList);
        model.addAttribute("order", new Order());
        return "index";
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    //Principal als parameter
    public String createFlight(Model model, @ModelAttribute("order") Order o) {
        //TODO check if values are null, return errors
        System.out.println(o.toString());
        Flight f = flightService.bookFlight(o);
        String message;
        String alertClass;
        if(f != null) {
            message = "Successfully booked flight " + f.getConnection().getFlightNumber() + " leaving on " + f.getFormattedDate(f.getDepartureTime());
            alertClass = "alert-success";
        }
        else {
            //TODO reason
            message = "Booking failed.";
            alertClass = "alert-danger";
        }


        //TODO source https://stackoverflow.com/questions/46744586/thymeleaf-show-a-success-message-after-clicking-on-submit-button

        Collection<Flight> flightList = flightService.listAllFlights();
        model.addAttribute("flights", flightList);
        model.addAttribute("order", new Order());
        model.addAttribute("message", message);
        model.addAttribute("alertClass", alertClass);

        return "index";
    }


}
