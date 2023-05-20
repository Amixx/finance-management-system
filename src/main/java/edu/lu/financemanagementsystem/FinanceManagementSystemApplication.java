package edu.lu.financemanagementsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories("edu.lu.financemanagementsystem.repository")
@SpringBootApplication()
public class FinanceManagementSystemApplication {
	public static void main(String[] args) {
		SpringApplication.run(FinanceManagementSystemApplication.class, args);
	}
}
