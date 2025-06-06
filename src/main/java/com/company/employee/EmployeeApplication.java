package com.company.employee;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EmployeeApplication {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeApplication.class);

    public static void main(String[] args) {
        logger.info("Starting EmployeeApplication...");
        SpringApplication.run(EmployeeApplication.class, args);
        logger.info("EmployeeApplication started successfully.");
    }
}