package com.yourproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import java.util.Arrays;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
        Environment env = app.run(args).getEnvironment();

        String activeProfiles = Arrays.toString(env.getActiveProfiles());
        String applicationName = env.getProperty("spring.application.name");
        String port = env.getProperty("server.port", "8080");

        System.out.println("\n" +
            "==================================================================\n" +
            applicationName + " started successfully\n" +
            "Profile: " + activeProfiles + "\n" +
            "URL: http://localhost:" + port + "\n" +
            "Health: http://localhost:" + port + "/api/health\n" +
            "==================================================================\n");
    }
}
