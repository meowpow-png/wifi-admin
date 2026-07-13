package hr.ht.rnd.wifiadmin.infra.transport.soap;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import org.hibernate.validator.constraints.URL;

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
        LocalTime syncSchedule
) {}
