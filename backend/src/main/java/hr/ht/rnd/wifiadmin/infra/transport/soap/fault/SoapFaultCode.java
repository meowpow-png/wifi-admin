package hr.ht.rnd.wifiadmin.infra.transport.soap.fault;

/**
 * SOAP fault codes returned by the platform.
 */
public enum SoapFaultCode {

    NOT_FOUND("tns:NotFound"),
    CLIENT("soap:Client");

    private final String value;

    SoapFaultCode(String value) {
        this.value = value;
    }

    String value() {
        return value;
    }
}
