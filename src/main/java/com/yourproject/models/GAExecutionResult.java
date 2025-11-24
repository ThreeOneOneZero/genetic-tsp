package com.yourproject.models;

import java.util.List;

/**
 * Final result of the genetic algorithm execution
 */
public class GAExecutionResult {
    private final Route bestRoute;
    private final double bestDistance;
    private final int totalGenerations;
    private final List<GenerationResult> generationHistory;
    private final GAConfig config;
    private final long executionTimeMs;

    public GAExecutionResult(
            Route bestRoute,
            double bestDistance,
            int totalGenerations,
            List<GenerationResult> generationHistory,
            GAConfig config,
            long executionTimeMs) {
        this.bestRoute = bestRoute;
        this.bestDistance = bestDistance;
        this.totalGenerations = totalGenerations;
        this.generationHistory = generationHistory;
        this.config = config;
        this.executionTimeMs = executionTimeMs;
    }

    public Route getBestRoute() {
        return bestRoute;
    }

    public double getBestDistance() {
        return bestDistance;
    }

    public int getTotalGenerations() {
        return totalGenerations;
    }

    public List<GenerationResult> getGenerationHistory() {
        return generationHistory;
    }

    public GAConfig getConfig() {
        return config;
    }

    public long getExecutionTimeMs() {
        return executionTimeMs;
    }
}
