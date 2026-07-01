package hr.ht.rnd.wifiadmin.infra.platform;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.URL;

@Validated
@ConfigurationProperties("platform")
public record PlatformProperties(
        @URL
        @NotBlank
        String soapEndpoint
) {}
