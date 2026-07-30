package hr.ht.rnd.wifiadmin.infra.security;

import hr.ht.rnd.wifiadmin.application.outbound.PasswordEncryptor;
import hr.ht.rnd.wifiadmin.test.autoconfigure.WiringIntegrationTest;
import hr.ht.rnd.wifiadmin.test.support.TestApplicationContextRunner;

import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.context.properties.bind.validation.BindValidationException;
import org.springframework.boot.security.autoconfigure.SecurityAutoConfiguration;
import org.springframework.boot.security.autoconfigure.web.servlet.ServletWebSecurityAutoConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.context.runner.WebApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfigurationSource;

import jakarta.servlet.http.HttpServletRequest;

import tools.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.util.function.Consumer;
import javax.crypto.SecretKey;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@WiringIntegrationTest
class SecurityConfigurationTest {

    private static final SecurityProperties SECURITY_PROPERTIES =
            TestSecurityProperties.builder().build();

    private final WebApplicationContextRunner runner = new WebApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(
                    SecurityAutoConfiguration.class,
                    ServletWebSecurityAutoConfiguration.class
            ))
            .withUserConfiguration(SecurityConfiguration.class, SecurityTestConfiguration.class)
            .withPropertyValues(TestSecurityProperties.propertyValues(SECURITY_PROPERTIES));

    @Nested
    @DisplayName("beans")
    class BeansTests {

        @Test
        @DisplayName("Registers security infrastructure beans when properties are valid")
        void should_RegisterSecurityInfrastructureBeans_when_PropertiesAreValid() {
            TestApplicationContextRunner.from(runner)
                    .hasBean("securityFilterChain")
                    .hasBean(CorsConfigurationSource.class)
                    .hasBean("authenticationManager")
                    .hasBean("passwordEncoder")
                    .hasBean(PasswordEncryptor.class)
                    .hasBean(SecretKey.class)
                    .hasBean(JwtAccessTokenVerifier.class)
                    .doesNotFail();
        }
    }

    @Nested
    @DisplayName("properties")
    class PropertiesTests {

        @Test
        @DisplayName("Binds configured security properties")
        void should_BindConfiguredSecurityProperties_when_ContextStarts() {
            Consumer<SecurityProperties> assertion = properties -> {
                assertThat(properties.aesKey()).isEqualTo(SECURITY_PROPERTIES.aesKey());
                assertThat(properties.jwtSecret()).isEqualTo(SECURITY_PROPERTIES.jwtSecret());
                assertThat(properties.allowedOrigins()).containsExactlyElementsOf(
                        SECURITY_PROPERTIES.allowedOrigins()
                );
                assertThat(properties.publicEndpoints()).containsExactlyElementsOf(
                        SECURITY_PROPERTIES.publicEndpoints()
                );
            };
            TestApplicationContextRunner.from(runner)
                    .withBean(SecurityProperties.class, assertion)
                    .doesNotFail();
        }

        @Test
        @DisplayName("Fails when security properties violate validation constraints")
        void should_Fail_when_SecurityPropertiesAreInvalid() {
            TestApplicationContextRunner.from(runner)
                    .withPropertyValues("security.jwt-secret=")
                    .failsWithException(BindValidationException.class);
        }
    }

    @Nested
    @DisplayName("jwtSigningKey")
    class JwtSigningKeyMethodTests {

        @Test
        @DisplayName("Fails when JWT secret is not Base64 encoded")
        void should_Fail_when_JwtSecretIsNotBase64Encoded() {
            TestApplicationContextRunner.from(runner)
                    .withPropertyValues("security.jwt-secret=not-base64")
                    .failsWithException(IllegalStateException.class);
        }

        @Test
        @DisplayName("Fails when JWT secret is too short")
        void should_Fail_when_JwtSecretIsTooShort() {
            TestApplicationContextRunner.from(runner)
                    .withPropertyValues("security.jwt-secret=dG9vLXNob3J0")
                    .failsWithException(IllegalStateException.class);
        }
    }

    @Nested
    @DisplayName("corsConfigurationSource")
    class CorsConfigurationSourceMethodTests {

        @Test
        @DisplayName("Uses allowed origins from security properties")
        void should_UseAllowedOriginsFromSecurityProperties_when_CorsConfigurationIsCreated() {
            Consumer<CorsConfigurationSource> assertion = source -> {
                var request = corsRequest();
                var configuration = source.getCorsConfiguration(request);

                assertThat(configuration).isNotNull();
                assertThat(configuration.getAllowedOrigins()).containsExactlyElementsOf(
                        SECURITY_PROPERTIES.allowedOrigins()
                );
            };
            TestApplicationContextRunner.from(runner)
                    .withBean(CorsConfigurationSource.class, assertion)
                    .doesNotFail();
        }
    }

    @Nested
    @DisplayName("passwordEncryptor")
    class PasswordEncryptorMethodTests {

        @Test
        @DisplayName("Fails when AES key is not Base64 encoded")
        void should_Fail_when_AesKeyIsNotBase64Encoded() {
            TestApplicationContextRunner.from(runner)
                    .withPropertyValues("security.aes-key=not-base64")
                    .failsWithException(IllegalStateException.class);
        }

        @Test
        @DisplayName("Fails when AES key is not 256 bits")
        void should_Fail_when_AesKeyIsNot256Bits() {
            TestApplicationContextRunner.from(runner)
                    .withPropertyValues("security.aes-key=dG9vLXNob3J0")
                    .failsWithException(IllegalStateException.class);
        }
    }

    private static HttpServletRequest corsRequest() {
        var request = new org.springframework.mock.web.MockHttpServletRequest();
        request.setRequestURI("/admin/configurations");
        return request;
    }

    @TestConfiguration(proxyBeanMethods = false)
    static class SecurityTestConfiguration {

        @Bean
        Clock clock() {
            return Clock.systemUTC();
        }

        @Bean
        JwtAuthenticationFilter jwtAuthenticationFilter() {
            return mock(JwtAuthenticationFilter.class);
        }

        @Bean
        ApiAuthenticationEntryPoint apiAuthenticationEntryPoint() {
            return new ApiAuthenticationEntryPoint(new ObjectMapper());
        }
    }
}
