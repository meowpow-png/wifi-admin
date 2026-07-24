# Run root Docker Compose
[arg("args", help="Arguments passed to docker compose")]
compose *args:
    @LOCAL_UID=$(id -u) LOCAL_GID=$(id -g) \
        docker compose -f compose.dev.yml {{args}}

# Execute a command inside the running Codex container
[arg("args", help="Command to execute inside Codex container")]
codex-exec *args:
    @just compose up -d codex
    @docker compose exec \
        --user "$(id -u):$(id -g)" \
        codex \
        {{args}}

# Start the interactive Codex CLI
codex-run:
    @just codex-exec codex

# Open a root shell inside the Codex container
codex-shell:
    @just compose up -d codex
    @just compose exec --user root codex bash
