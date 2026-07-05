package hr.ht.rnd.wifiadmin.infra.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
class OpenApiConfiguration {

    @Bean
    OpenAPI openApi() {
        var scheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT");

        var component = new Components().addSecuritySchemes(
                "bearerAuth",
                scheme
        );
        return new OpenAPI().components(component);
    }
}
