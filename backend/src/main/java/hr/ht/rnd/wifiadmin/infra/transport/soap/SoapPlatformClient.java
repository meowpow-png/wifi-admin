package hr.ht.rnd.wifiadmin.infra.transport.soap;

import hr.ht.rnd.wifiadmin.application.exception.CpeNotFoundException;
import hr.ht.rnd.wifiadmin.application.exception.PlatformResponseException;
import hr.ht.rnd.wifiadmin.application.outbound.PlatformClient;
import hr.ht.rnd.wifiadmin.domain.wifi.WifiConfiguration;
import hr.ht.rnd.wifiadmin.infra.transport.soap.fault.SoapFaultCode;
import hr.ht.rnd.wifiadmin.infra.transport.soap.fault.SoapFaultDecoder;
import hr.ht.rnd.wifiadmin.infra.transport.soap.fault.SoapFaultException;
import hr.ht.rnd.wifiadmin.infra.transport.soap.wsdl.*;

import jakarta.xml.ws.WebServiceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.function.Supplier;

import static hr.ht.rnd.wifiadmin.common.StructuredLog.*;

/**
 * SOAP-based implementation of {@link PlatformClient}.
 */
public final class SoapPlatformClient implements PlatformClient {

    private static final Logger log = LoggerFactory.getLogger(SoapPlatformClient.class);

    private final WifiPlatformPortType platformPort;

    public SoapPlatformClient(WifiPlatformPortType platformPort) {
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
                debug(log).withEvent(Event.PLATFORM_CPE_REPORTED_NOT_FOUND)
                        .withField(Field.CPE_ID, cpeId)
                        .log();

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
            if (SoapFaultDecoder.isSoapFault(e)) {
                throw SoapFaultDecoder.decode(e);
            }
            throw PlatformExceptionMapper.toTransportException(e)
                    .orElseThrow(() -> e);
        }
    }
}
