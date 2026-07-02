package hr.ht.rnd.wifiadmin.infra.rest;

import hr.ht.rnd.wifiadmin.application.inbound.WifiAdministration;

import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.Objects;

/**
 * REST controller exposing Wi-Fi management endpoints.
 */
@RestController
public final class WifiController {

    private final WifiAdministration admin;

    WifiController(WifiAdministration admin) {
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
    @GetMapping("/wifi-parameter/{cpeId}")
    WifiConfigurationDto retrieveConfiguration(@PathVariable String cpeId) {
        var configuration = admin.retrieveConfiguration(cpeId);

        return WifiConfigurationMapper.toDto(configuration);
    }

    /**
     * Updates the Wi-Fi configuration of a CPE device.
     *
     * @param request the requested Wi-Fi configuration
     *
     * @return status {@code 200 (OK)}
     * @throws NullPointerException if {@code request} is {@code null}
     */
    @PutMapping("/wifi-parameter")
    WifiConfigurationDto updateConfiguration(@Valid @RequestBody WifiConfigurationDto request) {
        var configuration = WifiConfigurationMapper.toDomain(request);

        return WifiConfigurationMapper.toDto(
                admin.updateConfiguration(configuration)
        );
    }
}
