package com.yourproject.controllers;

import com.yourproject.models.ApiResponse;
import com.yourproject.models.HealthCheckResponse;
import com.yourproject.services.HealthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
public class HealthController extends BaseController {
    private final HealthService healthService;

    public HealthController(HealthService healthService) {
        this.healthService = healthService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<HealthCheckResponse>> getHealth() {
        return ok(healthService.getHealth());
    }
}
