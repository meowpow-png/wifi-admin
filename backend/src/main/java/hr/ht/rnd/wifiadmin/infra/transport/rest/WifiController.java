package hr.ht.rnd.wifiadmin.infra.transport.rest;

import hr.ht.rnd.wifiadmin.application.inbound.PlatformAdministration;
import hr.ht.rnd.wifiadmin.common.LogContext;
import hr.ht.rnd.wifiadmin.domain.wifi.WifiConfiguration;
import hr.ht.rnd.wifiadmin.infra.transport.rest.dto.WifiConfigurationRequest;
import hr.ht.rnd.wifiadmin.infra.transport.rest.dto.WifiConfigurationResponse;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.util.Objects;

/**
 * REST controller exposing Wi-Fi management endpoints.
 */
@RestController
@SecurityRequirement(name = "bearerAuth")
public final class WifiController {

    private final PlatformAdministration admin;

    WifiController(PlatformAdministration admin) {
        Objects.requireNonNull(admin, "admin must not be null");
        this.admin = admin;
    }

    /**
     * Retrieves the Wi-Fi configuration of a CPE device.
     *
     * @param cpeId the CPE device identifier
     *
     * @return status {@code 200 (OK)}
     * @throws NullPointerException if {@code cpeId} is {@code null}
     */
    @Validated
    @GetMapping("/wifi-parameter/{cpeId}")
    WifiConfigurationResponse retrieveConfiguration(
            @NotBlank @PathVariable String cpeId
    ) {
        try (var ignored = LogContext.open()) {
            var configuration = admin.retrieveConfiguration(cpeId);
            return WifiConfigurationMapper.toResponse(configuration);
        }
    }

    /**
     * Updates the Wi-Fi configuration of a CPE device.
     *
     * @param request the requested Wi-Fi configuration
     *
     * @return status {@code 200 (OK)}
     * @throws NullPointerException if {@code request} is {@code null}
     * @throws InvalidRequestException if the request contains invalid data
     */
    @PutMapping("/wifi-parameter")
    WifiConfigurationResponse updateConfiguration(
            @Valid @RequestBody WifiConfigurationRequest request
    ) {
        try (var ignored = LogContext.open()) {
            WifiConfiguration configuration;
            try {
                configuration = WifiConfigurationMapper.toDomain(request);
            }
            catch (NullPointerException | IllegalArgumentException e) {
                throw new InvalidRequestException(e);
            }
            return WifiConfigurationMapper.toResponse(
                    admin.updateConfiguration(configuration)
            );
        }
    }
}
