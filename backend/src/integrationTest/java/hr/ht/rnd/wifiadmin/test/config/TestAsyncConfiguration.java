package hr.ht.rnd.wifiadmin.test.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.util.concurrent.Executor;

@TestConfiguration(proxyBeanMethods = false)
public class TestAsyncConfiguration {

    @Primary
    @Bean("asyncExecutor")
    Executor testAsyncExecutor() {
        return Runnable::run;
    }
}
