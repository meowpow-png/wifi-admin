package hr.ht.rnd.wifiadmin.infra.app.async;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@EnableAsync
@Configuration(proxyBeanMethods = false)
public class AsyncConfiguration {

    private static final MdcTaskDecorator DECORATOR = new MdcTaskDecorator();

    @Bean
    @SuppressWarnings("resource")
    @ConditionalOnMissingBean(name = "asyncExecutor")
    Executor asyncExecutor() {
        var delegate = Executors.newVirtualThreadPerTaskExecutor();

        return command -> delegate.execute(
                DECORATOR.decorate(command)
        );
    }
}
