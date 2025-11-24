package com.yourproject.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a population of routes in the genetic algorithm
 */
public class Population {
    private final List<Route> routes;
    private boolean sorted;

    public Population(int size) {
        this.routes = new ArrayList<>(size);
        this.sorted = false;
    }

    public Population(List<Route> routes) {
        this.routes = new ArrayList<>(routes);
        this.sorted = false;
    }

    public void addRoute(Route route) {
        routes.add(route);
        sorted = false;
    }

    public Route getRoute(int index) {
        return routes.get(index);
    }

    public void setRoute(int index, Route route) {
        routes.set(index, route);
        sorted = false;
    }

    public int size() {
        return routes.size();
    }

    public List<Route> getRoutes() {
        return Collections.unmodifiableList(routes);
    }

    public void sortByFitness() {
        if (!sorted) {
            Collections.sort(routes);
            sorted = true;
        }
    }

    public Route getBestRoute() {
        sortByFitness();
        return routes.get(0);
    }

    public Route getWorstRoute() {
        sortByFitness();
        return routes.get(routes.size() - 1);
    }

    public double getAverageDistance() {
        return routes.stream()
                .mapToDouble(Route::getTotalDistance)
                .average()
                .orElse(0.0);
    }

    public double getBestDistance() {
        return getBestRoute().getTotalDistance();
    }

    public double getWorstDistance() {
        return getWorstRoute().getTotalDistance();
    }

    public List<Route> getTopRoutes(int n) {
        sortByFitness();
        int count = Math.min(n, routes.size());
        return new ArrayList<>(routes.subList(0, count));
    }
}
