# ADR: Use a Local Database

## Context

The application retrieves and updates WiFi configurations through an external SOAP platform. A design decision was needed on whether every request should communicate directly with the platform or whether the application should maintain its own copy of the data.

## Alternatives

### Query the platform directly

Every read and update request is forwarded to the SOAP platform.

This approach is simple and always returns the latest data, but every request depends on the platform being available and responsive.

### Maintain an in-memory cache

Store recently retrieved WiFi configurations in application memory.

This improves read performance but all cached data is lost when the application restarts. It also becomes difficult to keep multiple application instances synchronized.

### Maintain a local database replica

Store a copy of the WiFi configurations in a local database and periodically synchronize it with the external platform.

## Decision

I decided to maintain a local database containing a replica of the WiFi configurations managed by the external platform. The application serves read requests from the database by default while treating the external platform as the authoritative source of truth. Changes are propagated to the platform first and persisted locally only after the platform update succeeds.

## Rationale

Maintaining a local database improves response times by serving most read requests without contacting the external platform. It also reduces the number of SOAP requests sent to the platform and allows the application to retain synchronized data across restarts.

Unlike an in-memory cache, a database provides durable storage and creates a solid foundation for future features such as reporting, auditing, and more advanced queries. At the same time, treating the external platform as the source of truth keeps the architecture simple and avoids ownership conflicts.

## Consequences

**Benefits:**

- Faster read operations
- Reduced load on the external platform
- Data survives application restarts
- Provides a foundation for future features
- Keeps the external platform as the source of truth

**Limitations:**

- Introduces an additional component to maintain
- Local data may become temporarily stale between synchronizations
- Synchronization logic is required

**Implications:**

- The database must be synchronized with the external platform
- Updates must be applied to the platform before being persisted locally
- Repository failures during reads should not prevent retrieval from the platform
