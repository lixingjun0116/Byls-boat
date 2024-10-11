package com.byls.boat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@ComponentScan(basePackages = "com.byls")
@SpringBootApplication
public class BoatApplication {

    public static void main(String[] args) {
        SpringApplication.run(BoatApplication.class, args);
    }

}
