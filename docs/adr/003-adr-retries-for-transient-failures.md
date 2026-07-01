# ADR: Use Retries for Transient Failures

## Context

The application communicates with an external SOAP platform over the network. Temporary communication failures can occur even when the platform is otherwise healthy. A strategy is therefore required to improve reliability while avoiding unnecessary load on the platform.

## Alternatives

### Fail immediately

Return an error as soon as a communication failure occurs.

This approach is simple but causes temporary network issues and platform interruptions to become user-visible failures.

### Retry with a fixed delay

Retry failed requests after a constant delay.

This improves reliability but repeatedly sends requests at a fixed rate while the platform is recovering.

### Retry with exponential backoff

Retry failed requests with progressively increasing delays between attempts.

## Decision

I decided to retry transient communication failures using an exponential backoff strategy.

## Rationale

Many communication failures are temporary and can be resolved by retrying the request after a short delay. Exponential backoff improves application reliability while avoiding repeated requests in rapid succession, reducing unnecessary load on the platform during recovery.

## Consequences

**Benefits:**

- Improves resilience against temporary communication failures
- Reduces user-visible failures caused by short-lived platform interruptions
- Reduces unnecessary load on the platform during recovery

**Limitations:**

- Increases the time required to return an error when all retry attempts fail
- Does not recover from persistent platform failures

**Implications:**

- Retry logic should be applied only to transient communication failures
