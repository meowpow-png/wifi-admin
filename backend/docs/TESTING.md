# Testing

## Annotations

Use the following annotations consistently throughout the test suite:

- `@IntegrationTest` — marks integration tests
- `@Nested` — groups related test scenarios
- `@DisplayName` — provides a readable description in test reports

```java
class ExampleServiceTest {

    @Nested
    class FindUserMethodTests {

        @Test
        @DisplayName("Returns default user when none is found")
        void should_ReturnDefaultUser_when_UserIsMissing() { }
    }
}
```

## Naming

### Test Classes

Top-level test classes should reflect the production class they test, use singular form, and end with `Test`.

```java
class ExampleServiceTest { }
```

Nested classes should group related scenarios, use plural form, and end with `Tests`.

Nested classes corresponding to a production method should follow the pattern `<methodName>MethodTests`, and their display name should match the method name.

```java
@Nested
@DisplayName("enqueue")
class EnqueueMethodTests { }
```

### Test Methods

Test methods should follow the pattern:

```text
should_<expected_behavior>_when_<condition>
```

**Example:**

```java
void should_ReturnDefaultUser_when_UserIsMissing() { }
```

### Display Names

Display names should describe the observable behavior using natural language.

```java
@DisplayName("Returns default user when none is found")
```

### Lifecycle Methods

Lifecycle methods should clearly identify the tests they prepare or clean up.

```java
@BeforeEach
void setupExampleServiceTest() { }

@AfterEach
void cleanupExampleServiceTest() { }
```

## Structure

Tests should follow **Arrange–Act–Assert** pattern, separating test setup, the action under test, and the verification of the outcome.

```java
@Test
@DisplayName("Throws exception when the user is not found")
void should_ThrowException_when_UserIsNotFound() {
    UserRepository repository = new InMemoryUserRepository();

    Executable action = () -> repository.findById("missing-id");

    assertThatThrownBy(action)
            .isInstanceOf(UserNotFoundException.class);
}
```

**Important:** Do not include boilerplate `// arrange`, `// act`, or `// assert` comments; the test structure should be evident from the code itself.

## Behavior

- Each test should verify a single, observable behavior
- Focus on outcomes that a caller can observe rather than internal steps
- Avoid verifying implementation details, or interactions that are not part of observable behavior
- If multiple behaviors need to be verified, write separate tests

## Setup

- Keep test setup local to the test whenever practical
- Prepare only the state required to verify the behavior under test
- Prefer inline setup when it makes the test easier to understand
- Use helper methods only when they improve readability
- Avoid shared mutable fixtures or setup that creates coupling between tests

## Assertions

Use AssertJ for all assertions. Assert observable outcomes using fluent, expressive assertions that clearly communicate the expected behavior. Prefer the most direct assertion available, and keep each assertion focused on the behavior under test.

```java
// Avoid:
assertThat(usernames.stream().anyMatch(u -> u.equals("alice"))).isTrue();

// Prefer:
assertThat(usernames).contains("alice");
```

```java
// Avoid:
assertThat(result.isPresent()).isTrue();
assertThat(result.get()).isEqualTo(user);

// Prefer:
assertThat(result).contains(user);
```

```java
// Avoid:
assertThatThrownBy(action)
        .isInstanceOf(RuntimeException.class);

// Prefer:
assertThatThrownBy(action)
        .isInstanceOf(UserNotFoundException.class);
```

## Test Scope

Write enough tests to provide confidence in the behavior of the system without covering unnecessary permutations or speculative edge cases. Focus on behaviors defined by the contract, business rules, or integration boundaries, and stop once those behaviors have been verified.

For example, if a method accepts a `String` parameter and the behavior is identical for `"alice"` and `"bob"`, testing both values provides little additional confidence. Prefer a single representative test unless the input itself changes the expected behavior.

## Unit Testing

### Selection

Select classes that implement meaningful, observable behavior and can be tested in isolation. A good unit test candidate can detect regressions without involving Spring, the database, the network, or other external infrastructure.

Good candidates include:

- Services with business logic
- Validators
- Mappers with non-trivial logic
- Parsers and converters
- Utility classes with meaningful behavior
- Authentication and authorization components
- Retry, scheduling, or synchronization logic

Poor candidates include:

- DTOs and simple records
- JPA entities
- Spring Data repositories
- Configuration properties
- Constant holders
- Generated code
- Thin wrappers that only delegate

When in doubt, ask:

> If this class were rewritten incorrectly while preserving its public API, could a unit test detect the regression without involving Spring, the database, or the network?

### Planning

Plan unit tests from the behavioral contracts of the class's public API. A behavioral contract is an observable promise made to callers, such as validating inputs, returning a value, reporting an error, or maintaining state.

Plan tests in the following order:

1. Identify the class's public methods
2. Determine the behavioral contracts for each method
3. Write one focused test for each behavioral contract
4. Stop once every meaningful behavioral contract has been verified

If multiple inputs produce the same observable behavior, prefer a single representative case unless the API explicitly defines different outcomes.

### Scope

Interact with the class exclusively through its public API and assert only observable outcomes. Do not test private methods, internal state, implementation details, or interactions that are not part of the observable behavior.

### Mocking

Use mocks only to isolate external collaborators. Keep stubbing minimal and limited to behavior required by the test. Prefer simple fakes when they express the scenario more clearly than mocks.

## Integration Testing

### Selection

Select integration tests for behaviors whose correctness depends on real infrastructure, framework-managed behavior, or communication across integration boundaries. A good integration test verifies interactions that cannot be reliably reproduced using mocks or isolated unit tests.

Good candidates include:

- REST controllers and request validation
- Database persistence and repository queries
- Client integrations with external systems
- JSON or XML serialization and deserialization
- Spring-managed behavior such as transactions, scheduling, or event handling
- Messaging or file storage integrations

Poor candidates include:

- Pure business logic
- Validators
- Mappers and converters
- Utility classes
- Parsers

When in doubt, ask:

> If this behavior were implemented incorrectly while preserving the public API, could a unit test reliably detect the regression without involving Spring, the database, the network, or other real infrastructure?

### Planning

Plan integration tests from the observable behaviors of each integration boundary. An integration boundary is any interaction whose correctness depends on real infrastructure, framework-managed behavior, or communication with external systems.

Plan tests in the following order:

1. Identify the integration boundary
2. Determine the observable behaviors of that boundary
3. Write one focused test for each meaningful behavior
4. Stop once every meaningful boundary behavior has been verified

If multiple scenarios exercise the same integration behavior and produce the same observable outcome, prefer a single representative test unless the boundary explicitly defines different outcomes.

### Scope

Exercise the system through its public integration points and assert only observable outcomes. Verify interactions that depend on real infrastructure or framework-managed behavior, without asserting implementation details or internal interactions that are not part of the integration boundary.

### Infrastructure

<!-- TODO: Document the shared integration test infrastructure, including the custom @IntegrationTest annotation, application startup, supporting services, and common test configuration inherited by all integration tests. -->

### Test Data

<!-- TODO: Document how integration tests prepare, isolate, and clean up test data, including any shared fixtures, helper utilities, or database initialization strategy used throughout the test suite. -->

### External Services

<!-- TODO: Document how integration tests interact with external services, including the use of mock implementations, configuring service behavior, and conventions for verifying integrations with external dependencies. -->

## Wiring Tests

Wiring tests verify Spring configuration, bean registration, and framework-managed behavior without starting the full application. They provide a fast, isolated way to validate application context wiring and framework configuration.

### Auto-Configuration

Auto-configuration tests verify that configuration classes register the expected beans under different application configurations. They are typically used to validate conditional bean registration based on configuration properties, available dependencies, or other conditions.

```java
TestApplicationContextRunner.from(runner)
        .withPropertyValues("feature.enabled=true")
        .hasBean(FeatureService.class)
        .doesNotFail();
```

### Bean Wiring

Bean wiring tests verify that the application context contains the expected beans and that Spring can successfully resolve their dependencies. They provide confidence that the application context is assembled correctly and that changes to configuration do not unintentionally break dependency injection.

```java
TestApplicationContextRunner.from(runner)
        .hasBean(UserService.class)
        .hasBean("passwordEncoder")
        .doesNotFail();
```

### Properties

Configuration property tests verify that application properties are correctly bound and influence the application context as intended. They are typically used to validate default values, custom configuration, and property-driven conditional behavior.

```java
TestApplicationContextRunner.from(runner)
        .withPropertyValues("retry.max-attempts=5")
        .withBean(RetryProperties.class, properties ->
                assertThat(properties.maxAttempts()).isEqualTo(5))
        .doesNotFail();
```

### Transactions

Transaction management tests verify that transactional boundaries are applied to the expected service methods. They help ensure that Spring's transaction management is configured correctly and continues to protect operations that require transactional behavior.

```java
TestApplicationContextRunner.from(runner)
        .withTransactionManagement()
        .hasTransactionalMethods(UserService.class)
        .doesNotFail();
```

### Scheduling

Scheduling tests verify that scheduled tasks are registered with Spring and configured as intended. They help ensure that scheduled methods remain discoverable and continue to use the expected scheduling configuration as the application evolves.

```java
TestApplicationContextRunner.from(runner)
        .withSchedulingEnabled()
        .hasScheduledMethod(SynchronizationScheduler.class, "synchronize")
        .usesCronFrom("synchronizationProperties")
        .doesNotFail();
```
