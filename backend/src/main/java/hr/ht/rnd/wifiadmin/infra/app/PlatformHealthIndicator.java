package hr.ht.rnd.wifiadmin.infra.app;

import hr.ht.rnd.wifiadmin.application.exception.CpeNotFoundException;
import hr.ht.rnd.wifiadmin.application.exception.PlatformException;
import hr.ht.rnd.wifiadmin.application.outbound.PlatformClient;
import hr.ht.rnd.wifiadmin.infra.transport.soap.PlatformProperties;

import org.springframework.boot.health.contributor.Health;
import org.springframework.boot.health.contributor.HealthIndicator;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
class PlatformHealthIndicator implements HealthIndicator {

    private final PlatformClient client;
    private final String healthCheckCpeId;

    PlatformHealthIndicator(PlatformClient client, PlatformProperties properties) {
        Objects.requireNonNull(client, "client must not be null");

        this.client = client;
        this.healthCheckCpeId = properties.cpeIdFormat().formatted(1);
    }

    @Override
    public Health health() {
        try {
            client.retrieveConfiguration(healthCheckCpeId);
            return Health.up().build();
        }
        catch (CpeNotFoundException e) {
            return Health.up().build();
        }
        catch (PlatformException e) {
            return Health.down(e).build();
        }
    }
}
