# Assignment

## Overview

You are building a **small Java/Kotlin backend application** that sits between a modern REST client and an existing SOAP platform.

The company already has an internal platform that manages Wi-Fi settings on customer routers. That platform exposes a **SOAP API**, but the new mobile application is only allowed to communicate using **REST**.

Your job is to build a **REST wrapper** around the existing SOAP service.

## Constraints

When implementing the solution:

- You **must** implement the REST API defined by the provided OpenAPI specification
- You **must** integrate with the existing SOAP platform using the provided WSDL
- You **must not** implement or modify the SOAP platform
- You **must** implement the backend using **Spring Boot** or **Ktor**, and **Java** or **Kotlin**

Additionally:

- You **must** treat the SOAP platform as the authoritative source of Wi-Fi configuration
- You **must not** change the REST API defined by the OpenAPI specification
- You **must not** change the SOAP contract defined by the WSDL

## Scope

The assignment includes only the functionality required to satisfy the documented requirements and bonus tasks. Supporting infrastructure (such as logging, security, authentication, and deployment) is considered part of the implementation rather than additional functionality.

Functionality that introduces new business capabilities beyond the purpose of the assignment (such as customer management, billing, or device inventory) is considered out of scope.

## Architecture

```text
REST Client
    │
    ▼
Your backend application
    │
SOAP
    ▼
Mock SOAP Platform
```

The SOAP platform already exists. It is provided as a Docker container and should be treated as an external system.

## What You Own

You are responsible **only** for the backend application.

Your application should:

- Expose the REST API described by the OpenAPI specification
- Call the SOAP platform using the WSDL specification
- Convert REST requests into SOAP requests
- Convert SOAP responses into REST responses
- Validate requests according to the OpenAPI contract and business rules
- Translate platform errors into meaningful HTTP responses

You do **not** implement the SOAP server.

## REST API

### Get Wi-Fi Configuration

```http
GET /wifi-parameter/{cpeId}
```

Receives a router identifier (`cpeId`), retrieves its Wi-Fi configuration from the SOAP platform, and returns it as JSON.

### Update Wi-Fi configuration

```http
PUT /wifi-parameter
```

Receives a JSON representation of the Wi-Fi configuration, forwards it to the SOAP platform, and returns the appropriate HTTP response.

## SOAP Platform

The SOAP platform is **not implemented by you**.

It exposes two operations:

- `getCpeID`
- `updateCpeId`

Although the second operation is called `updateCpeId`, it actually updates the **entire Wi-Fi configuration**, not only the router identifier.

## Request Flow

Every request follows the same path:

```text
Client
    │
REST (JSON)
    ▼
Your backend
    │
SOAP
    ▼
SOAP platform
    │
SOAP response
    ▼
Your backend
    │
REST (JSON)
    ▼
Client
```

Or even simpler:

> Receive REST → Call SOAP → Return REST.

## Development Environment

The repository already contains everything needed to complete the assignment:

| File                         | Description                                                     |
|------------------------------|-----------------------------------------------------------------|
| `openapi/openapi.yaml`       | OpenAPI specification describing the REST API                   |
| `wsdl/wifi-platform.wsdl`    | WSDL describing the SOAP service                                |
| `mockoon/platform-mock.json` | Mock SOAP platform configuration                                |
| `docker-compose.yml`         | Docker Compose configuration for running the mock SOAP platform |

Start the mock SOAP platform by running:

```bash
docker compose up -d
```

This starts the mock SOAP platform required by the application.

## Required Behavior

A complete solution should:

- Implement the REST API exactly as defined by the OpenAPI specification
- Communicate with the SOAP platform according to the WSDL
- Correctly map REST models to SOAP models and vice versa
- Validate requests
- Handle SOAP faults and network failures gracefully
- Return appropriate HTTP status codes

The backend must be implemented using Spring Boot **or** Ktor and Java **or** Kotlin.

## Bonus Tasks

These are additional improvements, not required for a passing solution.

### Local Database

Store Wi-Fi configurations locally and return GET requests from the database instead of querying the SOAP platform every time.

### Synchronization Scheduler

Synchronize the local database with the SOAP platform during configurable nightly jobs.

### Production Readiness

Add:

- logging
- security
- configuration profiles

---

### React Frontend

Build a React application that communicates with your REST API.

## Evaluation Criteria

Submissions are primarily evaluated on:

- Correct implementation of the REST API according to the OpenAPI specification
- Correct integration with the SOAP platform according to the WSDL
- Clean project structure and code quality
- Request validation and the appropriate error handling
- Automated tests and clear project documentation
