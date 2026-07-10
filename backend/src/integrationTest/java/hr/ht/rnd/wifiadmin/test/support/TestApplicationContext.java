package hr.ht.rnd.wifiadmin.test.support;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.boot.test.context.assertj.ApplicationContextAssertProvider;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Map;

final class TestApplicationContext {

    private final ApplicationContextAssertProvider<? extends ConfigurableApplicationContext> context;

    private TestApplicationContext(
            ApplicationContextAssertProvider<? extends ConfigurableApplicationContext> context
    ) {
        this.context = context;
    }

    static TestApplicationContext from(
            ApplicationContextAssertProvider<? extends ConfigurableApplicationContext> context
    ) {
        return new TestApplicationContext(context);
    }

    Throwable startupFailure() {
        return context.getStartupFailure();
    }

    <T> T getBean(Class<T> type) {
        return context.getBean(type);
    }

    <T> T getBean(String name, Class<T> type) {
        return context.getBean(name, type);
    }

    <T> Map<String, T> getBeansOfType(Class<T> type) {
        return context.getBeansOfType(type);
    }

    BeanFactory getBeanFactory() {
        return context.getSourceApplicationContext().getBeanFactory();
    }

    boolean containsBean(String name) {
        return context.containsBean(name);
    }
}
