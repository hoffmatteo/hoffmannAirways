package com.oth.sw.hoffmannairways.web;

import com.oth.sw.hoffmannairways.entity.Order;
import com.oth.sw.hoffmannairways.entity.User;
import com.oth.sw.hoffmannairways.entity.util.AccountType;
import com.oth.sw.hoffmannairways.service.UserServiceIF;
import com.oth.sw.hoffmannairways.service.impl.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;

@Controller
public class OrderController {
    //TODO order controller?
    @Autowired
    private FlightService flightService;
    
    @Autowired
    private UserServiceIF userService;


    @RequestMapping(value = "/orders", method = RequestMethod.GET)
    //Principal als parameter
    public String viewOrders(Model model, Principal principal) {
        //TODO check if values are null, return errors
        Collection<Order> upcomingOrders = new ArrayList<>();
        Collection<Order> pastOrders = new ArrayList<>();

        if (principal != null) {
            String username = principal.getName();
            User user = userService.getUserByUsername(username);
            if (user != null) {
                if (user.getAccountType() == AccountType.STAFF) {
                    upcomingOrders = flightService.getAllFutureOrders();
                    pastOrders = flightService.getAllPastOrders();
                } else {
                    upcomingOrders = flightService.getAllFutureOrders(username);
                    pastOrders = flightService.getAllPastOrders(username);

                }
            }
        }
        model.addAttribute("pastOrders", pastOrders);
        model.addAttribute("upcomingOrders", upcomingOrders);


        return "orders";
    }


}
