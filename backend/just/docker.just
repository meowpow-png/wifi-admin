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

# Build the backend Docker image
compose-build *args:
    @./gradlew clean assemble
    @just compose-dev build {{args}}

# Build codex agent docker image
codex-build *args:
    @just compose-dev build codex {{args}}

# Start and enter codex agent docker container
codex-run:
    @just codex-build
    @./gradlew compileAllClasses
    @just compose-dev run --rm codex

# Start an interactive shell in the codex container
codex-shell:
    @just codex-build
    @just compose-dev run --rm -it codex sh
