package com.oth.sw.hoffmannairways.web;

import com.oth.sw.hoffmannairways.entity.User;
import com.oth.sw.hoffmannairways.entity.util.AccountType;
import com.oth.sw.hoffmannairways.service.UserServiceIF;
import com.oth.sw.hoffmannairways.service.exception.UserException;
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

@Controller
@Scope("singleton")
public class SecurityController {

    @Autowired
    private UserServiceIF userService;


    @RequestMapping(value = "/login", method = RequestMethod.GET) // /login
    public String login(Model model) {
        model.addAttribute("user", new User());

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
    public String doRegister(Model model, @Valid @ModelAttribute("user") User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("UIMessage", new UIMessage("Not all fields were filled out properly.", "alert-danger"));
            return register(model);

        } else {
            user.setAccountType(AccountType.USER);
            try {
                userService.registerUser(user);
            } catch (UserException e) {
                //TODO
            }
        }

        return login(model);
    }


}

