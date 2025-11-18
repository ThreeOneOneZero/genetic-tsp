# Backend Java Template — Spring Boot 3 + Maven

Base pronta para iniciar um backend robusto com Spring Boot. Foco em clareza, padrões e pouco boilerplate.

## Stack

- **Java 21** (LTS)
- **Spring Boot 3.3.4**
- **Maven 3.8+**
- **PostgreSQL** (banco padrão)
- **Lombok** (opcional, para reduzir verbosidade)
- **JUnit 5** + Mockito (testes)

## Estrutura do Projeto

```
src/
├── main/
│   ├── java/com/yourproject/
│   │   ├── Application.java              # Ponto de entrada
│   │   ├── config/                       # Configurações, validação de env
│   │   │   └── EnvironmentConfig.java
│   │   ├── controllers/                  # Handlers HTTP (herdam BaseController)
│   │   │   ├── BaseController.java
│   │   │   └── HealthController.java
│   │   ├── services/                     # Lógica de negócio (herdam BaseService)
│   │   │   ├── BaseService.java
│   │   │   └── HealthService.java
│   │   ├── models/                       # DTOs, records, responses
│   │   │   ├── ApiResponse.java
│   │   │   ├── ErrorResponse.java
│   │   │   └── HealthCheckResponse.java
│   │   ├── errors/                       # Exceções customizadas
│   │   │   ├── ErrorCode.java
│   │   │   ├── AppException.java
│   │   │   ├── BusinessException.java
│   │   │   └── NotFoundException.java
│   │   ├── middleware/                   # Filters, interceptors
│   │   │   └── GlobalExceptionHandler.java
│   │   └── utils/                        # Helpers genéricos
│   │       ├── DateUtil.java
│   │       ├── ValidationUtil.java
│   │       └── LogUtil.java
│   └── resources/
│       ├── application.yml               # Configuração padrão
│       ├── application-dev.yml           # Profile desenvolvimento
│       └── application-prod.yml          # Profile produção
└── test/
    └── java/com/templateproject/
        ├── ApplicationTest.java
        └── controllers/
            └── HealthControllerTest.java
```

## Quick Start

### 1. Pré-requisitos

- Java 21 instalado
- Maven 3.8+
- PostgreSQL 12+ (ou outro banco relacional)

### 2. Setup

```bash
# Clone e abra o projeto
cd backend/java-template

# Configure variáveis de ambiente
cp .env.example .env.local

# Edite .env.local com suas credenciais
# DB_USER, DB_PASSWORD, APP_ENV, etc

# Instale dependências e compile
mvn clean install

# Execute a aplicação (modo desenvolvimento)
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"
```

### 3. Teste

```bash
# Health check
curl http://localhost:8080/api/health

# Resposta esperada:
{
  "success": true,
  "data": {
    "status": "UP",
    "timestamp": "2025-10-16T...",
    "uptime": ...
  },
  "timestamp": "2025-10-16T..."
}
```

## Padrão de Resposta

### Sucesso

```json
{
  "success": true,
  "data": {
    "id": 1,
    "name": "Example"
  },
  "timestamp": "2025-10-16T10:30:00Z"
}
```

### Erro

```json
{
  "success": false,
  "error": {
    "code": "INVALID_INPUT",
    "message": "Email format is invalid",
    "details": {
      "field": "email"
    }
  },
  "timestamp": "2025-10-16T10:30:00Z"
}
```

## Como Começar a Criar Endpoints

### Passo 1: Criar o Modelo

```java
// src/main/java/com/yourproject/models/User.java
public record User(
    String id,
    String name,
    String email
) {}
```

### Passo 2: Criar o Serviço

```java
// src/main/java/com/yourproject/services/UserService.java
@Service
public class UserService extends BaseService {
    public User getUser(String id) {
        logger.info("Fetching user: {}", id);
        // Lógica aqui
        return new User(id, "John Doe", "john@example.com");
    }
}
```

### Passo 3: Criar o Controller

```java
// src/main/java/com/yourproject/controllers/UserController.java
@RestController
@RequestMapping("/users")
public class UserController extends BaseController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> getUser(@PathVariable String id) {
        return ok(userService.getUser(id));
    }
}
```

### Passo 4: Usar Validação

```java
@PostMapping
public ResponseEntity<ApiResponse<User>> createUser(
    @Valid @RequestBody CreateUserRequest request
) {
    return created(userService.createUser(request));
}
```

## Lançar Exceções

```java
// Erro de validação
throw new BusinessException("Email already exists");

// Recurso não encontrado
throw new NotFoundException("User not found");

// Com detalhes
throw new BusinessException(
    ErrorCode.INVALID_INPUT,
    "Invalid email format",
    Map.of("field", "email")
);
```

## Logging

Use o logger herdado de `BaseService`:

```java
@Service
public class UserService extends BaseService {
    public void doSomething() {
        logger.info("Starting operation");
        logger.warn("Warning message");
        logger.error("Error occurred", exception);
    }
}
```

## Testes

```bash
# Rodar todos os testes
mvn test

# Rodar teste específico
mvn test -Dtest=HealthControllerTest

# Rodar com cobertura
mvn test jacoco:report
```

### Exemplo de Teste

```java
@WebMvcTest(UserController.class)
public class UserControllerTest {
    @Autowired private MockMvc mockMvc;
    @MockitoBean private UserService userService;

    @Test
    public void testGetUser() throws Exception {
        User user = new User("1", "John", "john@example.com");
        when(userService.getUser("1")).thenReturn(user);

        mockMvc.perform(get("/api/users/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.name").value("John"));
    }
}
```

## Profiles

A aplicação suporta três profiles:

### default

- Validação básica
- Log em INFO
- DDL: `validate`

### dev

- SQL logging ativado
- Log em DEBUG
- Perfeito para desenvolvimento local

```bash
# Ativar
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"
```

### prod

- Logs apenas WARN
- Performance otimizada
- DDL: `validate`

```bash
# Ativar
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=prod"
```

## Próximos Passos

- [ ] Adicionar banco de dados com JPA
- [ ] Implementar autenticação JWT
- [ ] Adicionar Swagger/OpenAPI
- [ ] Containerizar com Docker
- [ ] Adicionar CI/CD (GitHub Actions)
- [ ] Implementar cache (Redis)
- [ ] Adicionar monitoramento (Actuator)

## Checklist Antes de Usar

- [ ] Renomeie `com.yourproject` para seu package
- [ ] Configure `.env.local` com credenciais reais
- [ ] Rodou `mvn clean install` com sucesso
- [ ] Teste health check em `http://localhost:8080/api/health`
- [ ] Comece a criar seus controllers, services e models

## Filosofia

- **Type-Safe First** — Java força tipagem, use isso a seu favor
- **Convention over Configuration** — siga padrões Maven
- **Métodos pequenos** — uma coisa por vez
- **Erros estruturados** — sempre use exceções customizadas
- **Logging desde o início** — facilita debugging
- **Sem boilerplate desnecessário** — o framework é uma ferramenta

---

**Dúvidas?** Consulte a documentação do Spring Boot: https://spring.io/projects/spring-boot
