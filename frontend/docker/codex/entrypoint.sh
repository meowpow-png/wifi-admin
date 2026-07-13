#!/usr/bin/env bash
set -euxo pipefail

HOME=/home/codex

mkdir -p "$HOME/.codex"

echo "Initializing Codex configuration..."
mkdir -p /workspace/.codex/hooks

cp /usr/local/share/codex/config.toml /workspace/.codex/config.toml
cp -a /usr/local/share/codex/hooks/. /workspace/.codex/hooks/

echo "Updating ownership of $HOME..."
chown -R "${LOCAL_UID}:${LOCAL_GID}" "$HOME"

echo "Updating ownership of /workspace..."
chown -R "${LOCAL_UID}:${LOCAL_GID}" /workspace/.codex
chown "${LOCAL_UID}:${LOCAL_GID}" /workspace

echo "Installing npm dependencies..."
gosu "${LOCAL_UID}:${LOCAL_GID}" \
    env HOME="$HOME" \
    npm ci

exec env \
    HOME="$HOME" \
    CODEX_HOME="$HOME/.codex" \
    gosu "${LOCAL_UID}:${LOCAL_GID}" \
    "$@"
