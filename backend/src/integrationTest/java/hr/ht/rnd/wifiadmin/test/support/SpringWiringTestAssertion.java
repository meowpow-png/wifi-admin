package hr.ht.rnd.wifiadmin.test.support;

import org.springframework.aop.support.AopUtils;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor;
import org.springframework.scheduling.config.CronTask;
import org.springframework.scheduling.config.ScheduledTask;
import org.springframework.transaction.interceptor.TransactionAttributeSource;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Fluent assertions for verifying
 * Spring application context wiring.
 */
@SuppressWarnings("UnusedReturnValue")
public final class SpringWiringTestAssertion {

    private final TestApplicationContext context;

    private SpringWiringTestAssertion(TestApplicationContext context) {
        this.context = context;
    }

    /**
     * Asserts that the context started successfully.
     *
     * @return the fluent assertion
     */
    public SpringWiringTestAssertion starts() {
        var message = "Expected application context to start successfully, but it failed";
        assertThat(context.startupFailure()).as(message).isNull();

        return this;
    }

    /**
     * Asserts that the context contains
     * exactly one bean of the specified type.
     *
     * @param beanType the expected bean type
     *
     * @return the fluent assertion
     */
    public SpringWiringTestAssertion hasSingleBean(Class<?> beanType) {
        var message = "Expected exactly one bean of type %s, but found none or multiple";
        assertThat(context.getBeansOfType(beanType))
                .as(message.formatted(beanType.getSimpleName()))
                .hasSize(1);

        return this;
    }

    /**
     * Asserts that the context contains
     * a bean with the specified name.
     *
     * @param beanName the expected bean name
     *
     * @return the fluent assertion
     */
    public SpringWiringTestAssertion hasBean(String beanName) {
        var message = "Expected bean named '%s' to be present in context, but it was not found";
        assertThat(context.containsBean(beanName))
                .as(message.formatted(beanName))
                .isTrue();

        return this;
    }

    /**
     * Asserts that the context does not
     * contain any beans of the specified type.
     *
     * @param beanType the bean type expected to be absent
     *
     * @return the fluent assertion
     */
    public SpringWiringTestAssertion doesNotHaveBean(Class<?> beanType) {
        var message = "Expected no beans of type %s to be present in context, but at least one was found";
        assertThat(context.getBeansOfType(beanType))
                .as(message.formatted(beanType.getSimpleName()))
                .isEmpty();

        return this;
    }

    /**
     * Asserts that at least one scheduled task uses
     * the cron expression provided by the specified bean.
     *
     * @param cronBeanName the name of the bean
     * providing the expected cron expression
     *
     * @return the fluent assertion
     */
    public SpringWiringTestAssertion usesCronFrom(String cronBeanName) {
        var cron = context.getBean(cronBeanName, String.class);

        var cronTasks = postProcessor().getScheduledTasks().stream()
                .map(ScheduledTask::getTask)
                .filter(CronTask.class::isInstance)
                .map(CronTask.class::cast)
                .toList();

        var m1 = "Expected at least one scheduled cron task to be registered";
        assertThat(cronTasks).as(m1).isNotEmpty();

        var m2 = "Expected at least one scheduled task to use cron expression from bean '%s'";
        assertThat(cronTasks)
                .as(m2.formatted(cronBeanName))
                .anyMatch(task -> task.getExpression().equals(cron));

        return this;
    }

    /**
     * Asserts that the specified scheduled method is
     * registered with Spring's scheduling infrastructure.
     * <p>
     * <strong>Implementation Note:</strong>
     * Spring does not expose scheduled methods through
     * a public API. This assertion identifies scheduled
     * methods using the task description provided by Spring.
     *
     * @param beanType the type of the bean containing the scheduled method
     * @param methodName the name of the scheduled method
     *
     * @return the fluent assertion
     */
    public SpringWiringTestAssertion hasScheduledMethod(Class<?> beanType, String methodName) {
        var scheduledMethods = postProcessor().getScheduledTasks().stream()
                .map(ScheduledTask::getTask)
                .map(Object::toString)
                .toList();

        var message = "Expected scheduled method %s.%s to be registered";
        assertThat(scheduledMethods)
                .as(message.formatted(beanType.getSimpleName(), methodName))
                .anyMatch(task -> isScheduledMethod(task, beanType, methodName));

        return this;
    }

    /**
     * Asserts that a Spring event listener
     * is declared for the expected event type.
     *
     * @param beanType the type of the bean containing the listener method
     * @param eventType the expected event type
     *
     * @return the fluent assertion
     */
    public SpringWiringTestAssertion hasEventListenerMethod(Class<?> beanType, Class<?> eventType) {
        var bean = context.getBean(beanType);
        var targetClass = AopUtils.getTargetClass(bean);

        var listeners = findEventListenerMethods(targetClass, eventType);
        var listener = "%s listener for %s".formatted(
                targetClass.getSimpleName(),
                eventType.getSimpleName()
        );
        assertThat(listeners)
                .as("Expected %s to declare exactly one event listener method", listener)
                .hasSize(1);

        return this;
    }

    /**
     * Asserts that an asynchronous method
     * is declared for the expected argument type.
     *
     * @param beanType the type of the bean containing the async method
     * @param argumentType the expected argument type
     *
     * @return the fluent assertion
     */
    public SpringWiringTestAssertion hasAsyncMethod(Class<?> beanType, Class<?> argumentType) {
        var bean = context.getBean(beanType);
        var targetClass = AopUtils.getTargetClass(bean);

        var asyncMethods = findAsyncMethods(targetClass, argumentType);
        var method = "%s async method for %s".formatted(
                targetClass.getSimpleName(),
                argumentType.getSimpleName()
        );
        assertThat(asyncMethods)
                .as("Expected %s to declare exactly one async method", method)
                .hasSize(1);

        return this;
    }

    /**
     * Asserts that all public methods of
     * the specified bean are transactional.
     *
     * @param beanType the expected bean type
     *
     * @return the fluent assertion
     */
    public SpringWiringTestAssertion hasAllTransactionalMethods(Class<?> beanType) {
        var bean = context.getBean(beanType);
        var targetClass = AopUtils.getTargetClass(bean);

        List<String> nonTxMethods = findNonTransactionalMethods(targetClass);

        var className = targetClass.getSimpleName();
        var message = "Expected all public methods of %s to be transactional, but these were not: %s";

        assertThat(nonTxMethods)
                .as(message.formatted(className, nonTxMethods))
                .isEmpty();

        return this;
    }

    /**
     * Asserts that the specified method of
     * the given bean is transactional.
     *
     * @param beanType the expected bean type
     * @param methodName the expected transactional method
     *
     * @return the fluent assertion
     */
    public SpringWiringTestAssertion hasTransactionalMethod(Class<?> beanType, String methodName) {
        var bean = context.getBean(beanType);
        var targetClass = AopUtils.getTargetClass(bean);

        var method = getMethodOrThrow(targetClass, methodName);
        var attr = transactionAttributes().getTransactionAttribute(method, targetClass);

        var clazzName = targetClass.getSimpleName();
        var message = "Expected method %s.%s to be transactional";
        assertThat(attr).as(message.formatted(clazzName, methodName)).isNotNull();

        return this;
    }

    static SpringWiringTestAssertion assertThatContext(TestApplicationContext context) {
        return new SpringWiringTestAssertion(context);
    }

    TestApplicationContext getContext() {
        return context;
    }

    private ScheduledAnnotationBeanPostProcessor postProcessor() {
        return context.getBean(ScheduledAnnotationBeanPostProcessor.class);
    }

    private static boolean isScheduledMethod(
            String taskDescription,
            Class<?> beanType,
            String methodName
    ) {
        return taskDescription.contains(beanType.getSimpleName())
                && taskDescription.contains(methodName);
    }

    private List<Method> findEventListenerMethods(Class<?> type, Class<?> eventType) {
        return Arrays.stream(type.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(EventListener.class))
                .filter(method -> listensFor(method, eventType))
                .toList();
    }

    private List<Method> findAsyncMethods(Class<?> type, Class<?> argumentType) {
        return Arrays.stream(type.getDeclaredMethods())
                .filter(method -> Arrays.equals(method.getParameterTypes(), new Class<?>[]{argumentType}))
                .filter(method -> method.isAnnotationPresent(Async.class))
                .toList();
    }

    private boolean listensFor(Method method, Class<?> eventType) {
        return Arrays.equals(method.getParameterTypes(), new Class<?>[]{eventType})
                || declaresEventType(method.getAnnotation(EventListener.class), eventType);
    }

    private boolean declaresEventType(EventListener annotation, Class<?> eventType) {
        return Arrays.asList(annotation.value()).contains(eventType)
                || Arrays.asList(annotation.classes()).contains(eventType);
    }

    private List<String> findNonTransactionalMethods(Class<?> targetClass) {
        return Arrays.stream(targetClass.getDeclaredMethods())
                .filter(method -> Modifier.isPublic(method.getModifiers()))
                .filter(method -> !method.isSynthetic())
                .filter(method ->
                        transactionAttributes().getTransactionAttribute(method, targetClass) == null
                )
                .map(Method::getName)
                .toList();
    }

    private Method getMethodOrThrow(Class<?> type, String methodName) {
        // prefer methods declared on the class itself
        var declared = Arrays.stream(type.getDeclaredMethods())
                .filter(m -> m.getName().equals(methodName))
                .findFirst();

        // fallback to inherited/public methods
        return declared.orElseGet(() -> Arrays.stream(type.getMethods())
                .filter(m -> m.getName().equals(methodName))
                .filter(m -> m.getDeclaringClass() != Object.class)
                .findFirst()
                .orElseThrow(() -> new AssertionError(
                        "Method not found: " + type.getSimpleName() + "." + methodName
                )));
    }

    private TransactionAttributeSource transactionAttributes() {
        return context.getBeanFactory().getBean(TransactionAttributeSource.class);
    }
}
