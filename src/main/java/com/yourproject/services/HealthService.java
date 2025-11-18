package com.yourproject.services;

import com.yourproject.models.HealthCheckResponse;
import org.springframework.stereotype.Service;

@Service
public class HealthService extends BaseService {
    public HealthCheckResponse getHealth() {
        logger.info("Health check requested");
        return HealthCheckResponse.healthy();
    }
}
