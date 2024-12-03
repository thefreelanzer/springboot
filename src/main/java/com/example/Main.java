package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class Main {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @GetMapping("/")
    public GreetResponse greet() {
        return new GreetResponse("Hello!");
    }

    @GetMapping("/check-db")
    public String checkDatabaseConnection() {
        System.out.println("Checking database connection...");  // Debug message
        try {
            jdbcTemplate.execute("SELECT * FROM company");
            return "MySQL connection is working!";
        } catch (Exception e) {
            return "Error connecting to MySQL: " + e.getMessage();
        }
    }

    record GreetResponse(String greet) {
    }
}
