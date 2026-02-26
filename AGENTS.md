# AGENTS.md

## Cursor Cloud specific instructions

### Project overview
This is a **Shopping Cart REST API** built with Spring Boot 2.4.3 and Hibernate 5.4. It exposes REST endpoints for managing Users, Items, and Shopping Carts. There is no frontend.

### Java version
The project targets Java 8 (`pom.xml` source/target) but **must run on JDK 11** (not JDK 21) because the bundled Lombok version (1.18.18) is incompatible with JDK 17+. JDK 11 is installed at `/usr/lib/jvm/java-11-openjdk-amd64` and set as the system default. `JAVA_HOME` is exported in `~/.bashrc`.

### Configuration files added for dev
The repository ships without `application.properties` or a Hibernate `SessionFactory` bean. Two files were created for development:
- `src/main/resources/application.properties` — configures H2 in-memory DB, ddl-auto=update, excludes `HibernateJpaAutoConfiguration`
- `src/main/java/com/example/shop/config/HibernateConfig.java` — exposes `SessionFactory` via `LocalSessionFactoryBean` and registers `HibernateTransactionManager`

Without these files the app cannot start. If they are missing after a fresh checkout, recreate them (see git history for content).

### Build & Run
```bash
cd /workspace/shop
export JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64
mvn compile          # build
mvn test             # tests (no test classes exist yet)
mvn spring-boot:run  # starts on port 8080
```

### Key endpoints
| Method | Path | Description |
|--------|------|-------------|
| GET | `/item` | List all items |
| POST | `/add-item?code=X&name=Y&info=Z&stock=N` | Add item |
| GET | `/find-item/{id}` | Get item by ID |
| GET | `/user` | List all users |
| POST | `/add-user?username=X&password=Y&role=Z` | Add user |
| GET | `/find-user/{id}` | Get user by ID |

### Gotchas
- The DAOs use Hibernate `SessionFactory` directly (not Spring Data JPA repositories), so auto-configured `EntityManagerFactory` must be excluded to avoid `EntityManagerHolder`/`SessionHolder` cast errors.
- No lint tooling (Checkstyle, PMD, etc.) is configured in this project.
- No automated tests exist (`src/test/` directory is absent).
- The `User` entity has `@EnableAutoConfiguration` annotation which is harmless but unusual on an entity class.
- The `ShoppingCartController` is an empty stub with no endpoints.
