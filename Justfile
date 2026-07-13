set shell := ["bash", "-eu", "-o", "pipefail", "-c"]
set dotenv-load

mod frontend "frontend/Justfile"
mod backend "backend/Justfile"

import 'just/docker.just'
import 'just/soap.just'

platform-url := "http://localhost:8080/platform"

# List available recipes
default:
    @just --list

# Export provisioned Grafana dashboards
grafana-export:
    @./just/scripts/grafana-export.sh

# Clean all modules
clean:
    @just backend::gradle clean
    @just frontend::npm run clean

# Setup project
setup:
    @just backend::gradle setup

# Build all modules
build:
    @just backend::gradle assemble
    @just frontend::npm run build

# Deploy locally in production mode
deploy:
    @just backend::compose-dev build
    @just frontend::compose build
    @just compose up -d

# Redeploy locally in production mode
redeploy:
    @just compose down -v
    @just deploy
