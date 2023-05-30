package edu.lu.financemanagementsystem;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((authorizeHttpRequests) ->
                authorizeHttpRequests
                        .requestMatchers("/").permitAll()
                        .requestMatchers("/login").anonymous()
                        .requestMatchers("/register").anonymous()
                        .requestMatchers("/logout").authenticated()
                        .requestMatchers("/static/**").permitAll()
                        .requestMatchers("/css/**").permitAll()
                        .requestMatchers("/images/**").permitAll()
                        .anyRequest().authenticated()
        ).formLogin((formLogin) -> formLogin
                .usernameParameter("email")
                .passwordParameter("password")
                .loginPage("/login")
                .failureUrl("/login?error=true")
                .defaultSuccessUrl("/dashboard")
                .successHandler((request, response, authentication) -> {
                    if (authentication != null && authentication.isAuthenticated()) {
                        response.sendRedirect("/dashboard");
                    }
                })
        ).logout(Customizer.withDefaults());
        return http.build();
    }
}
