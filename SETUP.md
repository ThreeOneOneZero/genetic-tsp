# Setup Detalhado — Backend Java

Instruções passo a passo para configurar o ambiente de desenvolvimento.

## Pré-requisitos

### 1. Java 21

Verifique se tem instalado:

```bash
java -version
# Saída esperada: openjdk version "21.x.x"
```

Se não tiver, instale em https://adoptium.net ou https://www.oracle.com/java/technologies/downloads/

### 2. Maven 3.8+

```bash
mvn -version
# Saída esperada: Apache Maven 3.8.x ou superior
```

Se não tiver, instale em https://maven.apache.org/download.cgi

### 3. PostgreSQL (Opcional)

Este template **roda sem banco de dados por padrão**. PostgreSQL é opcional.

Se quiser usar:

```bash
psql --version
```

Instale em: https://www.postgresql.org/download/

## Setup Local

### Passo 1: Clonar/Navegar para o Projeto

```bash
cd backend/java-template
```

### Passo 2: Copiar Variáveis de Ambiente (Opcional)

```bash
# Windows
copy .env.example .env.local

# Linux/Mac
cp .env.example .env.local
```

**Nota:** As variáveis são opcionais. O template roda com valores padrão.

### Passo 3: Editar .env.local

Abra `.env.local` e configure:

```env
APP_ENV=development
APP_NAME=Backend API
SERVER_PORT=8080

# Seu banco de dados (só necessário se usar PostgreSQL)
DB_USER=seu_usuario
DB_PASSWORD=sua_senha
DB_NAME=yourproject_db
DB_HOST=localhost
DB_PORT=5432
```

**Dica:** Se não quer usar banco, deixe o arquivo como está.

### Passo 4: Criar Banco de Dados (opcional se usando PostgreSQL)

```bash
psql -U postgres -c "CREATE DATABASE yourproject_db;"
```

### Passo 5: Compilar o Projeto

```bash
mvn clean install
```

Saída esperada: `BUILD SUCCESS`

### Passo 6: Rodar a Aplicação

```bash
# Modo desenvolvimento (sem banco de dados - H2 em memória)
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"

# Ou modo padrão (sem banco de dados)
mvn spring-boot:run
```

Ou no IDE (VS Code/IntelliJ):

1. Abra `Application.java`
2. Clique em "Run" acima da classe
3. Selecione "Run Java Program"

### Passo 7: Verificar se está Funcionando

```bash
curl http://localhost:8080/api/health
```

Resposta esperada:

```json
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

## IDEs Recomendadas

### IntelliJ IDEA Community (Free)

Melhor suporte para Spring Boot:

1. Abra a pasta do projeto
2. Configure JDK: File → Project Structure → SDK → Java 21
3. Click em ▶ Run ao lado de `public static void main`

### VS Code + Extensions

Extensões recomendadas:

- Extension Pack for Java (Microsoft)
- Spring Boot Extension Pack (Pivotal)
- Lombok Annotations Support

## Comandos Úteis

```bash
# Compilar sem rodar
mvn clean compile

# Apenas testes
mvn test

# Build para produção (JAR)
mvn clean package -DskipTests

# Rodar JAR gerado
java -jar target/backend-1.0.0.jar

# Rodar com profile específico
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=prod"

# Ver dependências
mvn dependency:tree

# Forçar atualizar dependências
mvn clean install -U
```

## Troubleshooting

### "JAVA_HOME not set"

```bash
# Windows (PowerShell)
$env:JAVA_HOME = "C:\Program Files\Java\jdk-21"

# Linux/Mac
export JAVA_HOME=$(/usr/libexec/java_home -v 21)
```

### "Connection refused" no banco de dados

Verifique se:

1. PostgreSQL está rodando
2. Credenciais em `.env.local` estão corretas
3. Banco de dados `yourproject_db` existe

```bash
# PostgreSQL rodando?
psql -U postgres -c "SELECT 1;"
```

### "Port 8080 already in use"

```bash
# Mude a porta em .env.local
SERVER_PORT=3001
```

### Maven downloads lento

Edite `~/.m2/settings.xml`:

```xml
<mirrors>
  <mirror>
    <id>aliyun</id>
    <mirrorOf>central</mirrorOf>
    <url>https://maven.aliyun.com/repository/public</url>
  </mirror>
</mirrors>
```

## Próximo Passo

Após setup bem-sucedido, leia `README.md` para começar a criar endpoints.
