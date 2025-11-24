# Genetic TSP - Algoritmo Genético para Problema do Caixeiro Viajante

Backend Java Spring Boot para resolver o Problema do Caixeiro Viajante usando Algoritmos Genéticos.

> **Status:** Backend completo e testado com grafo real  
> **Próximo passo:** Integrar frontend React

📚 **Ver:** `INDICE.md` para navegação completa dos documentos

## ��� Descrição do Trabalho

Implementação de Algoritmo Genético (AG) para resolver o Problema do Caixeiro Viajante (PCV) em um grafo com 8 cidades, conforme especificação do trabalho T3 de Grafos.

### Requisitos Implementados

✅ **População**: Mínimo de 100 indivíduos (configurável)  
✅ **Taxa de Cruzamento**: 60%-80% (configurável)  
✅ **Taxa de Mutação**: 0.5%-1% (configurável)  
✅ **Cruzamento**: PMX (Partially Mapped Crossover) em 2 pontos fixos  
✅ **Seleção**: Estratégia elitista  
✅ **Critério de Parada**: Número máximo de gerações (mínimo 20, configurável)

## ��� Tecnologias

- **Java 21**
- **Spring Boot 3.5.0**
- **Maven**
- **REST API** para integração com frontend React

## ��� Estrutura do Projeto

```
src/main/java/com/yourproject/
├── Application.java                    # Ponto de entrada
├── controllers/
│   ├── BaseController.java            # Controller base
│   ├── HealthController.java          # Health check
│   └── TSPController.java             # API do TSP ⭐
├── services/
│   ├── BaseService.java               # Service base
│   ├── HealthService.java
│   ├── GeneticAlgorithmService.java   # Lógica principal do AG ⭐
│   └── operators/
│       ├── PMXCrossover.java          # Operador de cruzamento ⭐
│       ├── MutationOperator.java      # Operador de mutação ⭐
│       └── SelectionOperator.java     # Operador de seleção ⭐
├── models/
│   ├── City.java                      # Representa uma cidade ⭐
│   ├── Route.java                     # Representa uma rota (indivíduo) ⭐
│   ├── Population.java                # População de rotas ⭐
│   ├── GAConfig.java                  # Configuração do AG ⭐
│   ├── GenerationResult.java          # Resultado de uma geração ⭐
│   └── GAExecutionResult.java         # Resultado final da execução ⭐
├── errors/                            # Sistema de erros
└── middleware/
    └── GlobalExceptionHandler.java    # Tratamento global de erros
```

## ��� Endpoints da API

### Configuração e Execução

#### `POST /api/tsp/run`

Executa o algoritmo genético completo

```json
{
  "populationSize": 100,
  "crossoverRate": 70.0,
  "mutationRate": 0.8,
  "maxGenerations": 100,
  "elitismCount": 5,
  "generationGap": 90.0,
  "crossoverPoint1": 2,
  "crossoverPoint2": 5,
  "startCityId": "F"
}
```

**Resposta**: Resultado completo com melhor rota, histórico de gerações, tempo de execução

#### `POST /api/tsp/initialize`

Inicializa uma nova população

- Body: GAConfig (mesmo formato acima)
- Retorna: Estatísticas da população inicial

#### `POST /api/tsp/evolve?generation={n}`

Evolui a população por uma geração

- Retorna: Resultado da geração (melhor rota, média, pior)

### Consultas

#### `GET /api/tsp/stats`

Estatísticas da população atual

#### `GET /api/tsp/history`

Histórico de todas as gerações executadas

#### `GET /api/tsp/best`

Melhor rota encontrada até o momento

#### `GET /api/tsp/population?top=10`

Top N rotas da população atual

#### `GET /api/tsp/cities`

Lista de cidades disponíveis no grafo

#### `GET /api/tsp/config/default`

Configuração padrão do AG

### Health Check

#### `GET /api/health`

Status da aplicação

## ��� Como Executar

### 1. Pré-requisitos

- Java 21
- Maven 3.8+

### 2. Compilar e Executar

```bash
# Compilar
mvn clean install

# Executar
mvn spring-boot:run
```

A aplicação estará disponível em `http://localhost:8080`

### 3. Testar a API

```bash
# Health check
curl http://localhost:8080/api/health

# Executar AG com configuração padrão
curl -X POST http://localhost:8080/api/tsp/run \
  -H "Content-Type: application/json" \
  -d "{\"populationSize\":100,\"crossoverRate\":70.0,\"mutationRate\":0.8,\"maxGenerations\":50,\"startCityId\":\"F\"}"

# Ver melhor rota
curl http://localhost:8080/api/tsp/best
```

## ��� Algoritmo Genético

### Representação

- **Cromossomo**: Lista de cidades (permutação)
- **Gene**: Uma cidade
- **Fitness**: 1 / distância total (quanto menor a distância, maior o fitness)

### Operadores Genéticos

#### 1. **PMX Crossover** (Partially Mapped Crossover)

- Recombinação em 2 pontos fixos
- Preserva a ordem relativa das cidades
- Garante que não haja cidades duplicadas

#### 2. **Mutação por Troca (Swap)**

- Seleciona duas posições aleatórias
- Troca as cidades nessas posições
- Taxa de mutação configurável (0.5%-1%)

#### 3. **Seleção Elitista**

- Mantém os N melhores indivíduos da geração anterior
- Usa torneio para seleção de pais
- Garante que a melhor solução não seja perdida

### Fluxo de Execução

1. **Inicialização**: Criar população aleatória
2. **Avaliação**: Calcular fitness de cada indivíduo
3. **Seleção**: Selecionar pais (torneio) e elites
4. **Cruzamento**: Aplicar PMX nos pais selecionados
5. **Mutação**: Aplicar mutação nos filhos
6. **Substituição**: Criar nova geração (elites + filhos)
7. **Repetir** 3-6 até atingir critério de parada

## ��� Dados do Grafo

Veja o arquivo `GRAPH_DATA.md` para instruções sobre como adicionar os dados corretos.

## Integração com Frontend React

Este backend foi projetado para ser consumido por um frontend React. Endpoints sugeridos para o frontend:

### Página de Configuração

- `GET /api/tsp/config/default` - Carregar configuração padrão
- `GET /api/tsp/cities` - Listar cidades disponíveis

### Execução do Algoritmo

- `POST /api/tsp/run` - Executar AG completo
- `POST /api/tsp/initialize` + múltiplos `POST /api/tsp/evolve` - Execução passo a passo

### Visualização de Resultados

- `GET /api/tsp/history` - Gráfico de convergência
- `GET /api/tsp/best` - Exibir melhor rota graficamente
- `GET /api/tsp/population?top=20` - Listar top 20 rotas

## ��� Configurações Recomendadas

### Para convergência rápida:

```json
{
  "populationSize": 100,
  "crossoverRate": 80.0,
  "mutationRate": 0.5,
  "maxGenerations": 50,
  "elitismCount": 5
}
```

### Para exploração ampla:

```json
{
  "populationSize": 200,
  "crossoverRate": 70.0,
  "mutationRate": 1.0,
  "maxGenerations": 100,
  "elitismCount": 10
}
```

## ��� Debugging

Logs detalhados estão disponíveis no console da aplicação, mostrando:

- Melhor rota de cada geração
- Distância média da população
- Tempo de execução
- Estatísticas de convergência

## ��� Próximos Passos

- [ ] Adicionar dados reais do grafo do PDF
- [ ] Testar com diferentes configurações
- [ ] Criar frontend React para visualização
- [ ] Adicionar mais operadores de mutação (opcional)
- [ ] Implementar visualização gráfica da rota
