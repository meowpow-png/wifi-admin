# Architecture

This document describes the application's architecture and high-level design. Implementation details, technologies, and development conventions are documented in [IMPLEMENTATION.md](IMPLEMENTATION.md).

## Vision

The goal of this architecture is to build a production-quality integration service rather than a minimal assignment solution. The design emphasizes clear module boundaries, maintainability, extensibility, and operational readiness while remaining simple and easy to understand.

## Architectural Principles

The architecture is guided by the following principles:

- **Separation of Concerns** – Each module has a single, well-defined responsibility
- **Business-Centric Design** – Business logic remains independent of REST, SOAP, and persistence
- **Encapsulated Integrations** – External systems are isolated behind dedicated integration boundaries
- **Observability by Default** – Logging, metrics, and tracing are built into every operation
- **Production Readiness** – Validation, configuration, testing, and error handling are first-class concerns

## System Context

The application acts as a bridge between REST clients and the external WiFi platform. It exposes a REST API, persists WiFi configurations locally, and synchronizes data with the external platform.

```mermaid
flowchart LR
    Client["REST Client / React UI"]
    Scheduler["Scheduler"]

    subgraph Backend["WiFi Admin Backend"]
        API["REST API"]
        APP["Application"]
        PLATFORM["Platform"]
        PERSISTENCE["Persistence"]
    end

    DB[("Database")]
    SOAP["HT WiFi Platform"]

    Client -->|HTTP / JSON| API
    API --> APP

    Scheduler --> APP

    APP --> PLATFORM
    APP --> PERSISTENCE

    PLATFORM -->|SOAP| SOAP
    PERSISTENCE --> DB
```

## Logical Architecture

The application follows a Domain-Driven Design approach centered around a single bounded context, **WiFi Administration**. The architecture is divided into four logical modules, each with a clear responsibility.

- **Domain** contains the business model and business rules.
- **Application** contains use cases and application ports.
- **Infrastructure** contains technical implementations such as REST, SOAP, persistence, and configuration.
- **Common** contains shared utilities and cross-cutting abstractions.

## Dependency Rules

- The domain module must not depend on any other application module
- The application module may depend only on the domain and common modules
- The infrastructure module may depend on the application, domain, and common modules
- The common module must not depend on any other application module
- Application ports are implemented by the infrastructure module

```text
Application ─────────► Domain
      │
      └──────────────► Common

Infrastructure ──────► Application
Infrastructure ──────► Domain
Infrastructure ──────► Common

Domain ──────────────► (nothing)

Common ──────────────► (nothing)
```

## Request Processing

### Read Flow

Retrieves the requested WiFi configuration from the database. If the configuration is not available, it is retrieved from the external platform, stored in the database, and returned to the client.

```mermaid
sequenceDiagram
    actor Client
    participant API
    participant Application
    participant Database
    participant Platform as HT WiFi Platform

    Client->>API: GET /wifi-parameter/{cpeId}
    API->>Application: Retrieve configuration

    Application->>Database: Query configuration

    alt Configuration found
        Database-->>Application: WiFi configuration
    else Configuration not found
        Database-->>Application: Not found

        Application->>Platform: Retrieve configuration
        Platform-->>Application: WiFi configuration

        Application->>Database: Store configuration
    end

    Application-->>API: WiFi configuration
    API-->>Client: 200 OK
```

### Update Flow

Validates the request, propagates the change to the external platform, and persists the updated configuration in the database upon successful completion.

```mermaid
sequenceDiagram
    actor Client
    participant API
    participant Application
    participant Platform as HT WiFi Platform
    participant Database

    Client->>API: PUT /wifi-parameter
    API->>Application: Update configuration

    Note over Application: Validate request

    Application->>Platform: Update configuration
    Platform-->>Application: Success

    Application->>Database: Persist configuration

    Application-->>API: Success
    API-->>Client: 204 No Content
```

### Synchronization Flow

Synchronizes WiFi configurations from the external platform into the database at regular intervals.

```mermaid
sequenceDiagram
    participant Scheduler
    participant Application
    participant Platform as HT WiFi Platform
    participant Database

    Scheduler->>Application: Synchronize configurations

    loop For each configured device
        Application->>Platform: Retrieve configuration
        Platform-->>Application: WiFi configuration

        Application->>Database: Persist configuration
    end
```

## Platform Integration

The external WiFi platform is isolated behind an integration boundary, forming an anti-corruption layer that prevents platform-specific concerns from leaking into the business domain.

The following integration principles are applied:

- All platform communication is performed through the integration boundary
- Platform-specific models are mapped to the domain model before crossing module boundaries
- Platform-specific failures are translated into domain-specific exceptions

For more information, see [ADR-003: Use Retries for Transient Failures](adr/003-adr-retries-for-transient-failures.md).

## Cross-Cutting Concerns

### Validation

Request validation is performed at the API boundary. Domain invariants and business rules that cannot be validated at the API boundary are enforced within the domain.

### Exception Handling

Expected failures are represented by explicit, domain-specific exceptions. Infrastructure failures are translated before reaching the API layer to ensure consistent REST error responses.

### Logging

Application requests, platform interactions, and unexpected failures are logged using structured logging while excluding sensitive information.

### Correlation IDs

A unique correlation ID is assigned to every request and propagated throughout request processing to enable end-to-end tracing.

### Observability

The application exposes health information and operational metrics. Health checks verify the availability of critical dependencies.

### Configuration

Application configuration is externalized to support environment-specific deployments.

### Security

The application provides authentication and authorization capabilities. Sensitive information, such as WiFi passwords, is excluded from logs and error responses.

For more information, see [ADR-004: Use Token-Based Authentication](adr/004-adr-token-based-authentication.md).

## Persistence Strategy

WiFi configurations are stored in a database to reduce platform dependency, improve response times, and provide durable persistence across application restarts. The database serves read requests, while the external platform remains the authoritative source during synchronization.

The following persistence policies are applied:

- WiFi configurations are read from the database by default
- Missing configurations are retrieved from the platform and stored in the database
- Configuration changes are persisted only after they have been successfully applied on the platform

For more information, see [ADR-001: Use a Local Database](adr/001-adr-local-database.md).

## Synchronization Strategy

Periodic synchronization keeps the database aligned with the external WiFi platform. During synchronization, the platform is treated as the authoritative source and local records are updated accordingly.

The following synchronization policies are applied:

- Synchronization runs on a configurable schedule
- The set of synchronized CPEs is configurable
- Each CPE is synchronized independently
- Synchronization failures are logged without interrupting the overall job

Note that synchronization strategy maintains only the current platform state. Historical configuration snapshots are outside the scope of this project.

For more information, see [ADR-002: Synchronize Platform Data](adr/002-adr-synchronize-platform-data.md).

## Testing Strategy

### Unit Tests

Unit tests verify individual components in isolation. They focus on business logic, validation, mapping, and error handling without requiring external infrastructure.

### Integration Tests

Integration tests verify interactions between application components and external dependencies. They ensure the application behaves correctly in a production-like environment.

### Architecture Tests

Architecture tests enforce the project's structural rules. They verify module boundaries, dependency rules, and architectural constraints to prevent architectural drift as the codebase evolves.

### Resilience Tests

Resilience tests verify the application's behavior under transient infrastructure failures. They ensure retry policies, timeout handling, and error recovery mechanisms behave correctly when communicating with the external SOAP platform.
