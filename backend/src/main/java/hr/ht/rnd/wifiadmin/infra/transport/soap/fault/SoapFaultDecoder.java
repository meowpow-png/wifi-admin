package hr.ht.rnd.wifiadmin.infra.transport.soap.fault;

import jakarta.xml.ws.WebServiceException;

import java.util.Optional;

/**
 * Decodes SOAP faults from web service exceptions.
 */
public final class SoapFaultDecoder {

    private SoapFaultDecoder() {}

    /**
     * Decodes a SOAP fault from a web service exception.
     *
     * @param ex the web service exception
     *
     * @return the decoded SOAP fault
     * @throws IllegalArgumentException if the exception does not contain a SOAP fault
     */
    public static SoapFaultException decode(WebServiceException ex) {
        var code = findFaultCode(ex).orElseThrow(() -> {
            var message = "Exception does not contain a recognized SOAP fault";
            return new IllegalArgumentException(message, ex);
        });
        return new SoapFaultException(code, ex);
    }

    /**
     * Returns whether the exception contains a supported SOAP fault.
     *
     * @param ex the web service exception
     */
    public static boolean isSoapFault(Exception ex) {
        return findFaultCode(ex).isPresent();
    }

    private static Optional<SoapFaultCode> findFaultCode(Exception ex) {
        var message = ex.getMessage();
        if (message == null) {
            return Optional.empty();
        }
        for (var code : SoapFaultCode.values()) {
            if (message.contains(code.value())) {
                return Optional.of(code);
            }
        }
        return Optional.empty();
    }
}
