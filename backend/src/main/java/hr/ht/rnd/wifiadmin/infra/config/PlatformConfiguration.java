package hr.ht.rnd.wifiadmin.infra.config;

import hr.ht.rnd.wifiadmin.infra.platform.PlatformProperties;
import hr.ht.rnd.wifiadmin.infra.platform.wsdl.WifiPlatformPortType;
import hr.ht.rnd.wifiadmin.infra.platform.wsdl.WifiPlatformService;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jakarta.xml.ws.BindingProvider;

@Configuration
@EnableConfigurationProperties(PlatformProperties.class)
public class PlatformConfiguration {

    @Bean
    WifiPlatformPortType platformPort(PlatformProperties properties) {
        var service = new WifiPlatformService();
        var port = service.getWifiPlatformPort();

        var requestContext = ((BindingProvider) port).getRequestContext();

        requestContext.put(
                BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
                properties.soapEndpoint()
        );
        return port;
    }
}
