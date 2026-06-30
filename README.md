# Wifi Admin

## Architecture

The application's architecture, design principles, and architectural decisions are documented in [ARCHITECTURE.md](docs/ARCHITECTURE.md)

## Technology Stack

| Area          | Technology                                 |
|---------------|--------------------------------------------|
| Runtime       | Java 21, Spring Boot                       |
| API           | Spring Web, OpenAPI                        |
| Integration   | Apache CXF                                 |
| Data          | PostgreSQL, Spring Data JPA, Flyway        |
| Observability | Spring Boot Actuator, Micrometer           |
| Testing       | JUnit 5, Mockito, Testcontainers, ArchUnit |
| Deployment    | Docker, Docker Compose                     |
