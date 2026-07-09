# ADR: Synchronize Platform Data

## Context

The application maintains a local database containing a replica of the WiFi configurations managed by the external platform. Since the platform remains the authoritative source of truth, changes made outside the application can cause the local data to become stale. A strategy is therefore required to keep the local database synchronized with the platform.

## Alternatives

### No synchronization

The local database is updated only when the application retrieves or modifies a WiFi configuration.

This approach is simple but allows the local database to become permanently stale when changes are made directly on the platform.

### Synchronize on every read

Refresh the local database from the platform before serving every read request.

This ensures fresh data but removes most of the benefits of maintaining a local database by introducing a platform call for every request.

### Platform-driven synchronization

Update the local database whenever the platform publishes change events.

This provides near real-time consistency but requires capabilities that are not supported by the external platform.

### Periodic synchronization

Refresh the local database from the platform at configurable intervals.

## Decision

I decided to synchronize the local database with the external platform using a periodic scheduled job. The synchronization schedule is configurable, allowing it to be adjusted for different environments and operational requirements without requiring application changes.

## Rationale

Periodic synchronization provides a simple and reliable way to keep the local database reasonably up to date while preserving the performance benefits of serving read requests locally. It also avoids unnecessary load on the external platform by eliminating the need to refresh data on every request.

The external platform does not support publishing change events, making platform-driven synchronization unavailable. Running synchronization during periods of lower platform activity further reduces operational impact while remaining configurable for different deployment environments.

Platform configurations are retrieved sequentially to avoid placing unnecessary load on the external platform while keeping synchronization predictable and resilient. Each synchronized configuration is then published as an event, allowing persistence and other follow-up processing, such as metrics collection or audit logging, to run independently. This keeps the synchronization workflow focused on retrieving data while local processing continues in parallel with subsequent platform requests.

## Consequences

**Benefits:**

- Keeps the local database reasonably up to date
- Preserves fast database-backed read operations
- Reduces load on the external platform
- Allows synchronization frequency to be adjusted without redeployment
- Simple and reliable implementation

**Limitations:**

- Data may become temporarily stale between synchronization runs
- Requires scheduled background processing

**Implications:**

- The synchronization schedule should be configurable
- Synchronization failures should not prevent subsequent synchronization runs
