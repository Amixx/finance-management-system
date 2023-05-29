package edu.lu.financemanagementsystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class AuthenticationController {
    @GetMapping("/login")
    public String login(
            @RequestParam(name = "error", required = false) String error,
            Model model) {
        if (error != null) {
            model.addAttribute("failed", true);
        }
        return "authentication/login";
    }
}
