# ADR: Use Token-Based Authentication

## Context

The application exposes a REST API that allows clients to retrieve and modify WiFi configurations. A mechanism is required to authenticate clients before allowing access to protected resources.

## Alternatives

### No authentication

Allow unrestricted access to the REST API.

This approach is suitable for development and testing but is not appropriate for a production system exposing administrative operations.

### HTTP Basic Authentication

Authenticate every request using a username and password.

This approach is simple and widely supported but requires credentials to be sent with every request and is less suitable for modern REST APIs.

### Session-Based Authentication

Authenticate users by maintaining server-side sessions.

This approach is commonly used by traditional web applications but introduces server-side state and is less suitable for stateless REST services.

### Token-Based Authentication

Authenticate clients by issuing a signed token that is presented with subsequent requests.

## Decision

I decided to secure the REST API using token-based authentication.

## Rationale

Token-based authentication aligns well with the stateless nature of REST APIs by allowing each request to be authenticated independently. It eliminates the need for server-side session management and scales naturally as additional application instances are introduced.

## Consequences

**Benefits:**

- Well suited for stateless REST APIs
- Eliminates server-side session management
- Supports horizontal scaling
- Separates authentication from business logic

**Limitations:**

- Requires an authentication flow before accessing protected resources
- Introduces token lifecycle management

**Implications:**

- Clients must include a valid authentication token when accessing protected endpoints
- The application is responsible for issuing and validating authentication tokens
