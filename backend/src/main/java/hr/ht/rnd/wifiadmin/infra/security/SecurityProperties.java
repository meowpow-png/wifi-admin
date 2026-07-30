package hr.ht.rnd.wifiadmin.infra.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.time.DurationMin;

import java.time.Duration;
import java.util.List;

@Validated
@ConfigurationProperties(prefix = "security")
public record SecurityProperties(
        @NotBlank
        String aesKey,

        @NotBlank
        String jwtSecret,

        @DurationMin(seconds = 1)
        Duration jwtExpiration,

        @NotEmpty
        List<@NotBlank String> allowedOrigins,

        @NotEmpty
        List<@NotBlank String> publicEndpoints
) {}
