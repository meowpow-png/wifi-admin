package hr.ht.rnd.wifiadmin.infra.transport.soap.fault;

import jakarta.xml.ws.WebServiceException;

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
        var message = ex.getMessage();

        for (var faultCode : SoapFaultCode.values()) {
            if (message.contains(faultCode.value())) {
                return new SoapFaultException(faultCode, ex);
            }
        }
        var msg = "Exception does not contain a supported SOAP fault (message: %s)";
        throw new IllegalArgumentException(msg.formatted(message));
    }
}
