package com.oth.sw.hoffmannairways.web;

import com.oth.sw.hoffmannairways.entity.*;
import com.oth.sw.hoffmannairways.service.exception.FlightException;
import com.oth.sw.hoffmannairways.service.impl.AirplaneService;
import com.oth.sw.hoffmannairways.service.impl.FlightService;
import com.oth.sw.hoffmannairways.util.Helper;
import com.oth.sw.hoffmannairways.web.util.UIMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.util.*;

@Controller
@Scope("singleton")
public class FlightController {
    @Autowired
    private FlightService flightService;

    @Autowired
    private AirplaneService airplaneService;

    @RequestMapping("/flights")
    //Principal als parameter
    public String viewFlights(Model model) {
        //TODO list vs. collecitons
        Collection<Flight> flightList = flightService.listAllFlights();
        List<FlightConnection> connectionList = flightService.listAllFlightConnections();
        model.addAttribute("flights", flightList);
        model.addAttribute("order", new Order());
        model.addAttribute("connections", connectionList);
        return "flights/flights";
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

        return viewFlights(model);
    }

    @RequestMapping(value = "/createflight", method = RequestMethod.GET)
    public String viewCreateFlight(Model model) {
        model.addAllAttributes(setFlightArguments());
        model.addAttribute("flight", new Flight());
        return "flights/createflight";

    }

    private Map<String, Object> setFlightArguments() {
        Map<String, Object> attributes = new HashMap<>();
        Collection<Airplane> planeList = airplaneService.getAllPlanes();
        List<FlightConnection> connectionList = flightService.listAllFlightConnections();
        attributes.put("planes", planeList);
        attributes.put("connections", connectionList);
        return attributes;

    }

    @RequestMapping(value = "/createflight", method = RequestMethod.POST)
    //Principal als parameter
    public String createFlight(Model model, @Valid @ModelAttribute("flight") Flight f, @AuthenticationPrincipal User user) {
        if (f.getDepartureTime().before(new Date())) {
            model.addAttribute("UIMessage", new UIMessage("Cannot create flight that departs in the past.", "alert-danger"));
        } else {
            if (user != null) {
                try {
                    f.setCreator(user);
                    Flight createdFlight = flightService.createFlight(f);
                    model.addAttribute(new UIMessage("Successfully created flight #" + createdFlight.getFlightID(), "alert-success"));
                } catch (FlightException e) {
                    model.addAttribute("UIMessage", new UIMessage("Flight could not be created: " + e.getMessage(), "alert-danger"));
                }
            }
        }


        return viewCreateFlight(model);

    }

    @RequestMapping(value = "/editflight/{flight_id}", method = RequestMethod.GET)
    public String viewEditFlight(Model model, @PathVariable("flight_id") int flightID) {
        try {
            Flight selectedFlight = flightService.getFlight(flightID);
            model.addAttribute("flight", selectedFlight);
            model.addAttribute("selectedPlane", selectedFlight.getAirplane());
        } catch (FlightException e) {
            return viewCreateFlight(model);
        }
        model.addAllAttributes(setFlightArguments());

        return "flights/editflight";

    }

    @RequestMapping(value = "/editflight", method = RequestMethod.POST)
    //Principal als parameter
    public String editFlight(Model model, @ModelAttribute("flight") Flight f) {
        try {
            Flight savedFlight = flightService.editFlight(f);
            model.addAttribute("UIMessage", new UIMessage("Successfully edited flight " + savedFlight.getFlightID(), "alert-success"));
        } catch (FlightException e) {
            model.addAttribute("UIMessage", new UIMessage("Edit failed.", "alert-danger"));
        }

        return viewEditFlight(model, f.getFlightID());
    }

    @RequestMapping(value = "/deleteflight/{flight_id}", method = RequestMethod.GET)
    //Principal als parameter
    public String deleteFlight(Model model, @PathVariable("flight_id") int flightID) {
        try {
            Flight f = flightService.getFlight(flightID);
            flightService.deleteFlight(f);
            model.addAttribute("UIMessage", new UIMessage("Successfully deleted flight " + f.getFlightID(), "alert-success"));

        } catch (FlightException e) {
            model.addAttribute("UIMessage", new UIMessage("Delete failed: " + e.getMessage(), "alert-danger"));
        }
        return viewCreateFlight(model);
    }


}
