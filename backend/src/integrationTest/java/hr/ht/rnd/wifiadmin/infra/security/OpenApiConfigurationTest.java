package hr.ht.rnd.wifiadmin.infra.security;

import hr.ht.rnd.wifiadmin.test.autoconfigure.WiringIntegrationTest;
import hr.ht.rnd.wifiadmin.test.support.TestApplicationContextRunner;

import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityScheme;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;

@WiringIntegrationTest
class OpenApiConfigurationTest {

    private final ApplicationContextRunner runner = new ApplicationContextRunner()
            .withUserConfiguration(OpenApiConfiguration.class);

    @Nested
    @DisplayName("beans")
    class BeansTests {

        @Test
        @DisplayName("Registers OpenAPI bean")
        void should_RegisterOpenApiBean_when_ContextStarts() {
            TestApplicationContextRunner.from(runner)
                    .hasBean(OpenAPI.class)
                    .doesNotFail();
        }
    }

    @Nested
    @DisplayName("openApi")
    class OpenApiMethodTests {

        @Test
        @DisplayName("Configures bearer JWT security scheme")
        void should_ConfigureBearerJwtSecurityScheme_when_OpenApiBeanIsCreated() {
            Consumer<OpenAPI> assertion = openApi -> {
                var securitySchemes = openApi.getComponents().getSecuritySchemes();
                var scheme = securitySchemes.get("bearerAuth");

                assertThat(scheme).isNotNull();
                assertThat(scheme.getType()).isEqualTo(SecurityScheme.Type.HTTP);
                assertThat(scheme.getScheme()).isEqualTo("bearer");
                assertThat(scheme.getBearerFormat()).isEqualTo("JWT");
            };

            TestApplicationContextRunner.from(runner)
                    .withBean(OpenAPI.class, assertion)
                    .doesNotFail();
        }
    }
}
