#!/usr/bin/env bash
set -euo pipefail

export HOME=/home/codex

mkdir -p "$HOME/.codex"
chown -R "${LOCAL_UID}:${LOCAL_GID}" "$HOME"
chown "${LOCAL_UID}:${LOCAL_GID}" /workspace

if [ ! -f "$HOME/.codex/config.toml" ]; then
    cp /usr/local/share/codex/config.toml "$HOME/.codex/config.toml"
    chown "${LOCAL_UID}:${LOCAL_GID}" "$HOME/.codex/config.toml"
fi

exec gosu "${LOCAL_UID}:${LOCAL_GID}" \
    env HOME=/home/codex \
        CODEX_HOME=/home/codex/.codex \
        codex "$@"
