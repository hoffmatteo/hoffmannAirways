package com.oth.sw.hoffmannairways.web;

import com.oth.sw.hoffmannairways.entity.Airplane;
import com.oth.sw.hoffmannairways.entity.Flight;
import com.oth.sw.hoffmannairways.entity.FlightConnection;
import com.oth.sw.hoffmannairways.entity.User;
import com.oth.sw.hoffmannairways.service.UserServiceIF;
import com.oth.sw.hoffmannairways.service.impl.AirplaneService;
import com.oth.sw.hoffmannairways.service.impl.FlightService;
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
        String message = "Flight could not be created.";
        String alertClass = "alert-danger";

        if (principal != null) {
            User currUser = userService.getUserByUsername(principal.getName());
            if (currUser != null) {
                f.setCreator(currUser);
                Flight createdFlight = flightService.createFlight(f);
                if (createdFlight != null) {
                    message = "Successfully created flight " + createdFlight.getFlightID();
                    alertClass = "alert-success";
                }
            }
        }
        model.addAttribute("message", message);
        model.addAttribute("alertClass", alertClass);


        return viewCreateFlight(model);
    }

    @RequestMapping(value = "/editflight/{flight_id}", method = RequestMethod.GET)
    public String viewEditFlight(Model model, @PathVariable("flight_id") int flightID) {
        Flight selectedFlight = flightService.getFlight(flightID);
        if (selectedFlight != null) {
            model.addAttribute("flight", selectedFlight);
            model.addAttribute("selectedPlane", selectedFlight.getAirplane());
        } else {
            //TODO
        }
        model.addAllAttributes(setFlightArguments());

        return "editflight";

    }

    @RequestMapping(value = "/editflight", method = RequestMethod.POST)
    //Principal als parameter
    public String editFlight(Model model, @ModelAttribute("flight") Flight f) {
        Flight savedFlight = flightService.editFlight(f);
        String message;
        String alertClass;
        if (savedFlight != null) {
            message = "Successfully edited flight " + savedFlight.getFlightID();
            alertClass = "alert-success";
        } else {
            message = "Edit failed.";
            alertClass = "alert-danger";
        }
        model.addAttribute("message", message);
        model.addAttribute("alertClass", alertClass);


        return viewEditFlight(model, f.getFlightID());
    }


}
