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
    Executor asyncExecutor() {
        return Executors.newVirtualThreadPerTaskExecutor();
    }
}
