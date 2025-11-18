package com.yourproject.config;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
public class EnvironmentConfig {
    private static final Logger logger = LoggerFactory.getLogger(EnvironmentConfig.class);

    @Bean
    public ApplicationRunner validateEnvironment() {
        return args -> {
            logger.info("Validating required environment variables...");

            String[] requiredVars = {
                "APP_ENV",
                "APP_NAME"
            };

            for (String var : requiredVars) {
                String value = System.getenv(var);
                if (value == null || value.isBlank()) {
                    logger.warn("Environment variable not set (optional): {}", var);
                }
            }

            logger.info("Environment validation complete");
        };
    }
}
