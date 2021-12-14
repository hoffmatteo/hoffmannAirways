package com.oth.sw.hoffmannairways.web;

import com.oth.sw.hoffmannairways.entity.Airplane;
import com.oth.sw.hoffmannairways.entity.Flight;
import com.oth.sw.hoffmannairways.entity.FlightConnection;
import com.oth.sw.hoffmannairways.service.impl.AirplaneService;
import com.oth.sw.hoffmannairways.service.impl.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Controller
public class FlightCreateController {
    @Autowired
    private FlightService flightService;

    @Autowired
    private AirplaneService airplaneService;

    @RequestMapping(value = "/createflight", method = RequestMethod.GET)
    //Principal als parameter
    public String viewCreateFlight(Model model) {
        Collection<Airplane> planeList = airplaneService.getAvailablePlanes();
        List<FlightConnection> connectionList = flightService.listAllFlightConnections();
        model.addAttribute("planes", planeList);
        model.addAttribute("flight", new Flight());
        model.addAttribute("connections", connectionList);
        return "createflight";
    }

    @RequestMapping(value = "/createflight", method = RequestMethod.POST)
    //Principal als parameter
    public String createFlight(Model model, @ModelAttribute("flight") Flight f) {
        //TODO check if values are null, return errors
        System.out.println(f.toString());

        Flight createdFlight = flightService.createFlight(f);
        if(createdFlight != null) {
            System.out.println("success!");
        }


        Collection<Airplane> planeList = airplaneService.getAvailablePlanes();
        List<FlightConnection> connectionList = flightService.listAllFlightConnections();
        model.addAttribute("planes", planeList);
        model.addAttribute("flight", new Flight());
        model.addAttribute("connections", connectionList);


        return "createflight";
    }





}
