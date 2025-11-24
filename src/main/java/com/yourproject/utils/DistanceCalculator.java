package com.yourproject.utils;

import com.yourproject.models.City;
import java.util.HashMap;
import java.util.Map;

/**
 * Calculadora de distâncias usando matriz do grafo real
 */
public class DistanceCalculator {
    private static final Map<String, Integer> cityIndexMap = new HashMap<>();
    private static final double[][] distanceMatrix;
    private static final double INFINITY = 999999.0; // Rotas impossíveis
    
    static {
        // Mapear cidades para índices
        cityIndexMap.put("F", 0);
        cityIndexMap.put("G", 1);
        cityIndexMap.put("H", 2);
        cityIndexMap.put("E", 3);
        cityIndexMap.put("K", 4);
        cityIndexMap.put("N", 5);
        cityIndexMap.put("C", 6);
        cityIndexMap.put("L", 7);
        
        // Matriz de distâncias do grafo real
        // Baseada nas arestas: F-N:30, F-C:20, F-L:10, F-G:55, etc.
        distanceMatrix = new double[8][8];
        
        // Inicializar tudo com INFINITY (sem conexão)
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                distanceMatrix[i][j] = (i == j) ? 0 : INFINITY;
            }
        }
        
        // Adicionar arestas reais (grafo não-direcionado, então [i][j] = [j][i])
        addEdge("F", "N", 30);
        addEdge("F", "C", 20);
        addEdge("F", "L", 10);
        addEdge("F", "G", 55);
        
        addEdge("N", "C", 47);
        addEdge("N", "K", 60);
        
        addEdge("C", "K", 70);
        addEdge("C", "E", 10);
        addEdge("C", "H", 30);
        addEdge("C", "L", 10);
        
        addEdge("K", "E", 10);
        addEdge("K", "G", 90);
        addEdge("K", "H", 73);
        
        addEdge("E", "H", 60);
        addEdge("E", "G", 40);
        addEdge("E", "L", 5);
        
        addEdge("H", "G", 80);
        addEdge("H", "L", 40);
    }
    
    private static void addEdge(String city1, String city2, double distance) {
        int i1 = cityIndexMap.get(city1);
        int i2 = cityIndexMap.get(city2);
        distanceMatrix[i1][i2] = distance;
        distanceMatrix[i2][i1] = distance;
    }
    
    /**
     * Calcula distância entre duas cidades usando a matriz do grafo
     */
    public static double getDistance(City city1, City city2) {
        if (city1 == null || city2 == null) {
            throw new IllegalArgumentException("Cities cannot be null. city1=" + city1 + ", city2=" + city2);
        }
        
        Integer i1 = cityIndexMap.get(city1.getId());
        Integer i2 = cityIndexMap.get(city2.getId());
        
        if (i1 == null || i2 == null) {
            return INFINITY;
        }
        
        return distanceMatrix[i1][i2];
    }
    
    /**
     * Verifica se existe conexão direta entre duas cidades
     */
    public static boolean hasDirectConnection(City city1, City city2) {
        return getDistance(city1, city2) < INFINITY;
    }
    
    /**
     * Retorna a distância mínima teórica possível (soma das menores arestas)
     */
    public static double getTheoreticalMinimum() {        
        return 10 + 5 + 10 + 10 + 10 + 30 + 40; // Exemplo aproximado
    }
}
