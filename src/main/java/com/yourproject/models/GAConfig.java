package com.yourproject.models;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * Configuration for the Genetic Algorithm
 */
public class GAConfig {
    
    @NotNull
    @Min(100)
    private Integer populationSize = 100;

    @NotNull
    @Min(60)
    @Max(80)
    private Double crossoverRate = 70.0;

    @NotNull
    private Double mutationRate = 0.8;
    @Min(20)
    private Integer maxGenerations = 100;

    @NotNull
    @Min(0)
    @Max(20)
    private Integer elitismCount = 5; 

    @NotNull
    @Min(0)
    @Max(100)
    private Double generationGap = 0.9; 

    @NotNull
    @Min(1)
    private Integer crossoverPoint1 = 2;

    @NotNull
    @Min(2)
    private Integer crossoverPoint2 = 5;

    private String startCityId = "F";

    public GAConfig() {}

    // Getters and Setters
    public Integer getPopulationSize() {
        return populationSize;
    }

    public void setPopulationSize(Integer populationSize) {
        this.populationSize = populationSize;
    }

    public Double getCrossoverRate() {
        return crossoverRate;
    }

    public void setCrossoverRate(Double crossoverRate) {
        this.crossoverRate = crossoverRate;
    }

    public Double getMutationRate() {
        return mutationRate;
    }

    public void setMutationRate(Double mutationRate) {
        this.mutationRate = mutationRate;
    }

    public Integer getMaxGenerations() {
        return maxGenerations;
    }

    public void setMaxGenerations(Integer maxGenerations) {
        this.maxGenerations = maxGenerations;
    }

    public Integer getElitismCount() {
        return elitismCount;
    }

    public void setElitismCount(Integer elitismCount) {
        this.elitismCount = elitismCount;
    }

    public Double getGenerationGap() {
        return generationGap;
    }

    public void setGenerationGap(Double generationGap) {
        this.generationGap = generationGap;
    }

    public Integer getCrossoverPoint1() {
        return crossoverPoint1;
    }

    public void setCrossoverPoint1(Integer crossoverPoint1) {
        this.crossoverPoint1 = crossoverPoint1;
    }

    public Integer getCrossoverPoint2() {
        return crossoverPoint2;
    }

    public void setCrossoverPoint2(Integer crossoverPoint2) {
        this.crossoverPoint2 = crossoverPoint2;
    }

    public String getStartCityId() {
        return startCityId;
    }

    public void setStartCityId(String startCityId) {
        this.startCityId = startCityId;
    }

    @Override
    public String toString() {
        return "GAConfig{" +
                "populationSize=" + populationSize +
                ", crossoverRate=" + crossoverRate +
                ", mutationRate=" + mutationRate +
                ", maxGenerations=" + maxGenerations +
                ", elitismCount=" + elitismCount +
                ", generationGap=" + generationGap +
                ", crossoverPoints=[" + crossoverPoint1 + "," + crossoverPoint2 + "]" +
                ", startCity='" + startCityId + '\'' +
                '}';
    }
}
