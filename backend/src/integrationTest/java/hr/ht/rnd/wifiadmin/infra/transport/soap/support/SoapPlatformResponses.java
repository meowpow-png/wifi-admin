package hr.ht.rnd.wifiadmin.infra.transport.soap.support;

import hr.ht.rnd.wifiadmin.domain.wifi.WifiConfiguration;
import hr.ht.rnd.wifiadmin.domain.wifi.WifiPassword;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.SocketPolicy;

/**
 * SOAP response fixtures for the mock platform.
 */
public final class SoapPlatformResponses {

    private static final String SOAP_CONTENT_TYPE = "text/xml; charset=utf-8";

    private SoapPlatformResponses() {}

    public static MockResponse retrieved(WifiConfiguration configuration) {
        return soapOk(envelope("""
                <tns:GetCpeIdResponse>
                    %s
                </tns:GetCpeIdResponse>
                """.formatted(configuration(configuration))));
    }

    public static MockResponse retrievedWithLeadingWhitespace(WifiConfiguration configuration) {
        return soapOk("\n\t " + envelope("""
                <tns:GetCpeIdResponse>
                    %s
                </tns:GetCpeIdResponse>
                """.formatted(configuration(configuration))));
    }

    public static MockResponse updated(WifiConfiguration configuration) {
        return soapOk(envelope("""
                <tns:UpdateCpeIdResponse>
                    %s
                </tns:UpdateCpeIdResponse>
                """.formatted(configuration(configuration))));
    }

    public static MockResponse notFoundFault() {
        return soapFault("tns:NotFound", "tns:NotFound CPE was not found");
    }

    public static MockResponse clientFault() {
        return soapFault("soap:Client", "Platform rejected the request");
    }

    public static MockResponse invalidRetrieveResponse() {
        return soapOk(envelope("""
                <tns:GetCpeIdResponse>
                </tns:GetCpeIdResponse>
                """));
    }

    public static MockResponse invalidUpdateResponse() {
        return soapOk(envelope("""
                <tns:UpdateCpeIdResponse>
                </tns:UpdateCpeIdResponse>
                """));
    }

    public static MockResponse disconnectAtStart() {
        return new MockResponse().setSocketPolicy(SocketPolicy.DISCONNECT_AT_START);
    }

    private static MockResponse soapOk(String body) {
        return soapResponse(200, body);
    }

    private static MockResponse soapFault(String faultCode, String faultString) {
        return soapResponse(500, envelope("""
                <soap:Fault>
                    <faultcode>%s</faultcode>
                    <faultstring>%s</faultstring>
                </soap:Fault>
                """.formatted(faultCode, escape(faultString)))
        );
    }

    private static MockResponse soapResponse(int status, String body) {
        return new MockResponse()
                .setResponseCode(status)
                .setHeader("Content-Type", SOAP_CONTENT_TYPE)
                .setBody(body);
    }

    private static String envelope(String body) {
        return """
                <?xml version="1.0" encoding="UTF-8"?>
                <soap:Envelope
                    xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"
                    xmlns:tns="http://wifi-admin.local/platform/v1">
                    <soap:Body>
                        %s
                    </soap:Body>
                </soap:Envelope>
                """.formatted(body);
    }

    private static String configuration(WifiConfiguration configuration) {
        var password = configuration.password();
        return """
                <tns:configuration>
                    <tns:cpeId>%s</tns:cpeId>
                    <tns:wifiBand>%s</tns:wifiBand>
                    <tns:ssid>%s</tns:ssid>
                    <tns:encryptionType>%s</tns:encryptionType>
                    %s
                </tns:configuration>
                """.formatted(
                escape(configuration.cpeId()),
                configuration.wifiBand(),
                escape(configuration.ssid()),
                configuration.encryptionType(),
                password(password)
        );
    }

    private static String password(WifiPassword password) {
        if (password == null) {
            return "";
        }
        return "<tns:password>%s</tns:password>".formatted(escape(password.value()));
    }

    private static String escape(String value) {
        return value
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;");
    }
}
