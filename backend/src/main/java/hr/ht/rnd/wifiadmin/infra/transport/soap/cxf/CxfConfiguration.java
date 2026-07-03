package hr.ht.rnd.wifiadmin.infra.transport.soap.cxf;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;
import org.apache.cxf.logging.FaultListener;

@Configuration
@EnableConfigurationProperties(CxfProperties.class)
public class CxfConfiguration {

    @Bean
    Bus cxfBus(CxfProperties properties, CxfFaultLoggingPolicy policy) {
        var bus = BusFactory.getDefaultBus();

        bus.setProperty(
                FaultListener.class.getName(),
                new CxfFaultListener(properties.logFaults(), policy)
        );
        return bus;
    }
}
