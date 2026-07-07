package hr.ht.rnd.wifiadmin.infra.app.actuator;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.Selector;
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation;
import org.springframework.boot.logging.LogLevel;
import org.springframework.boot.logging.LoggingSystem;
import org.springframework.stereotype.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static hr.ht.rnd.wifiadmin.common.StructuredLog.*;

@Component
@Endpoint(id = "payload-logging")
class PayloadLoggingEndpoint extends LoggingEndpoint {

    private static final Logger log = LoggerFactory.getLogger(PayloadLoggingEndpoint.class);

    PayloadLoggingEndpoint(LoggingSystem loggingSystem) {
        super(loggingSystem);
    }

    @Override
    String loggerName() {
        return "hr.ht.rnd.wifiadmin.infra.transport.soap.payload";
    }

    @WriteOperation
    void configure(@Selector boolean enabled) {
        setLogLevel(enabled ? LogLevel.TRACE : LogLevel.OFF);

        info(log).withEvent(Event.SOAP_PAYLOAD_LOG_LEVEL_CHANGED)
                .withField("enabled", enabled)
                .log();
    }
}
