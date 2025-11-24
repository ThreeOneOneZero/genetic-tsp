package com.yourproject.services.operators;

import com.yourproject.models.City;
import com.yourproject.models.Route;

import java.util.Random;

/**
 * Mutation operator for genetic algorithm
 * 
 * Uses swap mutation: randomly selects two cities and swaps their positions
 */
public class MutationOperator {
    
    private final Random random;

    public MutationOperator() {
        this.random = new Random();
    }

    public MutationOperator(long seed) {
        this.random = new Random(seed);
    }

    /**
     * Mutates a route by swapping two random cities
     * 
     * @param route Route to mutate
     * @param mutationRate Probability of mutation (0.0 to 1.0)
     * @return Mutated route (or original if mutation didn't occur)
     */
    public Route mutate(Route route, double mutationRate) {
        if (random.nextDouble() > mutationRate) {
            return route; // No mutation
        }

        Route mutated = new Route(route);
        int size = mutated.size();

        if (size < 2) {
            return mutated; // Can't swap with less than 2 cities
        }

        // Select two different random positions
        int pos1 = random.nextInt(size);
        int pos2 = random.nextInt(size);

        // Ensure positions are different
        while (pos1 == pos2) {
            pos2 = random.nextInt(size);
        }

        // Swap cities
        City temp = mutated.getCity(pos1);
        mutated.setCity(pos1, mutated.getCity(pos2));
        mutated.setCity(pos2, temp);

        return mutated;
    }

    /**
     * Alternative mutation: Inversion mutation
     * Reverses the order of cities between two random points
     */
    public Route inversionMutate(Route route, double mutationRate) {
        if (random.nextDouble() > mutationRate) {
            return route;
        }

        Route mutated = new Route(route);
        int size = mutated.size();

        if (size < 2) {
            return mutated;
        }

        int pos1 = random.nextInt(size);
        int pos2 = random.nextInt(size);

        if (pos1 > pos2) {
            int temp = pos1;
            pos1 = pos2;
            pos2 = temp;
        }

        // Reverse segment
        while (pos1 < pos2) {
            City temp = mutated.getCity(pos1);
            mutated.setCity(pos1, mutated.getCity(pos2));
            mutated.setCity(pos2, temp);
            pos1++;
            pos2--;
        }

        return mutated;
    }

    /**
     * Scramble mutation: randomly shuffles cities in a segment
     */
    public Route scrambleMutate(Route route, double mutationRate) {
        if (random.nextDouble() > mutationRate) {
            return route;
        }

        Route mutated = new Route(route);
        int size = mutated.size();

        if (size < 2) {
            return mutated;
        }

        int pos1 = random.nextInt(size);
        int pos2 = random.nextInt(size);

        if (pos1 > pos2) {
            int temp = pos1;
            pos1 = pos2;
            pos2 = temp;
        }

        // Extract segment
        City[] segment = new City[pos2 - pos1 + 1];
        for (int i = 0; i < segment.length; i++) {
            segment[i] = mutated.getCity(pos1 + i);
        }

        // Shuffle segment
        for (int i = segment.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            City temp = segment[i];
            segment[i] = segment[j];
            segment[j] = temp;
        }

        // Put back
        for (int i = 0; i < segment.length; i++) {
            mutated.setCity(pos1 + i, segment[i]);
        }

        return mutated;
    }
}
