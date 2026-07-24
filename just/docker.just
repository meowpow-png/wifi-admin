# Run Docker Compose for production stack
[arg("args", help="Arguments passed to docker compose")]
compose *args:
    @docker compose -f compose.yml {{args}}

# Run Docker Compose for development stack
[arg("args", help="Arguments passed to docker compose")]
compose-dev *args:
    @LOCAL_UID=$(id -u) LOCAL_GID=$(id -g) \
        docker compose -f compose.dev.yml {{args}}

# Start interactive Codex CLI
codex:
    @just compose-dev run --rm codex
