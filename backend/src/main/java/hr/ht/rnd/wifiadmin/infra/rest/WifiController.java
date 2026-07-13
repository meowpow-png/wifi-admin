package hr.ht.rnd.wifiadmin.infra.rest;

import hr.ht.rnd.wifiadmin.application.inbound.WifiAdministration;
import hr.ht.rnd.wifiadmin.domain.WifiConfiguration;
import hr.ht.rnd.wifiadmin.infra.rest.dto.WifiConfigurationRequest;
import hr.ht.rnd.wifiadmin.infra.rest.dto.WifiConfigurationResponse;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

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
    @Validated
    @GetMapping("/wifi-parameter/{cpeId}")
    WifiConfigurationResponse retrieveConfiguration(
            @NotBlank @PathVariable String cpeId
    ) {
        var configuration = admin.retrieveConfiguration(cpeId);

        return WifiConfigurationMapper.toResponse(configuration);
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
