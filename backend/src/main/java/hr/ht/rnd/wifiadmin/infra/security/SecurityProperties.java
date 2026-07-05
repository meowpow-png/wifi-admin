package hr.ht.rnd.wifiadmin.infra.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;

@Validated
@ConfigurationProperties(prefix = "security")
public record SecurityProperties(
        @NotBlank
        String aesKey
) {}
