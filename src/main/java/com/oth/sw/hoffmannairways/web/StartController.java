package com.oth.sw.hoffmannairways.web;

import com.oth.sw.hoffmannairways.entity.Flight;
import com.oth.sw.hoffmannairways.entity.Order;
import com.oth.sw.hoffmannairways.entity.User;
import com.oth.sw.hoffmannairways.service.UserServiceIF;
import com.oth.sw.hoffmannairways.service.exception.FlightException;
import com.oth.sw.hoffmannairways.service.impl.FlightService;
import com.oth.sw.hoffmannairways.util.Helper;
import com.oth.sw.hoffmannairways.web.util.UIMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
    public String createOrder(Model model, @ModelAttribute("order") Order o, @AuthenticationPrincipal User user) {

        if (user != null) {
            o.setCustomer(user);
            try {
                Order savedOrder = flightService.bookFlight(o);
                Flight f = savedOrder.getFlight();
                String message = "Successfully booked flight " + f.getConnection().getFlightNumber() + " leaving on " + Helper.getFormattedDate(f.getDepartureTime());
                model.addAttribute("UIMessage", new UIMessage(message, "alert-success"));

            } catch (FlightException e) {
                model.addAttribute("UIMessage", new UIMessage("Booking failed.", "alert-danger"));

            }
        } else {
            model.addAttribute("UIMessage", new UIMessage("Booking failed, could not find user.", "alert-danger"));
        }
        
        //TODO source https://stackoverflow.com/questions/46744586/thymeleaf-show-a-success-message-after-clicking-on-submit-button
        return viewFlights(model);
    }


}
