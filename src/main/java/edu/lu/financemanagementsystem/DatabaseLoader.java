package edu.lu.financemanagementsystem;

import edu.lu.financemanagementsystem.model.User;
import edu.lu.financemanagementsystem.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
public class DatabaseLoader implements CommandLineRunner {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public DatabaseLoader(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... strings) {
        String email = "admin@fms.lv";

        // Check if the user already exists
        if (this.repository.findByEmail(email).isEmpty()) {
            // If not, create the user
            User user = new User();
            user.setFirstName("Jānis");
            user.setLastName("Bānis");
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode("secret"));
            user.setDeleted(false);
            user.setCreatedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
            user.setUpdatedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));

            // Save the user
            this.repository.save(user);
        }
    }
}