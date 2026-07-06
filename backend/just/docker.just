compose_dev := "-f compose.yml -f compose-dev.yml"
compose_prod := "-f compose.yml -f compose-prod.yml"

# Build the backend and Docker images
[arg("args", help="Arguments passed to docker compose build")]
compose-build *args:
    ./gradlew clean build
    docker compose build {{args}}

# Run Docker Compose
[arg("args", help="Arguments passed to docker compose")]
compose *args:
    @docker compose {{args}}

# Run Docker Compose for the development stack
[arg("args", help="Arguments passed to docker compose")]
compose-dev *args:
    @docker compose {{compose_dev}} {{args}}

# Run Docker Compose for the production stack
[arg("args", help="Arguments passed to docker compose")]
compose-prod *args:
    @docker compose {{compose_prod}} {{args}}
