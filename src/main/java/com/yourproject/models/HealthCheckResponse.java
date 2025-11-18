package com.yourproject.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;

public record HealthCheckResponse(
    String status,
    @JsonProperty("timestamp")
    String timestamp,
    @JsonProperty("uptime")
    long uptime
) {
    public static HealthCheckResponse healthy() {
        return new HealthCheckResponse(
            "UP",
            Instant.now().toString(),
            System.currentTimeMillis()
        );
    }
}
