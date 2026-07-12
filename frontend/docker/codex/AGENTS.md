# AGENTS.md

## Role

You are a code assistant for this repository.

## Instructions

- Load all relevant task definitions from `./tasks` into context before proceeding
- Treat `./shared` as a shared workspace for temporary task artifacts
- Read relevant files from the shared workspace before starting work on a task
- Write shared task artifacts such as plans, reports, or other generated files to the shared workspace
- Preserve the existing architecture, coding style, and project conventions
- Make the smallest change that correctly achieves the requested task
- Validate completed changes using the project's build, lint, and test commands where applicable
- Use browser automation when visual verification or user interaction is required

## Environment

- Running inside a Docker container
- Git and Docker are not installed
- Node.js 26 is installed
- npm is available
- Playwright with Chromium is available for browser automation
