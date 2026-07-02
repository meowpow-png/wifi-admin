package hr.ht.rnd.wifiadmin.infra.rest;

import hr.ht.rnd.wifiadmin.application.inbound.WifiAdministration;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import java.util.Objects;

@RestController
public final class WifiController {

    private final WifiAdministration admin;

    WifiController(WifiAdministration admin) {
        Objects.requireNonNull(admin, "admin must not be null");
        this.admin = admin;
    }

    @GetMapping("/wifi-parameter/{cpeId}")
    WifiConfigurationDto retrieveConfiguration(@PathVariable String cpeId) {
        var configuration = admin.retrieveConfiguration(cpeId);

        return WifiConfigurationMapper.toDto(configuration);
    }

    @PutMapping("/wifi-parameter")
    WifiConfigurationDto updateConfiguration(@Valid @RequestBody WifiConfigurationDto request) {
        var configuration = WifiConfigurationMapper.toDomain(request);

        return WifiConfigurationMapper.toDto(
                admin.updateConfiguration(configuration)
        );
    }
}
