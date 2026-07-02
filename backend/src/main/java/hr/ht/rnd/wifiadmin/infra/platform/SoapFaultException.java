package hr.ht.rnd.wifiadmin.infra.platform;

import hr.ht.rnd.wifiadmin.application.outbound.PlatformTransportException;

/**
 * SOAP fault returned by the platform.
 */
final class SoapFaultException extends PlatformTransportException {

    private final SoapFaultCode code;

    /**
     * Creates a SOAP fault exception.
     *
     * @param code the SOAP fault code
     * @param cause the underlying cause
     *
     * @throws NullPointerException if {@code code} or {@code message} is {@code null}
     */
    SoapFaultException(SoapFaultCode code, Throwable cause) {
        super(cause);
        this.code = code;
    }

    SoapFaultCode code() {
        return code;
    }
}
