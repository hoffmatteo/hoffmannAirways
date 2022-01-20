package com.oth.sw.hoffmannairways.web;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Scope("singleton")
public class StartController {

    @RequestMapping("/")
    //Principal als parameter
    public String start() {
        return "index";
    }

}
