dev := "-f compose.dev.yml"
prod := "-f compose.prod.yml"

# Run Docker Compose
[arg("args", help="Arguments passed to docker compose")]
compose *args:
    @docker compose {{args}}

# Run Docker Compose for development stack
[arg("args", help="Arguments passed to docker compose")]
compose-dev *args:
    @LOCAL_UID=$(id -u) LOCAL_GID=$(id -g) \
        docker compose {{dev}} {{args}}

# Run Docker Compose for production stack
[arg("args", help="Arguments passed to docker compose")]
compose-prod *args:
    @docker compose {{prod}} {{args}}

# Build backend Docker image
[arg("args", help="Arguments passed to docker compose")]
api-build *args:
    @just gradle clean assemble
    @just compose-dev build {{args}}

# Start interactive Codex CLI
codex:
    @just compose-dev run --rm codex
