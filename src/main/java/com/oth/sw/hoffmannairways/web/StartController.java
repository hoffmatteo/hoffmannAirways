package com.oth.sw.hoffmannairways.web;

import com.oth.sw.hoffmannairways.entity.Flight;
import com.oth.sw.hoffmannairways.entity.Order;
import com.oth.sw.hoffmannairways.entity.User;
import com.oth.sw.hoffmannairways.entity.util.AccountType;
import com.oth.sw.hoffmannairways.service.UserServiceIF;
import com.oth.sw.hoffmannairways.service.impl.AirplaneService;
import com.oth.sw.hoffmannairways.service.impl.FlightService;
import com.oth.sw.hoffmannairways.util.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;

@Controller
public class StartController {
    //TODO order controller?
    @Autowired
    private FlightService flightService;

    @Autowired
    private AirplaneService airplaneService;

    @Autowired
    private UserServiceIF userService;

    @RequestMapping("/")
    //Principal als parameter
    public String start() {
        return "index";
    }



    @RequestMapping("/flights")
    //Principal als parameter
    public String viewFlights(Model model) {

    /*
        Airplane plane = new Airplane("A380", 100, 5000);
        Airplane savedPlane = airplaneService.createPlane(plane);
        plane = new Airplane("A320", 500, 5000);
        airplaneService.createPlane(plane);
        plane = new Airplane("737 MAX", 1000, 5000);
        airplaneService.createPlane(plane);

        FlightConnection connection = new FlightConnection("LH2370", "MUC", "DUB", 2.0);
        FlightConnection conn = flightService.createFlightConnection(connection);
        connection = new FlightConnection("LH457", "LAX", "FRA", 11.0);
        flightService.createFlightConnection(connection);

     */



        Collection<Flight> flightList = flightService.listAllFlights();
        model.addAttribute("flights", flightList);
        model.addAttribute("order", new Order());
        return "flights";
    }

    @RequestMapping(value = "/flights", method = RequestMethod.POST)
    //Principal als parameter
    public String createOrder(Model model, @ModelAttribute("order") Order o, Principal principal) {
        //TODO check if values are null, return errors
        System.out.println(o.toString());
        String message = "Booking failed.";
        String alertClass = "alert-danger";

        if(principal != null) {
            User currUser = userService.getUserByUsername(principal.getName());
            if(currUser != null) {
                o.setCustomer(currUser);
                Flight f = flightService.bookFlight(o);
                if(f != null) {
                    message = "Successfully booked flight " + f.getConnection().getFlightNumber() + " leaving on " + Helper.getFormattedDate(f.getDepartureTime());
                    alertClass = "alert-success";
                }
            }
        }

        //TODO source https://stackoverflow.com/questions/46744586/thymeleaf-show-a-success-message-after-clicking-on-submit-button

        model.addAttribute("message", message);
        model.addAttribute("alertClass", alertClass);

        return viewFlights(model);
    }

    @RequestMapping(value = "/orders", method = RequestMethod.GET)
    //Principal als parameter
    public String viewOrders(Model model, Principal principal) {
        //TODO check if values are null, return errors
        Collection<Order> upcomingOrders = new ArrayList<>();
        Collection<Order> pastOrders = new ArrayList<>();

        if(principal != null) {
            String username = principal.getName();
            User user = userService.getUserByUsername(username);
            if(user != null) {
                if(user.getAccountType() == AccountType.STAFF) {
                    upcomingOrders = flightService.getAllFutureOrders();
                    pastOrders = flightService.getAllPastOrders();
                }
                else {
                    upcomingOrders = flightService.getAllFutureOrders(username);
                    pastOrders = flightService.getAllPastOrders(username);

                }
            }
        }
        model.addAttribute("pastOrders", pastOrders);
        model.addAttribute("upcomingOrders", upcomingOrders);



        return "orders";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET) // /login
    public String login(Model model) {
        model.addAttribute("user", new User());
        //userService.registerUser(new User("test", "123", "Max MusterMann",
               // AccountType.STAFF));
        /*userService.registerUser(new User("user", "123", "Max MusterMann",
        AccountType.USER));

         */
        return "login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST) // th:action="@{/login}"
    public String doLogin() {
        return "index";
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET) // /login
    public String register(Model model) {
        model.addAttribute("user", new User());

        return "register";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST) // /login
    public String doRegister(Model model, @ModelAttribute("user") User user) {
        user.setAccountType(AccountType.USER);
        //TODO error handling, input validation
        userService.registerUser(user);

        return login(model);
    }



}
