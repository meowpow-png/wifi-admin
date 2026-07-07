# Deploy backend locally in production mode
deploy-prod:
    @./gradlew clean build
    @just compose-prod build
    @just compose-prod up -d

# Redeploy backend locally in production mode
redeploy-prod:
    @just compose-prod down -v
    @just deploy-prod

# Deploy backend locally in development mode
deploy-dev:
    @./gradlew clean build
    @just compose-dev build
    @just compose-dev up -d

# Redeploy backend locally in development mode
redeploy-dev:
    @just compose-dev down -v
    @just deploy-dev
