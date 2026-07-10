package hr.ht.rnd.wifiadmin.test.support;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.DefaultApplicationArguments;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.context.assertj.AssertableApplicationContext;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Fluent builder for configuring and
 * executing {@link ApplicationContextRunner} tests.
 */
public final class TestApplicationContextRunner {

    private TestApplicationContextRunner() {}

    /**
     * Creates a builder for the specified
     * application context runner.
     *
     * @param runner the application context runner
     *
     * @return the builder
     */
    public static Builder from(ApplicationContextRunner runner) {
        return new Builder(runner);
    }

    public static final class Builder {

        private final List<Consumer<SpringWiringTestAssertion>> assertions = new ArrayList<>();
        private ApplicationContextRunner runner;

        private Builder(ApplicationContextRunner runner) {
            this.runner = runner;
        }

        /**
         * Adds property values to the application context.
         *
         * @param properties the property values
         *
         * @return the builder
         *
         * @see ApplicationContextRunner#withPropertyValues(String...)
         */
        public Builder withPropertyValues(String... properties) {
            this.runner = runner.withPropertyValues(properties);
            return this;
        }

        /**
         * Adds user configuration classes
         * to the application context.
         *
         * @param configs the configuration classes
         *
         * @return the builder
         *
         * @see ApplicationContextRunner#withUserConfiguration(Class[])
         */
        public Builder withConfiguration(Class<?>... configs) {
            this.runner = runner.withUserConfiguration(configs);
            return this;
        }

        public <T> Builder withBean(Class<T> beanType, Consumer<T> assertion) {
            assertions.add(a -> {
                var bean = a.getContext().getBean(beanType);
                assertion.accept(bean);
            });
            return this;
        }

        /**
         * Asserts that the application context
         * contains exactly one bean of the specified type.
         *
         * @param beanType the expected bean type
         *
         * @return the builder
         */
        public Builder hasBean(Class<?> beanType) {
            assertions.add(a -> a.hasSingleBean(beanType));
            return this;
        }

        /**
         * Asserts that the application context
         * contains a bean with the specified name.
         *
         * @param beanName the expected bean name
         *
         * @return the builder
         */
        public Builder hasBean(String beanName) {
            assertions.add(a -> a.hasBean(beanName));
            return this;
        }

        /**
         * Asserts that the application context
         * does not contain any beans of the specified type.
         *
         * @param beanType the bean type expected to be absent
         *
         * @return the builder
         */
        public Builder doesNotHaveBean(Class<?> beanType) {
            assertions.add(a -> a.doesNotHaveBean(beanType));
            return this;
        }

        /**
         * Asserts that at least one scheduled task uses
         * the cron expression provided by the specified bean.
         *
         * @param beanName the name of the bean providing the expected cron expression
         *
         * @return the builder
         */
        public Builder usesCronFrom(String beanName) {
            assertions.add(a -> a.usesCronFrom(beanName));
            return this;
        }

        /**
         * Asserts that the specified scheduled
         * method is registered with Spring's
         * scheduling infrastructure.
         *
         * @param beanType the expected bean type
         * @param methodName the expected scheduled method
         *
         * @return the builder
         */
        public Builder hasScheduledMethod(Class<?> beanType, String methodName) {
            assertions.add(a -> a.hasScheduledMethod(beanType, methodName));
            return this;
        }

        /**
         * Asserts that all public methods of
         * the specified bean are transactional.
         *
         * @param beanType the expected bean type
         *
         * @return the builder
         */
        public Builder hasTransactionalMethods(Class<?> beanType) {
            assertions.add(a -> a.hasAllTransactionalMethods(beanType));
            return this;
        }

        /**
         * Asserts that the specified method of
         * the given bean is transactional.
         *
         * @param beanType the expected bean type
         * @param methodName the expected transactional method
         *
         * @return the builder
         */
        public Builder hasTransactionalMethod(Class<?> beanType, String methodName) {
            assertions.add(a -> a.hasTransactionalMethod(beanType, methodName));
            return this;
        }

        /**
         * Enables Spring transaction management
         * for the application context.
         *
         * @return the builder
         */
        public Builder withTransactionManagement() {
            return withConfiguration(TestTransactionConfiguration.class);
        }

        /**
         * Enables Spring scheduling
         * for the application context.
         *
         * @return the builder
         */
        public Builder withSchedulingEnabled() {
            return withConfiguration(TestSchedulerConfiguration.class);
        }

        /**
         * Executes all {@link ApplicationRunner}
         * beans registered in the application context.
         *
         * @param args the application arguments
         *
         * @return the builder
         */
        public Builder runApplicationRunners(String... args) {
            assertions.add(a -> {
                var context = a.getContext();
                var runners = context.getBeansOfType(ApplicationRunner.class);
                var applicationArgs = new DefaultApplicationArguments(args);

                runners.values().forEach(r -> {
                    try {
                        r.run(applicationArgs);
                    }
                    catch (Exception e) {
                        throw new ApplicationRunnerException(e);
                    }
                });
            });
            return this;
        }

        /**
         * Runs the application context and
         * verifies all configured assertions.
         */
        public void doesNotFail() {
            runner.run(context -> {
                var assertion = assertion(context).starts();
                assertions.forEach(a -> a.accept(assertion));
            });
        }

        /**
         * Runs the application context and verifies
         * that the expected exception is thrown.
         * <p>
         * <strong>API Note:</strong>
         * Exceptions thrown during application context startup,
         * configured assertions, or {@link ApplicationRunner}
         * execution are all matched against the expected exception type.
         *
         * @param expected the expected exception type
         */
        public void failsWithException(Class<? extends Throwable> expected) {
            runner.run(context -> {
                var startupFailure = context.getStartupFailure();
                if (startupFailure != null) {
                    if (expected.isInstance(startupFailure)) {
                        return;
                    }
                    assertThat(startupFailure).hasCauseInstanceOf(expected);
                    return;
                }
                var assertion = assertion(context);
                try {
                    assertions.forEach(a -> a.accept(assertion));
                }
                catch (ApplicationRunnerException e) {
                    var cause = e.getCause() != null ? e.getCause() : e;
                    assertThat(cause).isInstanceOf(expected);
                    return;
                }
                var message = "Expected exception of type %s but nothing was thrown";
                throw new AssertionError(message.formatted(expected.getName()));
            });
        }

        private SpringWiringTestAssertion assertion(AssertableApplicationContext ctx) {
            return SpringWiringTestAssertion.assertThatContext(ctx);
        }
    }

    static final class ApplicationRunnerException extends RuntimeException {

        ApplicationRunnerException(Throwable cause) {
            super(cause);
        }
    }

    @TestConfiguration
    @EnableTransactionManagement
    static class TestTransactionConfiguration {}

    @EnableScheduling
    @TestConfiguration
    static class TestSchedulerConfiguration {}
}
