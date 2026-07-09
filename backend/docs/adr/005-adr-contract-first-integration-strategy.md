# ADR: Adopt a Contract-First Integration Strategy

## Context

The application integrates with an external SOAP platform described by a published WSDL. I needed to decide whether to implement the integration manually or derive it directly from the published contract.

I explored both approaches during development. I considered writing the SOAP request and response models myself, and I also evaluated a solution that maintained handwritten integration models. That comparison ultimately came down to one question: **who owns the integration contract?**

## Alternatives

### Manually implement the integration model

Interpret the published WSDL and maintain the request, response, and service models within the application.

This provides full control over the integration model, but it also duplicates a contract owned by the external platform. Any change to the platform contract must be manually reflected in the application, increasing maintenance effort and the risk of contract drift.

### Adopt a contract-first integration strategy

Treat the published service contract as the authoritative definition of the integration boundary and derive the platform-facing model directly from it.

The application owns only the translation between the platform model and the domain model.

## Decision

I decided to adopt a contract-first integration strategy and treat the published service contract as the single source of truth for the platform integration.

The platform-facing integration model is generated directly from the published contract, while the application maintains its own independent domain model. Translation between the two is performed exclusively within the integration adapter.

## Rationale

This approach avoids duplicating externally owned models and keeps the implementation aligned with the platform as it evolves.

It also preserves a clear separation between the external platform model and the application's domain model. Changes to the platform contract can be incorporated by regenerating the integration model instead of manually updating parallel representations of the same interface.

## Consequences

**Benefits:**

- The published contract becomes the single source of truth
- Eliminates manual duplication of externally owned models
- Reduces the risk of contract drift
- Keeps the domain model independent of the platform contract
- Platform contract changes are incorporated through regeneration

**Limitations:**

- Introduces a code generation step
- Requires regeneration when the platform contract changes
- Generated integration models should not be modified directly

**Implications:**

- The generated integration model remains isolated within the integration layer
- The application owns only the mapping between the platform and domain models
- Platform-specific concerns remain confined to the integration adapter
