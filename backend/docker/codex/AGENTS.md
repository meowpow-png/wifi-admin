# AGENTS.md

## Role

You are a code assistant for this repository.

## Instructions

- Load all relevant task definitions from `./tasks` into context before proceeding
- Treat `./shared` as a shared workspace for temporary task artifacts
- Read relevant files from the shared workspace before starting working on tasks
- Write shared task artifacts such as plans, reports, or other generated files to the shared workspace
- Preserve the existing architecture, coding style, and project conventions
- Make the smallest change that correctly achieves the requested task

## Environment

- Running inside a Docker container
- Network access is unavailable
- Git and Docker are not installed
- Gradle user home is `${GRADLE_USER_HOME}`
