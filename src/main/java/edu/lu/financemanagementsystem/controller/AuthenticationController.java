package edu.lu.financemanagementsystem.controller;

import edu.lu.financemanagementsystem.dto.UserRegister;
import edu.lu.financemanagementsystem.model.User;
import edu.lu.financemanagementsystem.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/")
public class AuthenticationController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String login(
            @RequestParam(name = "error", required = false) String error,
            Model model)
    {
        if (error != null) {
            model.addAttribute("failed", true);
        }

        return "authentication/login";
    }

    @GetMapping("/register")
    public String showRegistration(
            @RequestParam(name = "error", required = false) String error,
            Model model)
    {
        if (error != null) {
            model.addAttribute("failed", true);
        }

        // Save data in Data Transfer Object
        UserRegister userDto = new UserRegister();
        model.addAttribute("user", userDto);
        return "authentication/register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") @Valid UserRegister userReg) {
        // DTO will handle simple validation,
        // we just need to check for valid emails & passwords.
        if (!validateEmail(userReg.getEmail())) {
            // Invalid email syntax. Could be turned into an @attribute for UserRegister.email
            return "redirect:/register?error=email";
        }

        if (!userReg.getPassword().equals(userReg.getPasswordConfirm())) {
            // Passwords don't match
            return "redirect:/register?error=password";
        }

        // Check if the user already exists
        if (userRepository.findByEmail(userReg.getEmail()).isPresent()) {
            return "redirect:/register?error=email";
        }

        // Create new user
        User user = new User();
        user.setFirstName(userReg.getFirstName());
        user.setLastName(userReg.getLastName());
        user.setEmail(userReg.getEmail());
        user.setPassword(passwordEncoder.encode(userReg.getPassword()));
        user.setDeleted(false);
        user.setCreatedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
        user.setUpdatedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));

        this.userRepository.save(user);

        // TODO: Log in with new user automatically
        return "redirect:/login";
    }

    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-+]+(.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(.[A-Za-z0-9]+)*(.[A-Za-z]{2,})$";
    private static boolean validateEmail(String email) {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
