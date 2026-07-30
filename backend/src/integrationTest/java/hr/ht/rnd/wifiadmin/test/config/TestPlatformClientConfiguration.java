package hr.ht.rnd.wifiadmin.test.config;

import hr.ht.rnd.wifiadmin.test.support.TestPlatformClient;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration(proxyBeanMethods = false)
public class TestPlatformClientConfiguration {

    @Bean
    @Primary
    TestPlatformClient testPlatformClient() {
        return new TestPlatformClient();
    }
}
