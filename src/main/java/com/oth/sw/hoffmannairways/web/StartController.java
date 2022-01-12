package com.oth.sw.hoffmannairways.web;

import com.oth.sw.hoffmannairways.entity.Flight;
import com.oth.sw.hoffmannairways.entity.Order;
import com.oth.sw.hoffmannairways.entity.User;
import com.oth.sw.hoffmannairways.service.UserServiceIF;
import com.oth.sw.hoffmannairways.service.exception.FlightException;
import com.oth.sw.hoffmannairways.service.exception.UserException;
import com.oth.sw.hoffmannairways.service.impl.FlightService;
import com.oth.sw.hoffmannairways.util.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;
import java.util.Collection;

@Controller
@Scope("singleton")
public class StartController {
    //TODO order controller?
    @Autowired
    private FlightService flightService;

    @Autowired
    private UserServiceIF userService;

    @RequestMapping("/")
    //Principal als parameter
    public String start() {
        return "index";
    }


    @RequestMapping("/flights")
    //Principal als parameter
    public String viewFlights(Model model) {

    /*
        Airplane plane = new Airplane("A380", 100, 5000);
        Airplane savedPlane = airplaneService.createPlane(plane);
        plane = new Airplane("A320", 500, 5000);
        airplaneService.createPlane(plane);
        plane = new Airplane("737 MAX", 1000, 5000);
        airplaneService.createPlane(plane);

        FlightConnection connection = new FlightConnection("LH2370", "MUC", "DUB", 2.0);
        FlightConnection conn = flightService.createFlightConnection(connection);
        connection = new FlightConnection("LH457", "LAX", "FRA", 11.0);
        flightService.createFlightConnection(connection);

     */

        Collection<Flight> flightList = flightService.listAllFlights();
        model.addAttribute("flights", flightList);
        model.addAttribute("order", new Order());
        return "flights";
    }

    @RequestMapping(value = "/flights", method = RequestMethod.POST)
    //Principal als parameter
    public String createOrder(Model model, @ModelAttribute("order") Order o, Principal principal) {
        //TODO check if values are null, return errors
        System.out.println(o);
        String message = "Booking failed.";
        String alertClass = "alert-danger";

        if (principal != null) {
            try {
                User currUser = userService.getUserByUsername(principal.getName());
                o.setCustomer(currUser);
                try {
                    Order savedOrder = flightService.bookFlight(o);
                    if (o.getFlight() != null) {
                        Flight f = o.getFlight();
                        message = "Successfully booked flight " + f.getConnection().getFlightNumber() + " leaving on " + Helper.getFormattedDate(f.getDepartureTime());
                        alertClass = "alert-success";
                    }
                } catch (FlightException e) {
                    //TODO
                }
            } catch (UserException e) {
                //TODO
            }
        }

        //TODO source https://stackoverflow.com/questions/46744586/thymeleaf-show-a-success-message-after-clicking-on-submit-button

        model.addAttribute("message", message);
        model.addAttribute("alertClass", alertClass);

        return viewFlights(model);
    }


}
