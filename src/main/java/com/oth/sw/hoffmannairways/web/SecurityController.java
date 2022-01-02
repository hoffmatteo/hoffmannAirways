package com.oth.sw.hoffmannairways.web;

import com.oth.sw.hoffmannairways.entity.User;
import com.oth.sw.hoffmannairways.entity.util.AccountType;
import com.oth.sw.hoffmannairways.service.UserServiceIF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class SecurityController {

    @Autowired
    private UserServiceIF userService;

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

