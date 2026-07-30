package hr.ht.rnd.wifiadmin.test.config;

import hr.ht.rnd.wifiadmin.infra.app.TestClock;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration(proxyBeanMethods = false)
public class TestApplicationConfiguration {

    @Bean
    @Primary
    TestClock testClock() {
        return TestClock.create();
    }
}
