# Write Test Suite

Write a complete test suite for the given class.

## Setup

- Read `TESTING.md`
- If the required testing type is not clearly defined, ask for clarification before proceeding
- Read existing test classes of the required type to understand the project's testing style
- Read classes in the `testFixtures` module to discover available fixtures
- Only read production classes that are part of the given class's public API

## Steps

- Analyze the given class according to the guidance for the required testing type
- Implement one focused test for each meaningful contract or integration behavior
- Stop once all meaningful behaviors have been verified

## Notes

- Use representative test cases and avoid redundant permutations
- Preserve the project's coding, testing, and formatting conventions
- If a required production class is not available on the test classpath, add the appropriate test dependency in `build.gradle.kts` instead of using reflection or other workarounds
- Do not run tests unless specifically requested to do so
