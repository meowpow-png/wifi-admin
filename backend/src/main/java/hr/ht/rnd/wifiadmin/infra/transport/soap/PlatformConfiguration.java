package hr.ht.rnd.wifiadmin.infra.transport.soap;

import hr.ht.rnd.wifiadmin.infra.transport.soap.cxf.CxfFaultLoggingPolicy;
import hr.ht.rnd.wifiadmin.infra.transport.soap.fault.SoapCxfFaultLoggingPolicy;
import hr.ht.rnd.wifiadmin.infra.transport.soap.logging.SoapRequestLoggingInterceptor;
import hr.ht.rnd.wifiadmin.infra.transport.soap.logging.SoapResponseLoggingInterceptor;
import hr.ht.rnd.wifiadmin.infra.transport.soap.wsdl.WifiPlatformPortType;
import hr.ht.rnd.wifiadmin.infra.transport.soap.wsdl.WifiPlatformService;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.jaxb.JAXBDataBinding;

import jakarta.xml.ws.BindingProvider;

import java.util.Map;

@Configuration
@EnableScheduling
@EnableConfigurationProperties(PlatformProperties.class)
public class PlatformConfiguration {

    private static final String PLATFORM_NAMESPACE = "http://wifi-admin.local/platform/v1";
    private static final String HTTP_VERSION_PROPERTY = "org.apache.cxf.transport.http.forceVersion";

    /**
     * Creates and configures SOAP platform client.
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

        var inInterceptors = client.getInInterceptors();
        var outInterceptors = client.getOutInterceptors();

        inInterceptors.add(new XmlNormalizingInterceptor());
        inInterceptors.add(new SoapResponseLoggingInterceptor());
        outInterceptors.add(new SoapRequestLoggingInterceptor());

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

    @Bean
    CxfFaultLoggingPolicy soapCxfFaultLoggingPolicy() {
        return new SoapCxfFaultLoggingPolicy();
    }

    @Bean
    String platformSyncCronExpression(PlatformProperties properties) {
        var schedule = properties.syncSchedule();

        return "0 %d %d * * *".formatted(
                schedule.getMinute(),
                schedule.getHour()
        );
    }
}
