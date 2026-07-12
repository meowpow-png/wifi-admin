FROM node:26-bookworm-slim

RUN apt-get update && \
    apt-get install -y --no-install-recommends \
        bash \
        ca-certificates \
        curl \
        git \
        gosu \
        python3 && \
    npm install --global \
        @openai/codex@0.143.0 \
        playwright && \
    npx playwright install --with-deps chromium && \
    rm -rf /var/lib/apt/lists/*

COPY config.toml /usr/local/share/codex/config.toml
COPY hooks/ /usr/local/share/codex/hooks/
COPY entrypoint.sh /usr/local/bin/entrypoint.sh

RUN chmod +x /usr/local/bin/entrypoint.sh
RUN chmod +x \
    /usr/local/bin/entrypoint.sh \
    /usr/local/share/codex/hooks/prevent_repo_scan.py

WORKDIR /workspace

ENTRYPOINT ["/usr/local/bin/entrypoint.sh"]
CMD ["sleep", "infinity"]
