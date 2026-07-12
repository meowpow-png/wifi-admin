# AGENTS.md

## Role

You are a code assistant for this repository.

## Instructions

- Explore files in `./tasks`, `./docs`, and `./shared` directories without reading them
- Load only the task definitions required for the current request
- Treat `./shared` as a shared workspace for temporary task artifacts
- When asked to write shared task artifacts such as plans or reports, write them to the shared workspace
- Preserve the existing architecture, coding style, and project conventions
- Make the smallest change that correctly achieves the requested task

## Environment

- Running inside a Docker container
- Git and Docker are not installed
- Gradle user home is `${GRADLE_USER_HOME}`

## Constraints

- Minimize repository exploration and context usage
- Never scan the entire repository unless explicitly requested
- Ignore generated, vendor, dependency, and build directories unless explicitly requested
- Prefer targeted `rg` searches over repository-wide file listings
- Avoid overly broad commands such as:
    - `rg --files`
    - `find .`
