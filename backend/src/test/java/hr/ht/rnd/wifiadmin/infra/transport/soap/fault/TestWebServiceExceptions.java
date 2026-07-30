package hr.ht.rnd.wifiadmin.infra.transport.soap.fault;

import jakarta.xml.ws.WebServiceException;

final class TestWebServiceExceptions {

    private TestWebServiceExceptions() {}

    static WebServiceException notFound() {
        return new WebServiceException("Fault code: tns:NotFound");
    }

    static WebServiceException client() {
        return new WebServiceException("Fault code: soap:Client");
    }

    static WebServiceException unknown() {
        return new WebServiceException("Fault code: tns:Unknown");
    }

    static WebServiceException nullMessage() {
        return new WebServiceException((String) null);
    }
}
