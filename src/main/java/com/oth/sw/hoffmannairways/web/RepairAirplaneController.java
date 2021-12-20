package com.oth.sw.hoffmannairways.web;

import com.oth.sw.hoffmannairways.entity.Airplane;
import com.oth.sw.hoffmannairways.service.impl.AirplaneService;
import com.oth.sw.hoffmannairways.service.impl.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Collection;

@Controller
public class RepairAirplaneController {
    @Autowired
    private AirplaneService airplaneService;

    @Autowired
    private FlightService flightService;

    @RequestMapping(value = "/repairplane", method = RequestMethod.GET)
    //Principal als parameter
    public String viewRepairPlane(Model model) {
        //TODO find and get
        Collection<Airplane> availablePlanes = airplaneService.getAvailablePlanes();
        Collection<Airplane> assignedPlanes = airplaneService.getAllAssignedPlanes();
        Collection<Airplane> brokenPlanes = airplaneService.getAllBrokenPlanes();
        model.addAttribute("availablePlanes", availablePlanes);
        model.addAttribute("assignedPlanes", assignedPlanes);
        model.addAttribute("brokenPlanes", brokenPlanes);
        model.addAttribute("newPlane", new Airplane());
        return "repairplane";
    }


    @RequestMapping(value = "/repairplane", method = RequestMethod.POST)
    //Principal als parameter
    public String createRepairJob(Model model, @ModelAttribute("newPlane") Airplane a) {
        //TODO check if values are null, return errors
        System.out.println(a.toString());
        System.out.println(a.getPlaneID());

        flightService.repairPlane(a);






        return viewRepairPlane(model);
    }
}
