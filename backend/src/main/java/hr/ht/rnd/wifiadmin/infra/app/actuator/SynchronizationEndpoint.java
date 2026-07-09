package hr.ht.rnd.wifiadmin.infra.app.actuator;

import hr.ht.rnd.wifiadmin.infra.transport.soap.sync.PlatformSynchronizer;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation;
import org.springframework.stereotype.Component;

@Component
@Endpoint(id = "sync")
class SynchronizationEndpoint {

    private final PlatformSynchronizer synchronizer;

    SynchronizationEndpoint(PlatformSynchronizer synchronizer) {
        this.synchronizer = synchronizer;
    }

    @WriteOperation
    void synchronize() {
        synchronizer.synchronize();
    }
}
