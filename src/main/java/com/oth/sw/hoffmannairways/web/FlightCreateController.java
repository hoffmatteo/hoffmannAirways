package com.oth.sw.hoffmannairways.web;

import com.oth.sw.hoffmannairways.entity.Airplane;
import com.oth.sw.hoffmannairways.entity.Flight;
import com.oth.sw.hoffmannairways.entity.FlightConnection;
import com.oth.sw.hoffmannairways.entity.User;
import com.oth.sw.hoffmannairways.service.UserServiceIF;
import com.oth.sw.hoffmannairways.service.impl.AirplaneService;
import com.oth.sw.hoffmannairways.service.impl.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
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
public class FlightCreateController {
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
        System.out.println(f.toString());
        if(principal != null) {
            User currUser = userService.getUserByUsername(principal.getName());
            if (currUser != null) {
                f.setCreator(currUser);
                Flight createdFlight = flightService.createFlight(f);
                if (createdFlight != null) {
                    System.out.println("success!");
                }
            }
        }

        return viewCreateFlight(model);
    }

    @RequestMapping(value = "/editflight/{flight_id}", method = RequestMethod.GET)
    public String viewEditFlight(Model model, @PathVariable("flight_id") int flightID) {
        Flight selectedFlight = flightService.getFlight(flightID);
        if(selectedFlight != null) {
            model.addAttribute("flight", selectedFlight);
        }
        else {
            //TODO
        }
        model.addAllAttributes(setFlightArguments());

        return "editflight";

    }

    @RequestMapping(value = "/editflight", method = RequestMethod.POST)
    //Principal als parameter
    public String editFlight(Model model, @ModelAttribute("flight") Flight f) {
        Flight savedFlight = flightService.editFlight(f);

        return viewEditFlight(model, f.getFlightID());
    }





}
