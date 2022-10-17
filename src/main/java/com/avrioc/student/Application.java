package com.avrioc.student;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * SpringBoot main application class responsible for starting the server.
 */
@SpringBootApplication
@EnableMongoRepositories
public class Application {

    /**
     * main method for running the application.
     *
     * @param args
     */
    public static void main(String[] args) {

        SpringApplication.run(Application.class, args);

    }
}
