package hr.ht.rnd.wifiadmin.infra.transport.soap;

import hr.ht.rnd.wifiadmin.application.outbound.CpeNotFoundException;
import hr.ht.rnd.wifiadmin.application.outbound.PlatformClient;
import hr.ht.rnd.wifiadmin.application.outbound.PlatformConnectionException;
import hr.ht.rnd.wifiadmin.application.outbound.PlatformResponseException;
import hr.ht.rnd.wifiadmin.domain.WifiConfiguration;
import hr.ht.rnd.wifiadmin.infra.transport.soap.fault.SoapFaultCode;
import hr.ht.rnd.wifiadmin.infra.transport.soap.fault.SoapFaultDecoder;
import hr.ht.rnd.wifiadmin.infra.transport.soap.fault.SoapFaultException;
import hr.ht.rnd.wifiadmin.infra.transport.soap.wsdl.*;

import org.springframework.stereotype.Component;

import jakarta.xml.ws.WebServiceException;

import java.net.ConnectException;
import java.util.Objects;
import java.util.function.Supplier;

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
        Objects.requireNonNull(cpeId, "cpeId must not be null");

        var request = new GetCpeIdRequest();
        request.setCpeId(cpeId);

        GetCpeIdResponse response;
        try {
            response = callSoapPlatform(() -> platformPort.getCpeID(request));
        }
        catch (SoapFaultException e) {
            if (e.code() == SoapFaultCode.NOT_FOUND) {
                throw new CpeNotFoundException(cpeId, e);
            }
            throw e;
        }
        try {
            return SoapPlatformMapper.toDomain(response.getConfiguration());
        }
        catch (NullPointerException | IllegalArgumentException e) {
            throw new PlatformResponseException(e);
        }
    }

    @Override
    public WifiConfiguration updateConfiguration(WifiConfiguration configuration) {
        var platformConfiguration = SoapPlatformMapper.toPlatform(configuration);

        var request = new UpdateCpeIdRequest();
        request.setConfiguration(platformConfiguration);

        UpdateCpeIdResponse response;
        try {
            response = callSoapPlatform(() -> platformPort.updateCpeId(request));
        }
        catch (SoapFaultException e) {
            if (e.code() == SoapFaultCode.NOT_FOUND) {
                throw new CpeNotFoundException(configuration.cpeId(), e);
            }
            throw e;
        }
        try {
            return SoapPlatformMapper.toDomain(response.getConfiguration());
        }
        catch (NullPointerException | IllegalArgumentException e) {
            throw new PlatformResponseException(e);
        }
    }

    private static <T> T callSoapPlatform(Supplier<T> call) {
        try {
            return call.get();
        }
        catch (WebServiceException e) {
            if (hasCause(e, ConnectException.class)) {
                var message = "Platform could not be reached";
                throw new PlatformConnectionException(message, e);
            }
            throw SoapFaultDecoder.decode(e);
        }
    }

    private static boolean hasCause(Throwable throwable, Class<? extends Throwable> type) {
        while (throwable != null) {
            if (type.isInstance(throwable)) {
                return true;
            }
            throwable = throwable.getCause();
        }
        return false;
    }
}
