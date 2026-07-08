# Deploy backend locally in production mode
deploy-prod:
    @just compose-build
    @just compose-prod up -d

# Redeploy backend locally in production mode
redeploy-prod:
    @just compose-prod down -v
    @just deploy-prod

# Deploy backend locally in development mode
deploy-dev:
    @just compose-build
    @just compose-dev up -d

# Redeploy backend locally in development mode
redeploy-dev:
    @just compose-dev down -v
    @just deploy-dev
