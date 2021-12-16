package com.oth.sw.hoffmannairways.web;

import com.oth.sw.hoffmannairways.entity.Airplane;
import com.oth.sw.hoffmannairways.service.impl.AirplaneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Collection;
@Controller
public class RepairAirplaneController {
    @Autowired
    private AirplaneService airplaneService;

    @RequestMapping(value = "/repairplane", method = RequestMethod.GET)
    //Principal als parameter
    public String viewRepairPlane(Model model) {
        //TODO find and get
        Collection<Airplane> planeList = airplaneService.getAllPlanes();
        model.addAttribute("planes", planeList);
        System.out.println(planeList.size());
        return "repairplane";
    }
}
