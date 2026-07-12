#!/usr/bin/env bash
set -euxo pipefail

HOME=/home/codex

mkdir -p "$HOME/.codex"

if [ ! -f "$HOME/.codex/config.toml" ]; then
    echo "Initializing Codex configuration..."
    cp /usr/local/share/codex/config.toml "$HOME/.codex/config.toml"
fi
echo "Updating ownership of $HOME..."
chown -R "${LOCAL_UID}:${LOCAL_GID}" "$HOME"

exec env \
    HOME="$HOME" \
    CODEX_HOME="$HOME/.codex" \
    gosu "${LOCAL_UID}:${LOCAL_GID}" \
    "$@"
