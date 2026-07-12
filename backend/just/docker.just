compose_dev := "-f compose.yml -f compose-dev.yml"
compose_prod := "-f compose.yml -f compose-prod.yml"

# Run Docker Compose
[arg("args", help="Arguments passed to docker compose")]
compose *args:
    @docker compose {{args}}

# Run Docker Compose for the development stack
[arg("args", help="Arguments passed to docker compose")]
compose-dev *args:
    @LOCAL_UID=$(id -u) LOCAL_GID=$(id -g) \
        docker compose {{compose_dev}} {{args}}

# Run Docker Compose for the production stack
[arg("args", help="Arguments passed to docker compose")]
compose-prod *args:
    @docker compose {{compose_prod}} {{args}}

# Build backend Docker image
[arg("args", help="Arguments passed to docker compose")]
api-build *args:
    @./gradlew clean assemble
    @just compose-dev build {{args}}

# Start and enter codex agent docker container
codex-run:
    @just compose-dev build codex
    @./gradlew compileAllClasses
    @just compose-dev run --rm codex

# Start an interactive shell in the codex container
codex-shell:
    @just compose-dev build codex
    docker compose {{compose_dev}} run \
        --rm \
        --user root \
        --entrypoint sh codex
