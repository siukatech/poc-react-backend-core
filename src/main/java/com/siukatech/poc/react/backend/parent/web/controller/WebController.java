package com.siukatech.poc.react.backend.parent.web.controller;


import com.siukatech.poc.react.backend.parent.web.annotation.v1.PublicApiV1Controller;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Slf4j
@PublicApiV1Controller
// This is not working
//@ConditionalOnMissingBean(name = {"webController"})
public class WebController {

    @GetMapping(path = "/")
    public String index() {
        return "external";
    }

    @GetMapping(path = "/authorized")
    public String authorized(Principal principal, Model model) {
        //addCustomers();
        //model.addAttribute("customers", customerDAO.findAll());
        model.addAttribute("username", principal.getName());
        return principal.getName();
    }

}
