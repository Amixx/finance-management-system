package edu.lu.financemanagementsystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/")
public class LandingController {

    @GetMapping
    public String getLandingPage() {
        return "index";
    }
}
