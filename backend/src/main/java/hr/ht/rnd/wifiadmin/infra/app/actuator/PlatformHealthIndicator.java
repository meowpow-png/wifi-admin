package hr.ht.rnd.wifiadmin.infra.app.actuator;

import hr.ht.rnd.wifiadmin.application.exception.CpeNotFoundException;
import hr.ht.rnd.wifiadmin.application.exception.PlatformException;
import hr.ht.rnd.wifiadmin.common.LogContext;
import hr.ht.rnd.wifiadmin.infra.transport.soap.PlatformProperties;
import hr.ht.rnd.wifiadmin.infra.transport.soap.SoapPlatformClient;

import org.springframework.boot.health.contributor.Health;
import org.springframework.boot.health.contributor.HealthIndicator;
import org.springframework.stereotype.Component;

@Component(ActuatorEndpoints.HEALTH)
class PlatformHealthIndicator implements HealthIndicator {

    private final SoapPlatformClient client;
    private final String healthCheckCpeId;

    PlatformHealthIndicator(SoapPlatformClient client, PlatformProperties properties) {
        this.client = client;
        this.healthCheckCpeId = properties.cpeIdFormat().formatted(1);
    }

    @Override
    public Health health() {
        try (var ignored = LogContext.open()) {
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
