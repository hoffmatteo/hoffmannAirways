package com.oth.sw.hoffmannairways.web;

import com.oth.sw.hoffmannairways.entity.Airplane;
import com.oth.sw.hoffmannairways.entity.Flight;
import com.oth.sw.hoffmannairways.service.impl.AirplaneService;
import com.oth.sw.hoffmannairways.service.impl.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Collection;
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
        model.addAttribute("planes", planeList);
        model.addAttribute("flight", new Flight());

        System.out.println(planeList.size());

        return "createflight";
    }

    @RequestMapping(value = "/createflight", method = RequestMethod.POST)
    //Principal als parameter
    public String createFlight(Model model, @ModelAttribute("flight") Flight f) {


        return "createflight";
    }


}
