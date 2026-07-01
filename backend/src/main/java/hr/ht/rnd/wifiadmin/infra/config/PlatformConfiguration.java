package hr.ht.rnd.wifiadmin.infra.config;

import hr.ht.rnd.wifiadmin.infra.platform.PlatformProperties;
import hr.ht.rnd.wifiadmin.infra.platform.wsdl.WifiPlatformPortType;
import hr.ht.rnd.wifiadmin.infra.platform.wsdl.WifiPlatformService;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.jaxb.JAXBDataBinding;

import jakarta.xml.ws.BindingProvider;

import java.util.Map;

@Configuration
@EnableConfigurationProperties(PlatformProperties.class)
public class PlatformConfiguration {

    private static final String PLATFORM_NAMESPACE = "http://wifi-admin.local/platform/v1";
    private static final String HTTP_VERSION_PROPERTY = "org.apache.cxf.transport.http.forceVersion";

    /**
     * Creates and configures the SOAP platform client.
     * <p>
     * <strong>Implementation Note:</strong>
     * The client is configured to use HTTP/1.1 for compatibility
     * with platforms that do not support HTTP/2 transport negotiation.
     * A preferred JAXB namespace mapping is also configured to emit
     * explicit namespace prefixes required by the target platform contract.
     */
    @Bean
    WifiPlatformPortType platformPort(PlatformProperties properties) {
        var service = new WifiPlatformService();
        var port = service.getWifiPlatformPort();
        var client = ClientProxy.getClient(port);

        var dataBinding = (JAXBDataBinding) client.getEndpoint().getService().getDataBinding();
        dataBinding.setNamespaceMap(Map.of(PLATFORM_NAMESPACE, "tns"));

        var requestContext = ((BindingProvider) port).getRequestContext();
        requestContext.put(HTTP_VERSION_PROPERTY, "1.1");

        requestContext.put(
                BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
                properties.soapEndpoint()
        );
        return port;
    }
}
