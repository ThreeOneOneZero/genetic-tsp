package com.yourproject.controllers;

import com.yourproject.models.*;
import com.yourproject.services.GeneticAlgorithmService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * TSP Genetic Algorithm API Controller
 * 
 * Provides endpoints to:
 * - Configure and run the genetic algorithm
 * - Get current population statistics
 * - View generation history
 * - Get the best route found
 */
@RestController
@RequestMapping("/tsp")
public class TSPController extends BaseController {

    private final GeneticAlgorithmService gaService;

    public TSPController(GeneticAlgorithmService gaService) {
        this.gaService = gaService;
    }

    /**
     * Initialize a new population with given configuration
     * 
     * POST /api/tsp/initialize
     * 
     * @param config GA configuration
     * @return Population statistics
     */
    @PostMapping("/initialize")
    public ResponseEntity<ApiResponse<Map<String, Object>>> initializePopulation(
            @Valid @RequestBody GAConfig config) {
        
        gaService.initializePopulation(config);
        Map<String, Object> stats = gaService.getPopulationStats();
        
        return ok(stats);
    }

    /**
     * Evolve population for one generation
     * 
     * POST /api/tsp/evolve
     * 
     * @param generation Generation number
     * @return Generation result with statistics
     */
    @PostMapping("/evolve")
    public ResponseEntity<ApiResponse<GenerationResult>> evolveGeneration(
            @RequestParam(defaultValue = "1") int generation) {
        
        if (gaService.getCurrentPopulation() == null) {
            throw new IllegalStateException("Population not initialized. Call /initialize or /run first.");
        }
        
        GenerationResult result = gaService.evolveGeneration(generation);
        return ok(result);
    }

    /**
     * Run complete genetic algorithm
     * 
     * POST /api/tsp/run
     * 
     * @param config GA configuration
     * @return Complete execution result
     */
    @PostMapping("/run")
    public ResponseEntity<ApiResponse<GAExecutionResult>> runGeneticAlgorithm(
            @Valid @RequestBody GAConfig config) {
        
        if (config.getMutationRate() < 0.5 || config.getMutationRate() > 1.0) {
            throw new IllegalArgumentException("Taxa de mutação deve estar entre 0.5% e 1.0% (enunciado)");
        }
        
        GAExecutionResult result = gaService.runGeneticAlgorithm(config);
        return ok(result);
    }

    /**
     * Get current population statistics
     * 
     * GET /api/tsp/stats
     * 
     * @return Population statistics
     */
    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getPopulationStats() {
        if (gaService.getCurrentPopulation() == null) {
            throw new IllegalStateException("Population not initialized. Call /initialize or /run first.");
        }
        
        Map<String, Object> stats = gaService.getPopulationStats();
        return ok(stats);
    }

    /**
     * Get generation history
     * 
     * GET /api/tsp/history
     * 
     * @return List of generation results
     */
    @GetMapping("/history")
    public ResponseEntity<ApiResponse<List<GenerationResult>>> getHistory() {
        List<GenerationResult> history = gaService.getHistory();
        
        if (history.isEmpty()) {
            throw new IllegalStateException("No generation history available. Call /run first.");
        }
        
        return ok(history);
    }

    /**
     * Get current population (top N routes)
     * 
     * GET /api/tsp/population
     * 
     * @param top Number of top routes to return (default 10)
     * @return Top routes from current population
     */
    @GetMapping("/population")
    public ResponseEntity<ApiResponse<List<Route>>> getPopulation(
            @RequestParam(defaultValue = "10") int top) {
        
        Population population = gaService.getCurrentPopulation();
        if (population == null) {
            return ok(List.of());
        }
        
        List<Route> topRoutes = population.getTopRoutes(top);
        return ok(topRoutes);
    }

    /**
     * Get best route from current population
     * 
     * GET /api/tsp/best
     * 
     * @return Best route
     */
    @GetMapping("/best")
    public ResponseEntity<ApiResponse<Route>> getBestRoute() {
        Population population = gaService.getCurrentPopulation();
        if (population == null) {
            return ok(null);
        }
        
        Route bestRoute = population.getBestRoute();
        return ok(bestRoute);
    }

    /**
     * Get available cities in the graph
     * 
     * GET /api/tsp/cities
     * 
     * @return Map of cities
     */
    @GetMapping("/cities")
    public ResponseEntity<ApiResponse<Map<String, City>>> getCities() {
        Map<String, City> cities = gaService.getCities();
        return ok(cities);
    }

    /**
     * Set custom cities (for testing different graphs)
     * 
     * POST /api/tsp/cities
     * 
     * @param cities Map of cities to set
     * @return Confirmation message
     */
    @PostMapping("/cities")
    public ResponseEntity<ApiResponse<Map<String, String>>> setCities(
            @RequestBody Map<String, City> cities) {
        
        gaService.setCities(cities);
        
        Map<String, String> response = Map.of(
            "message", "Cities updated successfully",
            "count", String.valueOf(cities.size())
        );
        
        return ok(response);
    }

    /**
     * Get default GA configuration
     * 
     * GET /api/tsp/config/default
     * 
     * @return Default GA configuration
     */
    @GetMapping("/config/default")
    public ResponseEntity<ApiResponse<GAConfig>> getDefaultConfig() {
        GAConfig config = new GAConfig();
        return ok(config);
    }
}
