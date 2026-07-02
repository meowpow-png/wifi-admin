package hr.ht.rnd.wifiadmin.infra.platform;

import hr.ht.rnd.wifiadmin.application.outbound.PlatformClient;
import hr.ht.rnd.wifiadmin.domain.WifiConfiguration;
import hr.ht.rnd.wifiadmin.infra.platform.wsdl.GetCpeIdRequest;
import hr.ht.rnd.wifiadmin.infra.platform.wsdl.UpdateCpeIdRequest;
import hr.ht.rnd.wifiadmin.infra.platform.wsdl.WifiPlatformPortType;

import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * SOAP-based implementation of {@link PlatformClient}.
 */
@Component
final class SoapPlatformClient implements PlatformClient {

    private final WifiPlatformPortType platformPort;

    SoapPlatformClient(WifiPlatformPortType platformPort) {
        Objects.requireNonNull(platformPort, "platformPort must not be null");
        this.platformPort = platformPort;
    }

    @Override
    public WifiConfiguration retrieveConfiguration(String cpeId) {
        var request = new GetCpeIdRequest();

        request.setCpeId(cpeId);

        var response = platformPort.getCpeID(request);
        return PlatformMapper.toDomain(response.getConfiguration());
    }

    @Override
    public WifiConfiguration updateConfiguration(WifiConfiguration configuration) {
        var request = new UpdateCpeIdRequest();

        var configType = PlatformMapper.toPlatform(configuration);
        request.setConfiguration(configType);

        var response = platformPort.updateCpeId(request);
        return PlatformMapper.toDomain(response.getConfiguration());
    }
}
