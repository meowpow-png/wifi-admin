# WiFi Admin Backend

This is a backend service for administering Wi-Fi parameters on CPE devices. 

The service exposes a REST API for administrator authentication, password management, and Wi-Fi configuration retrieval/update. It persists configuration data locally in PostgreSQL and integrates with the external WiFi platform through SOAP.

## Architecture

The backend follows a modular, domain-oriented architecture based on Ports and Adapters.

| Package       | Responsibility                                           |
|---------------|----------------------------------------------------------|
| `domain`      | Business model and domain rules                          |
| `application` | Use cases, services, and application ports               |
| `infra`       | REST, SOAP, persistence, security, and app configuration |
| `common`      | Shared logging and cross-cutting utilities               |

Read [ARCHITECTURE](docs/ARCHITECTURE.md) and [IMPLEMENTATION](docs/IMPLEMENTATION.md) for more information.

## API

The application API is served on port `8081`.

Swagger UI is enabled in the `dev` profile:

```text
http://localhost:8081/swagger-ui/index.html
```

The OpenAPI contract is maintained in `openapi/openapi.yaml`.

Protected endpoints require a JWT bearer token:

```text
Authorization: Bearer <token>
```

### Authenticate Administrator

Authenticates an administrator and returns a JWT access token.

**Path**

```text
POST /auth/login
```

**Body (application/json)**

| Field    | Type   | Required | Description                |
|----------|--------|----------|----------------------------|
| username | string | yes      | Administrator username     |
| password | string | yes      | Administrator password     |

**Example**

```json
{
  "username": "admin",
  "password": "admin"
}
```

**Response (application/json)**

| Field | Type   | Description      |
|-------|--------|------------------|
| token | string | JWT access token |

**Example**

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

**Responses**

- `200 OK` - Authentication succeeded
- `400 Bad Request` - Request validation failed
- `401 Unauthorized` - Authentication failed

### Change Administrator Password

Changes the password of the authenticated administrator.

**Path**

```text
PUT /admin/password
```

**Body (application/json)**

| Field           | Type   | Required | Description                    |
|-----------------|--------|----------|--------------------------------|
| currentPassword | string | yes      | Current administrator password |
| newPassword     | string | yes      | New administrator password     |

**Example**

```json
{
  "currentPassword": "admin",
  "newPassword": "new-password"
}
```

**Responses**

- `204 No Content` - Password changed successfully
- `400 Bad Request` - Request validation failed
- `401 Unauthorized` - Authentication failed
- `500 Internal Server Error` - Administrator account could not be resolved

### Retrieve Wi-Fi Configuration

Returns the Wi-Fi configuration for a CPE device.

**Path**

```text
GET /wifi-parameter/{cpeId}
```

**Path Parameters**

| Parameter | Type   | Required | Description           |
|-----------|--------|----------|-----------------------|
| cpeId     | string | yes      | CPE device identifier |

**Response (application/json)**

| Field          | Type   | Description                        |
|----------------|--------|------------------------------------|
| cpeId          | string | CPE device identifier              |
| wifiBand       | string | Wi-Fi band                         |
| ssid           | string | Wireless network name              |
| encryptionType | string | Wireless encryption type, nullable |
| password       | string | Wireless password, nullable        |

**Example**

```json
{
  "cpeId": "CPE_001",
  "wifiBand": "BAND_2_4_GHZ",
  "ssid": "Office WiFi",
  "encryptionType": "WPA2_PSK",
  "password": "secret-password"
}
```

**Responses**

- `200 OK` - Wi-Fi configuration returned successfully
- `400 Bad Request` - Request validation failed
- `401 Unauthorized` - Missing or invalid access token
- `404 Not Found` - CPE device was not found
- `502 Bad Gateway` - Platform communication or response failure
- `500 Internal Server Error` - Unexpected server failure

### Update Wi-Fi Configuration

Updates the Wi-Fi configuration for a CPE device on the external platform and returns the confirmed configuration.

**Path**

```text
PUT /wifi-parameter
```

**Body (application/json)**

| Field          | Type   | Required | Description                                      |
|----------------|--------|----------|--------------------------------------------------|
| cpeId          | string | yes      | CPE device identifier                            |
| wifiBand       | string | yes      | Wi-Fi band                                       |
| ssid           | string | yes      | Wireless network name                            |
| encryptionType | string | no       | Wireless encryption type; defaults to `OPEN`     |
| password       | string | no       | Wireless password for encrypted configurations   |

**Allowed values**

| Field            | Values                                                              |
|------------------|---------------------------------------------------------------------|
| `wifiBand`       | `BAND_2_4_GHZ`, `BAND_5_GHZ`                                        |
| `encryptionType` | `OPEN`, `WEP`, `WPA_PSK`, `WPA2_PSK`, `WPA3_SAE`, `WPA2_ENTERPRISE` |

**Example**

```json
{
  "cpeId": "CPE_001",
  "wifiBand": "BAND_2_4_GHZ",
  "ssid": "Office WiFi",
  "encryptionType": "WPA2_PSK",
  "password": "secret-password"
}
```

**Response (application/json)**

Returns the same Wi-Fi configuration model as `GET /wifi-parameter/{cpeId}`.

**Responses**

- `200 OK` - Wi-Fi configuration updated successfully
- `400 Bad Request` - Request validation failed
- `401 Unauthorized` - Missing or invalid access token
- `404 Not Found` - CPE device was not found
- `502 Bad Gateway` - Platform communication or response failure
- `500 Internal Server Error` - Unexpected server failure

### Error Response

Error responses use a common response body.

| Field   | Type   | Description                              |
|---------|--------|------------------------------------------|
| message | string | Human-readable error message             |
| code    | string | Application-specific error identifier    |

**Example**

```json
{
  "message": "CPE not found",
  "code": "CPE_NOT_FOUND"
}
```

Known error codes:

- `VALIDATION_FAILED`
- `CPE_NOT_FOUND`
- `PLATFORM_ERROR`
- `AUTHENTICATION_FAILED`
- `INTERNAL_SERVER_ERROR`

### Management

Management endpoints are served on port `8082`.

| Endpoint                             | Method | Description                            |
|--------------------------------------|--------|----------------------------------------|
| `/actuator/health`                   | `GET`  | Application health                     |
| `/actuator/health/platform`          | `GET`  | External platform health               |
| `/actuator/shutdown`                 | `POST` | Shut down the backend                  |
| `/actuator/sync`                     | `POST` | Trigger platform synchronization       |
| `/actuator/logging/{level}`          | `POST` | Set application logging level          |
| `/actuator/payloadlogging/{enabled}` | `POST` | Enable or disable SOAP payload logging |

## Configuration

Configuration is loaded from Spring Boot property files and environment variables. Common configuration is defined in `application.properties`. 

Environment-specific overrides are defined in:

- `application-dev.properties`
- `application-prod.properties`
- `application-test.properties`

### Application Properties

| Property                          | Description                                                     |
|-----------------------------------|-----------------------------------------------------------------|
| `platform.soap-endpoint`          | External SOAP platform endpoint                                 |
| `platform.cpe-id-format`          | Format used to generate synchronized CPE identifiers            |
| `platform.cpe-id-count`           | Number of CPE identifiers included in scheduled synchronization |
| `platform.sync-on-startup`        | Whether synchronization runs during application startup         |
| `platform.sync-schedule`          | Daily synchronization time                                      |
| `platform.connection-timeout`     | SOAP connection timeout                                         |
| `platform.receive-timeout`        | SOAP response timeout                                           |
| `platform.retry.max-attempts`     | Maximum SOAP retry attempts                                     |
| `platform.retry.delay`            | Initial SOAP retry delay                                        |
| `platform.retry.max-delay`        | Maximum SOAP retry delay                                        |
| `platform.retry.delay-multiplier` | Exponential retry delay multiplier                              |
| `security.aes-key`                | Base64-encoded 256-bit AES key for persisted Wi-Fi passwords    |
| `security.jwt-secret`             | JWT signing secret                                              |
| `security.jwt-expiration`         | JWT expiration duration                                         |
| `security.allowed-origins`        | Allowed CORS origins                                            |
| `security.public-endpoints`       | Unauthenticated endpoint patterns                               |
| `cxf.log-faults`                  | Whether CXF SOAP faults are logged                              |

### Environment Variables

The default configuration expects these environment variables:

| Variable        | Description                     |
|-----------------|---------------------------------|
| `DB_HOST`       | PostgreSQL host                 |
| `DB_NAME`       | PostgreSQL database name        |
| `DB_USER`       | PostgreSQL username             |
| `DB_PASSWORD`   | PostgreSQL password             |
| `SOAP_ENDPOINT` | External SOAP platform endpoint |
| `AES_KEY`       | Base64-encoded 256-bit AES key  |
| `JWT_SECRET`    | JWT signing secret              |

### Default Administrator

The Flyway seed migration creates a default administrator account:

```text
username: admin
password: admin
```

Change this password after first login in any persistent environment.

## Commands

This project uses Gradle for build and verification tasks. Just recipes provide convenience wrappers for local operations and API calls.

### Gradle

| Command                       | Description                                                           |
|-------------------------------|-----------------------------------------------------------------------|
| `./gradlew test`              | Run unit tests                                                        |
| `./gradlew integrationTest`   | Run integration tests                                                 |
| `./gradlew architectureTest`  | Run architecture tests                                                |
| `./gradlew coverage`          | Generate aggregate JaCoCo coverage reports                            |
| `./gradlew setup`             | Create the local `.env` file when it does not already exist           |
| `./gradlew compileAllClasses` | Compile all source sets without running tests                         |
| `./gradlew wsdl2java`         | Generate SOAP client classes from `wifi-platform.wsdl`                |

**Note:** `check` task is finalized by `integrationTest`, so `./gradlew check` also triggers integration tests after the main check work completes.

The `setup` task is provided by the project's internal environment Gradle plugin. It prepares a local development environment file with the variables expected by Gradle, Docker Compose, and the application runtime, while leaving an existing `.env` file unchanged.

### Just

| Command                                                                     | Description                                                          |
|-----------------------------------------------------------------------------|----------------------------------------------------------------------|
| `just`                                                                      | List available recipes                                               |
| `just deploy-dev`                                                           | Build and deploy the local development stack                         |
| `just redeploy-dev`                                                         | Recreate the local development stack                                 |
| `just deploy-prod`                                                          | Build and deploy the local production stack                          |
| `just redeploy-prod`                                                        | Recreate the local production stack                                  |
| `just compose <args>`                                                       | Run `docker compose` with arbitrary arguments                        |
| `just compose-dev <args>`                                                   | Run Docker Compose with development overlays                         |
| `just compose-prod <args>`                                                  | Run Docker Compose with production overlays                          |
| `just compose-build <args>`                                                 | Assemble the application and build the `wifi-admin-api` Docker image |
| `just server-start`                                                         | Start the backend with Gradle                                        |
| `just server-shutdown`                                                      | Shut down the backend through Actuator                               |
| `just server-health`                                                        | Read platform health through Actuator                                |
| `just server-sync`                                                          | Trigger platform synchronization                                     |
| `just server-logging <level>`                                               | Set application logging level                                        |
| `just server-payload-logging <true\|false>`                                 | Enable or disable SOAP payload logging                               |
| `just login [username] [password]`                                          | Authenticate and store the returned JWT in `.jwt`                    |
| `just wifi-get <cpe-id>`                                                    | Retrieve Wi-Fi configuration through the REST API                    |
| `just wifi-update <cpe-id> <ssid> [wifi-band] [encryption-type] [password]` | Update Wi-Fi configuration through the REST API                      |
| `just change-password <current-password> <new-password>`                    | Change the authenticated administrator password                      |
| `just soap-get <cpe-id>`                                                    | Retrieve Wi-Fi configuration directly from the SOAP platform         |
| `just soap-put <cpe-id> <ssid> [wifi-band] [encryption-type] [password]`    | Update Wi-Fi configuration directly on the SOAP platform             |
| `just grafana-export`                                                       | Export provisioned Grafana dashboards                                |
| `just codex-run`                                                            | Start the interactive Codex CLI                                      |
| `just codex-shell`                                                          | Open a root shell inside the Codex container                         |

## Testing

Tests are organized by source set:

| Source Set         | Location                    | Purpose                    |
|--------------------|-----------------------------|----------------------------|
| `test`             | `src/test/java`             | Unit tests                 |
| `integrationTest`  | `src/integrationTest/java`  | Integration and flow tests |
| `architectureTest` | `src/architectureTest/java` | Architecture rule tests    |
| `testFixtures`     | `src/testFixtures/java`     | Shared test fixtures       |

Generated SOAP classes live under `build/generated/sources/wsdl` and are excluded from aggregate coverage.

Read [TESTING](docs/TESTING.md) for more information.

## Persistence

The backend stores data in PostgreSQL using Spring Data JPA.

Schema changes are managed by Flyway migrations:

| Migration                           | Description                             |
|-------------------------------------|-----------------------------------------|
| `V1__create_wifi_configuration.sql` | Creates Wi-Fi configuration storage     |
| `V2__create_admin_accounts.sql`     | Creates administrator account storage   |
| `V3__seed_admin_account.sql`        | Seeds the default administrator account |

Wi-Fi passwords are encrypted at the persistence boundary using AES. Administrator passwords are hashed with BCrypt.

## Platform Integration

The backend integrates with the external WiFi platform through SOAP using Apache CXF:

SOAP contract is defined in `src/main/resources/wsdl/wifi-platform.wsdl`. Client classes are generated by the `wsdl2java` Gradle task and included in the main source set.

Platform communication is hardened by:

- Connection and receive timeouts
- Retry handling with exponential backoff
- SOAP fault decoding
- XML response normalization

## Synchronization

The backend synchronizes configured CPE devices from the external platform into local database on a configured schedule once a day, or optionally on server startup. 

Synchronization behavior is controlled by:

| Property                   | Description                                                  |
|----------------------------|--------------------------------------------------------------|
| `platform.sync-on-startup` | Runs synchronization during application startup when enabled |
| `platform.sync-schedule`   | Daily scheduled synchronization time                         |
| `platform.cpe-id-format`   | CPE identifier format                                        |
| `platform.cpe-id-count`    | Number of generated CPE identifiers to synchronize           |

Manual synchronization is available through:

```shell
just server-sync
```

or directly:

```shell
curl -X POST http://localhost:8082/actuator/sync
```

## Observability

The backend emits structured logs through Logback and exposes operational state through Spring Boot Actuator. 

The application uses trace identifiers to correlate synchronous REST handling, SOAP platform calls, asynchronous event handling, and persistence follow-up work. SOAP payload logging is controlled separately from the main application logging level because payloads may contain sensitive data.

Production Docker Compose deployment includes Grafana, Loki, and Alloy for log aggregation and dashboards.

## Deployment

The application is packaged as the `wifi-admin-api` Docker image and can be deployed with Docker Compose recipes.

Development deployment:

```shell
just deploy-dev
```

Production-style local deployment:

```shell
just deploy-prod
```

Read [DEPLOYMENT](docs/DEPLOYMENT.md) for more information.
