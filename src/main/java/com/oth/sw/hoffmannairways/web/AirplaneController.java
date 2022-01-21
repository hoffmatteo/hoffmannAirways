package com.oth.sw.hoffmannairways.web;

import com.oth.sw.hoffmannairways.entity.Airplane;
import com.oth.sw.hoffmannairways.service.AirplaneServiceIF;
import com.oth.sw.hoffmannairways.service.FlightServiceIF;
import com.oth.sw.hoffmannairways.service.exception.AirplaneException;
import com.oth.sw.hoffmannairways.service.exception.FlightException;
import com.oth.sw.hoffmannairways.util.Helper;
import com.oth.sw.hoffmannairways.web.queue.QueueController;
import com.oth.sw.hoffmannairways.web.util.UIMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.util.Collection;

@Controller
@Scope("singleton")
public class AirplaneController {
    @Autowired
    private AirplaneServiceIF airplaneService;

    @Autowired
    private FlightServiceIF flightService;

    @Autowired
    private QueueController controller;

    @RequestMapping(value = "/planes", method = RequestMethod.GET)
    public String viewPlanes(Model model) {
        Collection<Airplane> availablePlanes = airplaneService.getAvailablePlanes();
        Collection<Airplane> assignedPlanes = airplaneService.getAllAssignedPlanes();
        Collection<Airplane> brokenPlanes = airplaneService.getAllBrokenPlanes();
        model.addAttribute("availablePlanes", availablePlanes);
        model.addAttribute("assignedPlanes", assignedPlanes);
        model.addAttribute("brokenPlanes", brokenPlanes);
        model.addAttribute("newPlane", new Airplane());

        return "planes/planes";
    }


    @RequestMapping(value = "/planes", method = RequestMethod.POST)
    public String createRepairJob(Model model, @Valid @ModelAttribute("plane") Airplane a, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("UIMessage", new UIMessage("Failed to repair plane: Inputs were not valid", "alert-danger"));
        } else {
            try {
                Airplane plane = flightService.repairPlane(a);

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
