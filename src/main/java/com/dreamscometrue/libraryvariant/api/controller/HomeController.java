package com.dreamscometrue.libraryvariant.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("")
public class HomeController {
    @GetMapping("/home")
    public ModelAndView home() {
        return new ModelAndView("home");
    }
}
