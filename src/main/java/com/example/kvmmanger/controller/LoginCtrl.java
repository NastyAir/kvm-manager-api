package com.example.kvmmanger.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class LoginCtrl {
    @RequestMapping(value = "/api/login", method = RequestMethod.POST)
    public String index() {
        return "forward:/login";
    }
}
