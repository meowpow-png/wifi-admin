#!/usr/bin/env bash
set -euo pipefail

HOME=/home/codex

mkdir -p "$HOME/.codex"

if [ "$(stat -c '%u:%g' "$HOME")" != "${LOCAL_UID}:${LOCAL_GID}" ]; then
    echo "Updating ownership of $HOME..."
    chown -R "${LOCAL_UID}:${LOCAL_GID}" "$HOME"
fi

if [ "$(stat -c '%u:%g' /workspace)" != "${LOCAL_UID}:${LOCAL_GID}" ]; then
    echo "Updating ownership of /workspace..."
    chown "${LOCAL_UID}:${LOCAL_GID}" /workspace
fi

if [ ! -f "$HOME/.codex/config.toml" ]; then
    echo "Initializing Codex configuration..."
    cp /usr/local/share/codex/config.toml "$HOME/.codex/config.toml"
    chown "${LOCAL_UID}:${LOCAL_GID}" "$HOME/.codex/config.toml"
fi

if [ "${CODEX_WARMUP:-false}" = "true" ]; then
    echo "Precompiling project classes..."
    if ! gosu "${LOCAL_UID}:${LOCAL_GID}" \
        env \
            HOME="$HOME" \
            GRADLE_USER_HOME="$HOME/.gradle" \
        ./gradlew --no-daemon --console=plain --quiet compileAllClasses; then
        echo "Warning: Failed to precompile project classes."
    fi
fi

exec env \
    HOME="$HOME" \
    CODEX_HOME="$HOME/.codex" \
    GRADLE_USER_HOME="$HOME/.gradle" \
    gosu "${LOCAL_UID}:${LOCAL_GID}" \
    "$@"
