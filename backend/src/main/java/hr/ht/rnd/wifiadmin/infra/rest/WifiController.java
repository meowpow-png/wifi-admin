package hr.ht.rnd.wifiadmin.infra.rest;

import hr.ht.rnd.wifiadmin.application.WifiService;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import java.util.Objects;

@RestController
public final class WifiController {

    private final WifiService wifiService;

    WifiController(WifiService wifiService) {
        Objects.requireNonNull(wifiService, "wifiService must not be null");
        this.wifiService = wifiService;
    }

    @GetMapping("/wifi-parameter/{cpeId}")
    WifiConfigurationDto retrieveConfiguration(@PathVariable String cpeId) {
        var configuration = wifiService.retrieveConfiguration(cpeId);

        return WifiConfigurationMapper.toDto(configuration);
    }

    @PutMapping("/wifi-parameter")
    WifiConfigurationDto updateConfiguration(@Valid @RequestBody WifiConfigurationDto request) {
        var configuration = WifiConfigurationMapper.toDomain(request);

        return WifiConfigurationMapper.toDto(
                wifiService.updateConfiguration(configuration)
        );
    }
}
