package com.acme.backendfreshsense;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;

@SpringBootApplication(exclude = {UserDetailsServiceAutoConfiguration.class})
public class BackendFreshSenseApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendFreshSenseApplication.class, args);
    }

}
