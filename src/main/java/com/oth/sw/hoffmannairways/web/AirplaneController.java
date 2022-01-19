package com.oth.sw.hoffmannairways.web;

import com.oth.sw.hoffmannairways.entity.Airplane;
import com.oth.sw.hoffmannairways.service.exception.AirplaneException;
import com.oth.sw.hoffmannairways.service.exception.FlightException;
import com.oth.sw.hoffmannairways.service.impl.AirplaneService;
import com.oth.sw.hoffmannairways.service.impl.FlightService;
import com.oth.sw.hoffmannairways.util.Helper;
import com.oth.sw.hoffmannairways.web.util.UIMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.util.Collection;

@Controller
@Scope("singleton")
public class AirplaneController {
    @Autowired
    private AirplaneService airplaneService;

    @Autowired
    private FlightService flightService;

    @RequestMapping(value = "/planes", method = RequestMethod.GET)
    public String viewPlanes(Model model) {
        Collection<Airplane> availablePlanes = airplaneService.getAvailablePlanes();
        Collection<Airplane> assignedPlanes = airplaneService.getAllAssignedPlanes();
        Collection<Airplane> brokenPlanes = airplaneService.getAllBrokenPlanes();
        model.addAttribute("availablePlanes", availablePlanes);
        model.addAttribute("assignedPlanes", assignedPlanes);
        model.addAttribute("brokenPlanes", brokenPlanes);
        model.addAttribute("newPlane", new Airplane());

        return "planes";
    }

    @RequestMapping(value = "/repairplane/{planeID}", method = RequestMethod.GET)
    public String viewRepairPlane(Model model, @PathVariable("planeID") int planeID) {
        try {
            Airplane plane = airplaneService.getPlane(planeID);
            model.addAttribute("plane", plane);


        } catch (AirplaneException e) {
            e.printStackTrace();
        }


        return "repairplane";
    }


    @RequestMapping(value = "/repairplane", method = RequestMethod.POST)
    public String createRepairJob(Model model, @Valid @ModelAttribute("plane") Airplane a, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("UIMessage", new UIMessage("Failed to repair plane: Inputs were not valid", "alert-danger"));
        } else {
            try {

                Airplane plane = flightService.repairPlane(a);
                System.out.println("testing");

                String message = "Successfully started repair process for Airplane " + plane.getPlaneName()
                        + ", ID: " + plane.getPlaneID() + ". Deadline is set to " + Helper.getFormattedDate(plane.getUnavailableUntil());
                model.addAttribute("UIMessage", new UIMessage(message, "alert-success"));

            } catch (AirplaneException | FlightException e) {

                model.addAttribute("UIMessage", new UIMessage("Failed to repair plane: " + e.getMessage(), "alert-danger"));
            }
        }
        return viewPlanes(model);
    }
}
