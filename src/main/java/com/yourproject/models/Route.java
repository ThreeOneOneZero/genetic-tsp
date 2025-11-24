package com.yourproject.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a route (individual) in the genetic algorithm
 * A route is a sequence of cities forming a complete tour
 */
public class Route implements Comparable<Route> {
    private final List<City> cities;
    private final City startCity;
    private Double totalDistance;
    private Double fitness;

    public Route(City startCity, List<City> cities) {
        this.startCity = startCity;
        this.cities = new ArrayList<>(cities);
        this.totalDistance = null;
        this.fitness = null;
    }

    public Route(Route other) {
        this.startCity = other.startCity;
        this.cities = new ArrayList<>(other.cities);
        this.totalDistance = other.totalDistance;
        this.fitness = other.fitness;
    }

    public List<City> getCities() {
        return Collections.unmodifiableList(cities);
    }

    public City getStartCity() {
        return startCity;
    }

    public City getCity(int index) {
        return cities.get(index);
    }

    public void setCity(int index, City city) {
        cities.set(index, city);
        // Invalidate cached values
        totalDistance = null;
        fitness = null;
    }

    public int size() {
        return cities.size();
    }

    public double getTotalDistance() {
        if (totalDistance == null) {
            totalDistance = calculateTotalDistance();
        }
        return totalDistance;
    }

    public double getFitness() {
        if (fitness == null) {
            double distance = getTotalDistance();
            if (distance == 0) {
                fitness = 0.0; // Rota inválida
            } else if (Double.isInfinite(distance) || Double.isNaN(distance)) {
                fitness = 0.0; // Rota impossível
            } else {
                fitness = 1.0 / distance;
            }
        }
        return fitness;
    }

    private double calculateTotalDistance() {
        double distance = 0.0;
        
        distance += getDistance(startCity, cities.get(0));
        
        for (int i = 0; i < cities.size() - 1; i++) {
            distance += getDistance(cities.get(i), cities.get(i + 1));
        }
        
        distance += getDistance(cities.get(cities.size() - 1), startCity);
        
        return distance;
    }

    private double getDistance(City city1, City city2) {
        return com.yourproject.utils.DistanceCalculator.getDistance(city1, city2);
    }

    public boolean containsCity(City city) {
        return cities.contains(city);
    }

    @Override
    public int compareTo(Route other) {
        return Double.compare(this.getTotalDistance(), other.getTotalDistance());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(startCity.getName());
        for (City city : cities) {
            sb.append(" -> ").append(city.getName());
        }
        sb.append(" -> ").append(startCity.getName());
        sb.append(" (Distance: ").append(String.format("%.2f", getTotalDistance())).append(")");
        return sb.toString();
    }

    public List<String> getCityNames() {
        List<String> names = new ArrayList<>();
        names.add(startCity.getName());
        for (City city : cities) {
            names.add(city.getName());
        }
        names.add(startCity.getName());
        return names;
    }
}
