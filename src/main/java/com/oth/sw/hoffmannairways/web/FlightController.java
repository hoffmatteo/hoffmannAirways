package com.oth.sw.hoffmannairways.web;

import com.oth.sw.hoffmannairways.entity.Airplane;
import com.oth.sw.hoffmannairways.entity.Flight;
import com.oth.sw.hoffmannairways.entity.FlightConnection;
import com.oth.sw.hoffmannairways.entity.User;
import com.oth.sw.hoffmannairways.service.UserServiceIF;
import com.oth.sw.hoffmannairways.service.exception.FlightException;
import com.oth.sw.hoffmannairways.service.exception.UserException;
import com.oth.sw.hoffmannairways.service.impl.AirplaneService;
import com.oth.sw.hoffmannairways.service.impl.FlightService;
import com.oth.sw.hoffmannairways.web.util.UIMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Scope("singleton")
public class FlightController {
    @Autowired
    private FlightService flightService;

    @Autowired
    private AirplaneService airplaneService;

    @Autowired
    private UserServiceIF userService;

    @RequestMapping(value = "/createflight", method = RequestMethod.GET)
    //Principal als parameter
    public String viewCreateFlight(Model model) {
        model.addAllAttributes(setFlightArguments());
        model.addAttribute("flight", new Flight());
        return "createflight";
        
    }

    private Map<String, Object> setFlightArguments() {
        Map<String, Object> attributes = new HashMap<>();
        Collection<Airplane> planeList = airplaneService.getAvailablePlanes();
        List<FlightConnection> connectionList = flightService.listAllFlightConnections();
        attributes.put("planes", planeList);
        attributes.put("connections", connectionList);
        return attributes;

    }

    @RequestMapping(value = "/createflight", method = RequestMethod.POST)
    //Principal als parameter
    public String createFlight(Model model, @ModelAttribute("flight") Flight f, Principal principal) {
        //TODO check if values are null, return errors
        if (principal != null) {
            try {
                User currUser = userService.getUserByUsername(principal.getName());
                f.setCreator(currUser);
                Flight createdFlight = flightService.createFlight(f);
                model.addAttribute(new UIMessage("Successfully created flight " + createdFlight.getFlightID(), "alert-success"));
            } catch (UserException | FlightException e) {
                model.addAttribute("UIMessage", new UIMessage("Flight could not be created.", "alert-danger"));
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

        return "editflight";

    }

    @RequestMapping(value = "/editflight", method = RequestMethod.POST)
    //Principal als parameter
    public String editFlight(Model model, @ModelAttribute("flight") Flight f) {
        String message;
        String alertClass;

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
            model.addAttribute("UIMessage", new UIMessage("Delete failed.", "alert-danger"));
        }
        //TODO
        return viewCreateFlight(model);
    }


}
