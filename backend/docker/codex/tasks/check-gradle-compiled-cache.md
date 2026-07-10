# Check Gradle Compiled Cache

Check whether compiled class outputs exist and the Gradle cache is populated.

## Setup

- Do not run Gradle commands
- Inspect the project build files to identify source sets and declared dependencies
- Determine the active Gradle user home from the environment before checking dependency caches

## Steps

- Analyze compiled class output directories for each source set
- Compare Java sources with their expected class files and identify missing or stale outputs
- Inspect generated source outputs when they are part of the main source set
- Inspect local Gradle project caches, wrapper distribution caches, and dependency artifact caches
- Check whether declared direct dependencies and plugins appear in the cache

## Output

Return a concise readiness summary that states whether classes are compiled, 
whether any outputs are missing or stale, and whether the Gradle cache appears fully populated.
