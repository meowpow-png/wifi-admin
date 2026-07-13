package hr.ht.rnd.wifiadmin.infra.rest;

import hr.ht.rnd.wifiadmin.domain.WifiBand;
import hr.ht.rnd.wifiadmin.domain.WifiEncryptionType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record WifiConfigurationDto(
        @NotBlank
        String cpeId,

        @NotNull
        WifiBand wifiBand,

        @NotBlank
        String ssid,

        WifiEncryptionType encryptionType,
        String password
) {}
