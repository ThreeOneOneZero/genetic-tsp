package com.yourproject.models;

import java.util.List;

/**
 * Result of a single generation execution
 */
public class GenerationResult {
    private final int generation;
    private final Route bestRoute;
    private final double bestDistance;
    private final double averageDistance;
    private final double worstDistance;
    private final List<Route> topRoutes;

    public GenerationResult(
            int generation,
            Route bestRoute,
            double bestDistance,
            double averageDistance,
            double worstDistance,
            List<Route> topRoutes) {
        this.generation = generation;
        this.bestRoute = bestRoute;
        this.bestDistance = bestDistance;
        this.averageDistance = averageDistance;
        this.worstDistance = worstDistance;
        this.topRoutes = topRoutes;
    }

    public int getGeneration() {
        return generation;
    }

    public Route getBestRoute() {
        return bestRoute;
    }

    public double getBestDistance() {
        return bestDistance;
    }

    public double getAverageDistance() {
        return averageDistance;
    }

    public double getWorstDistance() {
        return worstDistance;
    }

    public List<Route> getTopRoutes() {
        return topRoutes;
    }
}
