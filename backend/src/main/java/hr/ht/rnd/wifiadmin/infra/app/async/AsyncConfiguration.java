package hr.ht.rnd.wifiadmin.infra.app.async;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@EnableAsync
@Configuration(proxyBeanMethods = false)
public class AsyncConfiguration {

    @Bean
    @SuppressWarnings("resource")
    Executor asyncExecutor() {
        var delegate = Executors.newVirtualThreadPerTaskExecutor();

        return command -> delegate.execute(
                new MdcTaskDecorator().decorate(command)
        );
    }
}
