package hr.ht.rnd.wifiadmin.infra.app.actuator;

import hr.ht.rnd.wifiadmin.infra.transport.soap.sync.PlatformSynchronizer;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Endpoint(id = "sync")
class SynchronizationEndpoint {

    private final PlatformSynchronizer synchronizer;

    SynchronizationEndpoint(PlatformSynchronizer synchronizer) {
        Objects.requireNonNull(synchronizer, "synchronizer must not be null");
        this.synchronizer = synchronizer;
    }

    @WriteOperation
    void synchronize() {
        synchronizer.synchronize();
    }
}
