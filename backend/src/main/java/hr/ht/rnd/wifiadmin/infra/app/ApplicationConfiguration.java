package hr.ht.rnd.wifiadmin.infra.app;

import org.springframework.boot.logging.LoggingSystem;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration(proxyBeanMethods = false)
public class ApplicationConfiguration {

    @Bean
    Clock clock() {
        return Clock.systemDefaultZone();
    }

    @Bean
    LoggingSystem loggingSystem() {
        return LoggingSystem.get(getClass().getClassLoader());
    }
}
