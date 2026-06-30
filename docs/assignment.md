# Assignment

## Overview

You are building a **small Spring Boot backend** that sits between a modern REST client and an existing SOAP platform.

The company already has an internal platform that manages WiFi settings on customer routers. That platform exposes a **SOAP API**, but the new mobile application is only allowed to communicate using **REST**.

Your job is to build a **REST wrapper** around the existing SOAP service.

## The Architecture

```text
REST Client
    │
    ▼
Your Spring Boot application
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

### Get WiFi Configuration

```http
GET /wifi-parameter/{cpeId}
```

Receives a router identifier (`cpeId`), retrieves its WiFi configuration from the SOAP platform, and returns it as JSON.

### Update WiFi configuration

```http
PUT /wifi-parameter
```

Receives a JSON representation of the WiFi configuration, forwards it to the SOAP platform, and returns the appropriate HTTP response.

## SOAP Platform

The SOAP platform is **not implemented by you**.

It exposes two operations:

- `getCpeID`
- `updateCpeId`

Although the second operation is called `updateCpeId`, it actually updates the **entire WiFi configuration**, not only the router identifier.

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

The repository already contains everything needed:

- OpenAPI specification describing the REST API.
- WSDL describing the SOAP service.
- Mock SOAP server.
- Docker Compose configuration.

Running

```bash
docker compose up -d
```

starts the mock SOAP platform.

## Required Behavior

A complete solution should:

- Implement the REST API exactly as defined by the OpenAPI specification.
- Communicate with the SOAP platform according to the WSDL.
- Correctly map REST models to SOAP models and vice versa.
- Validate requests.
- Handle SOAP faults and network failures gracefully.
- Return appropriate HTTP status codes.

The backend must be implemented using Spring Boot **or** Ktor and Java **or** Kotlin.

## Bonus Tasks

These are additional improvements, not required for a passing solution.

### Local Database

Store WiFi configurations locally and return GET requests from the database instead of querying the SOAP platform every time.

---

### Synchronization Scheduler

Synchronize the local database with the SOAP platform during configurable nightly jobs.

---

### Production Readiness

Add:

- logging
- security
- configuration profiles

---

### React Frontend

Build a React application that communicates with your REST API.
