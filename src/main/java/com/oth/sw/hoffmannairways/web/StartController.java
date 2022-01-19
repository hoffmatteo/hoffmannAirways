package com.oth.sw.hoffmannairways.web;

import com.oth.sw.hoffmannairways.service.UserServiceIF;
import com.oth.sw.hoffmannairways.service.impl.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Scope("singleton")
public class StartController {
    //TODO order controller?
    @Autowired
    private FlightService flightService;

    @Autowired
    private UserServiceIF userService;

    @RequestMapping("/")
    //Principal als parameter
    public String start() {
        return "index";
    }


}
