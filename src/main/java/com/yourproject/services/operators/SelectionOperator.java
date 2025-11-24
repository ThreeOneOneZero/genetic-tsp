package com.yourproject.services.operators;

import com.yourproject.models.Population;
import com.yourproject.models.Route;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Selection operator for genetic algorithm
 * 
 * Implements various selection strategies including elitist selection
 */
public class SelectionOperator {
    
    private final Random random;

    public SelectionOperator() {
        this.random = new Random();
    }

    public SelectionOperator(long seed) {
        this.random = new Random(seed);
    }

    /**
     * Elitist selection: keeps the best individuals from current population
     * 
     * @param population Current population
     * @param eliteCount Number of elite individuals to keep
     * @return List of elite routes
     */
    public List<Route> selectElite(Population population, int eliteCount) {
        population.sortByFitness();
        return new ArrayList<>(population.getTopRoutes(eliteCount));
    }

    /**
     * Tournament selection: selects best individual from random subset
     * 
     * @param population Current population
     * @param tournamentSize Size of tournament
     * @return Selected route
     */
    public Route tournamentSelection(Population population, int tournamentSize) {
        Population tournament = new Population(tournamentSize);
        
        for (int i = 0; i < tournamentSize; i++) {
            int randomIndex = random.nextInt(population.size());
            tournament.addRoute(population.getRoute(randomIndex));
        }
        
        return tournament.getBestRoute();
    }

    /**
     * Roulette wheel selection: probability proportional to fitness
     * 
     * @param population Current population
     * @return Selected route
     */
    public Route rouletteSelection(Population population) {
        // Calculate total fitness
        double totalFitness = 0.0;
        for (int i = 0; i < population.size(); i++) {
            totalFitness += population.getRoute(i).getFitness();
        }

        // Random value between 0 and total fitness
        double value = random.nextDouble() * totalFitness;

        // Find individual
        double sum = 0.0;
        for (int i = 0; i < population.size(); i++) {
            sum += population.getRoute(i).getFitness();
            if (sum >= value) {
                return population.getRoute(i);
            }
        }

        // Fallback (should not happen)
        return population.getRoute(population.size() - 1);
    }

    /**
     * Rank selection: selection probability based on rank, not fitness
     * 
     * @param population Current population
     * @return Selected route
     */
    public Route rankSelection(Population population) {
        population.sortByFitness();
        
        int size = population.size();
        // Sum of ranks: 1 + 2 + 3 + ... + n = n(n+1)/2
        int totalRank = size * (size + 1) / 2;
        
        int value = random.nextInt(totalRank);
        int sum = 0;
        
        for (int i = 0; i < size; i++) {
            sum += (size - i); // Higher rank for better individuals
            if (sum >= value) {
                return population.getRoute(i);
            }
        }
        
        return population.getRoute(size - 1);
    }

    /**
     * Stochastic Universal Sampling (SUS)
     * More fair than roulette wheel selection
     * 
     * @param population Current population
     * @param count Number of individuals to select
     * @return List of selected routes
     */
    public List<Route> stochasticUniversalSampling(Population population, int count) {
        List<Route> selected = new ArrayList<>();
        
        // Calculate total fitness
        double totalFitness = 0.0;
        for (int i = 0; i < population.size(); i++) {
            totalFitness += population.getRoute(i).getFitness();
        }

        // Calculate pointer distance
        double distance = totalFitness / count;
        double start = random.nextDouble() * distance;

        double sum = 0.0;
        int index = 0;

        for (int i = 0; i < count; i++) {
            double pointer = start + i * distance;
            
            while (sum < pointer && index < population.size()) {
                sum += population.getRoute(index).getFitness();
                index++;
            }
            
            if (index > 0) {
                selected.add(new Route(population.getRoute(index - 1)));
            }
        }

        return selected;
    }
}
