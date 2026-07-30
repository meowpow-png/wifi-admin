package hr.ht.rnd.wifiadmin.infra.transport.soap.fault;

import hr.ht.rnd.wifiadmin.application.exception.PlatformTransportException;

/**
 * SOAP fault returned by the platform.
 */
public final class SoapFaultException extends PlatformTransportException {

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
        super(cause.getMessage(), cause);
        this.code = code;
    }

    public SoapFaultCode code() {
        return code;
    }
}
