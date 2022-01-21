package com.oth.sw.hoffmannairways.web;

import com.oth.sw.hoffmannairways.entity.Order;
import com.oth.sw.hoffmannairways.entity.User;
import com.oth.sw.hoffmannairways.entity.util.AccountType;
import com.oth.sw.hoffmannairways.service.FlightServiceIF;
import com.oth.sw.hoffmannairways.service.UserServiceIF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.Collection;

@Controller
@Scope("singleton")
public class OrderController {
    @Autowired
    private FlightServiceIF flightService;

    @Autowired
    private UserServiceIF userService;


    @RequestMapping(value = "/orders", method = RequestMethod.GET)
    //Principal als parameter
    public String viewOrders(Model model, @AuthenticationPrincipal User user) {
        Collection<Order> upcomingOrders = new ArrayList<>();
        Collection<Order> pastOrders = new ArrayList<>();

        if (user != null) {
            if (user.getAccountType() == AccountType.STAFF) {
                upcomingOrders = flightService.getAllFutureOrders();
                pastOrders = flightService.getAllPastOrders();
            } else {
                upcomingOrders = flightService.getAllFutureOrders(user.getUsername());
                pastOrders = flightService.getAllPastOrders(user.getUsername());

            }
        }
        model.addAttribute("pastOrders", pastOrders);
        model.addAttribute("upcomingOrders", upcomingOrders);

        return "orders";
    }


}
