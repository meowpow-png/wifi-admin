# ADR: Platform Interactions as Application Events

## Context

The application retrieves and updates Wi-Fi configurations through an external platform while maintaining a synchronized local database. A design decision was needed on how successful platform interactions should be communicated within the application and how follow-up processing, such as persistence, should be triggered.

The synchronization workflow already publishes application events after retrieving configurations from the platform, allowing persistence and other processing to execute independently. However, request processing invoked persistence directly, resulting in two different interaction models for operations that ultimately performed the same work.

## Alternatives

### Invoke persistence directly

Application services invoke the persistence component after successfully retrieving or updating a Wi-Fi configuration.

This approach is straightforward and makes persistence an explicit part of each workflow. However, every additional follow-up action, such as metrics collection or audit logging, must also be orchestrated directly by the application service.

### Publish application events

Application services publish events describing successful platform interactions. Dedicated event handlers perform persistence and any other follow-up processing independently.

## Decision

I decided to model successful platform interactions as application events. Whenever the application successfully retrieves or updates a Wi-Fi configuration through the external platform, it publishes an application event describing the completed operation. Persistence and any other follow-up processing are performed by independent event handlers rather than being orchestrated directly by the application service.

This interaction model is used consistently for both request processing and periodic synchronization.

## Rationale

Successful interactions with the external platform represent meaningful application events rather than implementation details. Publishing these events establishes a consistent interaction model across the application while allowing orchestration services to focus solely on communicating with the platform.

This approach decouples platform interactions from follow-up processing. Persistence becomes one consumer of platform interaction events rather than an explicit step within every workflow. Additional processing, such as metrics collection, audit logging, notifications, or future integrations, can be introduced by subscribing to the same events without modifying existing orchestration logic.

Using a common event-driven workflow for both request processing and synchronization also improves consistency by ensuring all successful platform interactions are handled in the same manner regardless of how they were initiated.

## Consequences

**Benefits:**

- Establishes a consistent interaction model for request processing and synchronization
- Keeps orchestration services focused on platform communication
- Decouples persistence from application workflows
- Allows additional processing to be added without modifying existing orchestration services
- Improves extensibility through independent event subscribers

**Limitations:**

- Introduces additional architectural components and event flow
- Makes follow-up processing less explicit than direct method invocation
- Requires developers to understand event relationships when tracing application behavior

**Implications:**

- Successful platform interactions should be represented as application events
- Persistence should be implemented as a consumer of platform interaction events
- Additional cross-cutting processing should subscribe to existing events rather than extending orchestration services
