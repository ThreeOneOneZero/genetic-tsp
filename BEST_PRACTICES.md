# Boas Práticas Avançadas — Backend Java

Padrões e técnicas para manter o código limpo, testável e escalável.

## 1. Usar DTOs para Requisição e Resposta

❌ **Não faça:**

```java
@PostMapping
public User createUser(@RequestBody User user) {
    return userService.save(user);  // Expõe entity direto
}
```

✅ **Faça:**

```java
@PostMapping
public ResponseEntity<ApiResponse<UserResponse>> createUser(
    @Valid @RequestBody CreateUserRequest req
) {
    User user = userService.create(req);
    return created(UserResponse.from(user));
}
```

**Por quê:**

- Decacopla a API de mudanças internas
- Valida dados na requisição
- Controla o que é retornado

## 2. Validação em Camadas

```java
// DTO com validação de formato
public record CreateUserRequest(
    @NotBlank
    @Email
    String email
) {}

// Service com validação de regra de negócio
@Service
public class UserService {
    public User create(CreateUserRequest req) {
        if (userRepository.existsByEmail(req.email())) {
            throw new BusinessException("Email already registered");
        }
        return userRepository.save(new User(req.email()));
    }
}
```

**Dica:** Format (Controller) → Lógica (Service)

## 3. Exception Handling Estruturado

```java
// Defina seus erros
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handle(BusinessException ex) {
        return ResponseEntity
            .status(ex.getHttpStatus())
            .body(ErrorResponse.of(ex.getErrorCode().getCode(), ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
        logger.error("Unexpected error", ex);
        return ResponseEntity
            .status(500)
            .body(ErrorResponse.of("INTERNAL_ERROR", "Internal server error"));
    }
}
```

## 4. Transactions e Propagation

```java
@Service
@Transactional  // Padrão: REQUIRED
public class OrderService {

    @Transactional(readOnly = true)  // Query apenas
    public Order findById(String id) {
        return orderRepository.findById(id).orElseThrow();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)  // Nova transação
    public void saveAudit(String action) {
        auditRepository.save(new Audit(action));
    }
}
```

**Dica:** Use `readOnly = true` para queries, economiza recursos.

## 5. Dependency Injection Correto

```java
// ❌ Evite field injection
@Service
public class UserService {
    @Autowired
    private UserRepository repo;  // Difícil de testar
}

// ✅ Use constructor injection
@Service
public class UserService {
    private final UserRepository repo;

    public UserService(UserRepository repo) {
        this.repo = repo;
    }
}

// ✅ Ou com Lombok
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repo;
}
```

**Por quê:** Facilita testes unitários e é explícito.

## 6. Logging Estruturado

```java
// ❌ Não faça
logger.info("User created");

// ✅ Faça com contexto
logger.info("User created", Map.of(
    "userId", user.getId(),
    "email", user.getEmail(),
    "timestamp", Instant.now()
));

// Ou use logging estruturado (SLF4J)
logger.info("User created - userId: {}, email: {}", user.getId(), user.getEmail());
```

## 7. Padrão Specification para Queries Complexas

```java
@Repository
public interface UserRepository extends
    JpaRepository<User, String>,
    JpaSpecificationExecutor<User> {}

// Service
public Page<User> search(UserFilter filter, Pageable pageable) {
    Specification<User> spec = (root, query, cb) -> {
        List<Predicate> predicates = new ArrayList<>();

        if (filter.name() != null) {
            predicates.add(cb.like(root.get("name"), "%" + filter.name() + "%"));
        }

        if (filter.email() != null) {
            predicates.add(cb.equal(root.get("email"), filter.email()));
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    };

    return userRepository.findAll(spec, pageable);
}
```

## 8. Testing Best Practices

```java
// Teste unitário (service sem banco)
class UserServiceTest {
    private UserRepository userRepository = mock(UserRepository.class);
    private UserService service = new UserService(userRepository);

    @Test
    void shouldCreateUserSuccessfully() {
        // Arrange
        CreateUserRequest req = new CreateUserRequest("john@example.com");
        User expected = new User("1", "john@example.com");
        when(userRepository.existsByEmail(req.email())).thenReturn(false);
        when(userRepository.save(any())).thenReturn(expected);

        // Act
        User result = service.create(req);

        // Assert
        assertEquals(expected.getId(), result.getId());
        verify(userRepository).save(any());
    }
}

// Teste de integração (com banco)
@SpringBootTest
class UserControllerIntegrationTest {
    @Autowired private TestRestTemplate restTemplate;

    @Test
    void shouldCreateUserEndpoint() {
        CreateUserRequest req = new CreateUserRequest("john@example.com");

        ResponseEntity<ApiResponse> response = restTemplate.postForEntity(
            "/api/users",
            req,
            ApiResponse.class
        );

        assertEquals(201, response.getStatusCodeValue());
    }
}
```

## 9. Usar Enums para Status/Estados

```java
public enum OrderStatus {
    PENDING("Pedido aguardando confirmação"),
    CONFIRMED("Pedido confirmado"),
    SHIPPED("Pedido enviado"),
    DELIVERED("Pedido entregue"),
    CANCELLED("Pedido cancelado");

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

// Na Entity
@Entity
public class Order {
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
}
```

## 10. Usar Records para DTOs Simples

```java
// ✅ Limpo e conciso
public record CreateUserRequest(
    @NotBlank String name,
    @Email String email
) {}

public record UserResponse(
    String id,
    String name,
    String email
) {
    public static UserResponse from(User user) {
        return new UserResponse(user.getId(), user.getName(), user.getEmail());
    }
}

// Em vez de classes com getters/setters
```

## 11. Usar Builder Pattern para Objetos Complexos

```java
@Builder
@Entity
public class Order {
    private String id;
    private String userId;
    private OrderStatus status;
    private BigDecimal total;
    private List<OrderItem> items;
}

// Uso
Order order = Order.builder()
    .userId("123")
    .status(OrderStatus.PENDING)
    .total(BigDecimal.valueOf(199.90))
    .items(List.of(item1, item2))
    .build();
```

## 12. API Versioning

```java
@RestController
@RequestMapping("/api/v1/users")
public class UserControllerV1 { ... }

@RestController
@RequestMapping("/api/v2/users")
public class UserControllerV2 { ... }
```

## 13. Rate Limiting (com anotação customizada)

```java
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimit {
    int requests() default 100;
    int minutes() default 1;
}

@RestControllerAdvice
@Component
public class RateLimitInterceptor implements HandlerInterceptor {
    private final RateLimitService rateLimitService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (handler instanceof HandlerMethod method) {
            RateLimit limit = method.getMethodAnnotation(RateLimit.class);
            if (limit != null && !rateLimitService.allowed(request, limit)) {
                response.setStatus(429);  // Too Many Requests
                return false;
            }
        }
        return true;
    }
}

// Uso
@GetMapping("/{id}")
@RateLimit(requests = 1000, minutes = 5)
public ResponseEntity<ApiResponse<User>> getUser(@PathVariable String id) {
    return ok(userService.getUser(id));
}
```

## 14. Usando Profiles para Diferentes Configs

```yaml
# application.yml (base)
app:
  name: backend-api

---
# application-dev.yml
spring:
  jpa:
    show-sql: true
logging:
  level:
    root: DEBUG

---
# application-prod.yml
spring:
  jpa:
    show-sql: false
logging:
  level:
    root: WARN
```

## 15. Health Checks Customizados

```java
@Component
public class DatabaseHealthIndicator extends AbstractHealthIndicator {

    @Autowired
    private DataSource dataSource;

    @Override
    protected void doHealthCheck(Health.Builder builder) {
        try (Connection conn = dataSource.getConnection()) {
            builder.up().withDetail("database", "PostgreSQL");
        } catch (Exception e) {
            builder.down().withDetail("error", e.getMessage());
        }
    }
}

// Acesso: GET /health
```

## Checklist de Qualidade

- [ ] Todos os métodos têm logging apropriado
- [ ] Exceções são tratadas e estruturadas
- [ ] DTOs são usados na API (não entities)
- [ ] Validação em duas camadas (formato + lógica)
- [ ] Transações usam readOnly = true quando apropriado
- [ ] Testes unitários e de integração existem
- [ ] Não há SQL injection (use parameterized queries)
- [ ] Performance: índices no banco, paginação em listas grandes
- [ ] Documentação no README
- [ ] Código segue convenções Java (CamelCase, etc)

---

**Próxima Leitura:** Veja EXAMPLE_ENDPOINT.md e DATABASE_SETUP.md para exemplos práticos.
