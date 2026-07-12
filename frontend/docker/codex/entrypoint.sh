#!/usr/bin/env bash
set -euo pipefail

HOME=/home/codex

mkdir -p "$HOME/.codex"

echo "Updating ownership of $HOME..."
chown -R "${LOCAL_UID}:${LOCAL_GID}" "$HOME"

if [ "$(stat -c '%u:%g' /workspace)" != "${LOCAL_UID}:${LOCAL_GID}" ]; then
    echo "Updating ownership of /workspace..."
    chown "${LOCAL_UID}:${LOCAL_GID}" /workspace
fi

if [ ! -f "$HOME/.codex/config.toml" ]; then
    echo "Initializing Codex configuration..."
    cp /usr/local/share/codex/config.toml "$HOME/.codex/config.toml"
    chown "${LOCAL_UID}:${LOCAL_GID}" "$HOME/.codex/config.toml"
fi

exec env \
    HOME="$HOME" \
    CODEX_HOME="$HOME/.codex" \
    gosu "${LOCAL_UID}:${LOCAL_GID}" \
    "$@"
