package com.yourproject.services.operators;

import com.yourproject.models.City;
import com.yourproject.models.Route;

import java.util.*;

/**
 * Partially Mapped Crossover (PMX) operator
 * 
 * PMX is a crossover operator for permutation-based problems like TSP.
 * It preserves the relative order of cities while allowing genetic mixing.
 */
public class PMXCrossover {

    /**
     * Performs PMX crossover between two parent routes
     * 
     * @param parent1 First parent route
     * @param parent2 Second parent route
     * @param point1 First crossover point
     * @param point2 Second crossover point
     * @return Array with two offspring routes
     */
    public static Route[] crossover(Route parent1, Route parent2, int point1, int point2) {
        int size = parent1.size();
        
        // Validate crossover points
        if (point1 >= point2 || point1 < 0 || point2 > size) {
            throw new IllegalArgumentException("Invalid crossover points");
        }

        // Create offspring
        Route offspring1 = createOffspring(parent1, parent2, point1, point2);
        Route offspring2 = createOffspring(parent2, parent1, point1, point2);

        return new Route[]{offspring1, offspring2};
    }

    private static Route createOffspring(Route parent1, Route parent2, int point1, int point2) {
        int size = parent1.size();
        List<City> cities = new ArrayList<>(Collections.nCopies(size, null));
        
        // Copy segment from parent1 to offspring
        for (int i = point1; i < point2; i++) {
            cities.set(i, parent1.getCity(i));
        }

        // Create mapping from parent2 to parent1 in the crossover segment
        Map<City, City> mapping = new HashMap<>();
        for (int i = point1; i < point2; i++) {
            mapping.put(parent2.getCity(i), parent1.getCity(i));
        }

        // Fill remaining positions from parent2
        for (int i = 0; i < size; i++) {
            if (i >= point1 && i < point2) {
                continue; // Skip already filled segment
            }

            City candidate = parent2.getCity(i);
            
            // Follow mapping chain if city already exists in offspring
            while (cities.contains(candidate)) {
                City mapped = mapping.get(candidate);
                if (mapped == null) {
                    // If no mapping exists, this city is not in the crossover segment
                    // Find any city from parent2 that is not yet in offspring
                    for (City city : parent2.getCities()) {
                        if (!cities.contains(city)) {
                            candidate = city;
                            break;
                        }
                    }
                    break;
                }
                candidate = mapped;
            }

            cities.set(i, candidate);
        }

        // Validate: ensure no null cities
        for (int i = 0; i < size; i++) {
            if (cities.get(i) == null) {
                // Find missing city from parent2
                for (City city : parent2.getCities()) {
                    if (!cities.contains(city)) {
                        cities.set(i, city);
                        break;
                    }
                }
            }
        }

        return new Route(parent1.getStartCity(), cities);
    }
}
