package com.yourproject.services;

import com.yourproject.models.*;
import com.yourproject.services.operators.MutationOperator;
import com.yourproject.services.operators.PMXCrossover;
import com.yourproject.services.operators.SelectionOperator;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Serviço de Algoritmo Genético para resolver o TSP
 */
@Service
public class GeneticAlgorithmService extends BaseService {

    private final MutationOperator mutationOperator;
    private final SelectionOperator selectionOperator;
    private final Random random;

    // Graph data - will be loaded from configuration
    private Map<String, City> cities;
    private GAConfig currentConfig;
    private Population currentPopulation;
    private List<GenerationResult> history;

    public GeneticAlgorithmService() {
        this.mutationOperator = new MutationOperator();
        this.selectionOperator = new SelectionOperator();
        this.random = new Random();
        this.history = new ArrayList<>();
        initializeGraph();
    }

    private void initializeGraph() {
        cities = new HashMap<>();
        
        cities.put("F", new City("F", "F", 100, 300));
        cities.put("G", new City("G", "G", 400, 100));
        cities.put("H", new City("H", "H", 350, 250));
        cities.put("E", new City("E", "E", 300, 200));
        cities.put("K", new City("K", "K", 250, 150));
        cities.put("N", new City("N", "N", 150, 200));
        cities.put("C", new City("C", "C", 200, 250));
        cities.put("L", new City("L", "L", 150, 300));
        
        logger.info("Grafo inicializado com {} cidades", cities.size());
    }

    public Population initializePopulation(GAConfig config) {
        try {
            this.currentConfig = config;
            this.history.clear();
            
            logger.info("Inicializando população: {}", config);
            
            City startCity = cities.get(config.getStartCityId());
            if (startCity == null) {
                throw new IllegalArgumentException("Cidade inicial inválida: " + config.getStartCityId());
            }

            List<City> availableCities = new ArrayList<>(cities.values());
            availableCities.remove(startCity);

            Population population = new Population(config.getPopulationSize());

            for (int i = 0; i < config.getPopulationSize(); i++) {
                List<City> routeCities = new ArrayList<>(availableCities);
                Collections.shuffle(routeCities, random);
                Route route = new Route(startCity, routeCities);
                population.addRoute(route);
            }

            this.currentPopulation = population;
            
            logger.info("População inicial: {} indivíduos, melhor distância: {}", 
                       population.size(), population.getBestDistance());
            
            return population;
        } catch (Exception e) {
            logger.error("Error initializing population", e);
            throw e;
        }
    }

    public GenerationResult evolveGeneration(int generationNumber) {
        if (currentPopulation == null || currentConfig == null) {
            throw new IllegalStateException("População não inicializada");
        }

        Population newPopulation = new Population(currentConfig.getPopulationSize());

        List<Route> elites = selectionOperator.selectElite(
            currentPopulation, 
            currentConfig.getElitismCount()
        );
        elites.forEach(newPopulation::addRoute);

        int offspringCount = (int) (currentConfig.getPopulationSize() * 
                                   currentConfig.getGenerationGap() / 100.0);
        offspringCount = Math.max(offspringCount, 
                                 currentConfig.getPopulationSize() - currentConfig.getElitismCount());

        while (newPopulation.size() < currentConfig.getPopulationSize()) {
            Route parent1 = selectionOperator.tournamentSelection(currentPopulation, 5);
            Route parent2 = selectionOperator.tournamentSelection(currentPopulation, 5);

            Route offspring1, offspring2;

            if (random.nextDouble() < currentConfig.getCrossoverRate() / 100.0) {
                Route[] offspring = PMXCrossover.crossover(
                    parent1, parent2,
                    currentConfig.getCrossoverPoint1(),
                    currentConfig.getCrossoverPoint2()
                );
                offspring1 = offspring[0];
                offspring2 = offspring[1];
            } else {
                offspring1 = new Route(parent1);
                offspring2 = new Route(parent2);
            }

            offspring1 = mutationOperator.mutate(offspring1, currentConfig.getMutationRate() / 100.0);
            offspring2 = mutationOperator.mutate(offspring2, currentConfig.getMutationRate() / 100.0);

            if (newPopulation.size() < currentConfig.getPopulationSize()) {
                newPopulation.addRoute(offspring1);
            }
            if (newPopulation.size() < currentConfig.getPopulationSize()) {
                newPopulation.addRoute(offspring2);
            }
        }

        currentPopulation = newPopulation;

        GenerationResult result = new GenerationResult(
            generationNumber,
            currentPopulation.getBestRoute(),
            currentPopulation.getBestDistance(),
            currentPopulation.getAverageDistance(),
            currentPopulation.getWorstDistance(),
            currentPopulation.getTopRoutes(10)
        );

        history.add(result);

        logger.info("Geração {}: Melhor={}, Média={}, Pior={}", 
                   generationNumber,
                   String.format("%.2f", result.getBestDistance()),
                   String.format("%.2f", result.getAverageDistance()),
                   String.format("%.2f", result.getWorstDistance()));

        return result;
    }

    public GAExecutionResult runGeneticAlgorithm(GAConfig config) {
        try {
            long startTime = System.currentTimeMillis();
            
            initializePopulation(config);
            
            // Record initial generation
            GenerationResult initialResult = new GenerationResult(
                0,
                currentPopulation.getBestRoute(),
                currentPopulation.getBestDistance(),
                currentPopulation.getAverageDistance(),
                currentPopulation.getWorstDistance(),
                currentPopulation.getTopRoutes(10)
            );
            history.add(initialResult);

            for (int gen = 1; gen <= config.getMaxGenerations(); gen++) {
                evolveGeneration(gen);
            }

            long endTime = System.currentTimeMillis();
            long executionTime = endTime - startTime;

            Route bestRoute = currentPopulation.getBestRoute();
            
            logger.info("AG finalizado em {}ms - Melhor rota: {} (distância: {})", 
                       executionTime, bestRoute.getCityNames(), bestRoute.getTotalDistance());

            return new GAExecutionResult(
                bestRoute,
                bestRoute.getTotalDistance(),
                config.getMaxGenerations(),
                new ArrayList<>(history),
                config,
                executionTime
            );
        } catch (Exception e) {
            logger.error("Erro ao executar AG", e);
            throw new RuntimeException("Falha ao executar AG: " + e.getMessage(), e);
        }
    }

    public Map<String, Object> getPopulationStats() {
        if (currentPopulation == null) {
            return Map.of("error", "No population initialized");
        }

        Map<String, Object> stats = new HashMap<>();
        stats.put("size", currentPopulation.size());
        stats.put("bestDistance", currentPopulation.getBestDistance());
        stats.put("averageDistance", currentPopulation.getAverageDistance());
        stats.put("worstDistance", currentPopulation.getWorstDistance());
        stats.put("bestRoute", currentPopulation.getBestRoute().getCityNames());
        
        return stats;
    }

    public List<GenerationResult> getHistory() {
        return new ArrayList<>(history);
    }

    public Population getCurrentPopulation() {
        return currentPopulation;
    }

    public Map<String, City> getCities() {
        return new HashMap<>(cities);
    }

    public void setCities(Map<String, City> cities) {
        this.cities = new HashMap<>(cities);
        logger.info("Grafo atualizado: {} cidades", cities.size());
    }
}
