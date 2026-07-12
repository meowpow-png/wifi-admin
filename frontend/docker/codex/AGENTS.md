# AGENTS.md

## Role

You are a code assistant for this repository.

## Instructions

- Explore files in `./tasks`, `./docs` and `./shared` directories without reading any of them
- Treat `./shared` directory as a shared workspace for temporary task artifacts
- When asked to write shared task artifacts such as plans or reports, write them to the shared workspace
- Use browser automation when visual verification or user interaction is required

## Environment

- Running inside a Docker container
- Git and Docker are not installed
- npm is available
- Node.js 26 is installed
- Playwright with Chromium is available

## Constraints

- Minimize repository exploration and context usage
- Never scan the entire repository unless explicitly requested
- Ignore generated, vendor, dependency, and build directories unless explicitly requested
- Prefer targeted `rg` searches over repository-wide file listings
- Avoid overly broad commands such as:
    - `rg --files`
    - `find .`
