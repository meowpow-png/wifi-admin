package hr.ht.rnd.wifiadmin.infra.transport.soap;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import org.hibernate.validator.constraints.URL;
import org.hibernate.validator.constraints.time.DurationMin;

import java.time.Duration;
import java.time.LocalTime;

@Validated
@ConfigurationProperties("platform")
public record PlatformProperties(
        @URL
        @NotBlank
        String soapEndpoint,

        @NotBlank
        String cpeIdFormat,

        @Positive
        int cpeIdCount,

        boolean syncOnStartup,

        @NotNull
        LocalTime syncSchedule,

        @NotNull
        @DurationMin(seconds = 1)
        Duration connectionTimeout,

        @NotNull
        @DurationMin(seconds = 1)
        Duration receiveTimeout,

        @Valid
        @NotNull
        Retry retry
) {
    public record Retry(
            @Positive
            int maxAttempts,

            @NotNull
            @DurationMin(millis = 100)
            Duration delay,

            @NotNull
            @DurationMin(millis = 100)
            Duration maxDelay,

            @DecimalMin(value = "1.0", inclusive = false)
            double delayMultiplier
    ) {}
}
