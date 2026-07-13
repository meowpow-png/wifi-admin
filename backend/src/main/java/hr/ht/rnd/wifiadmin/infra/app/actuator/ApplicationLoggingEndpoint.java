package hr.ht.rnd.wifiadmin.infra.app.actuator;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.Selector;
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation;
import org.springframework.boot.logging.LogLevel;
import org.springframework.boot.logging.LoggingSystem;
import org.springframework.stereotype.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static hr.ht.rnd.wifiadmin.common.StructuredLog.Event;
import static hr.ht.rnd.wifiadmin.common.StructuredLog.info;

@Component
@Endpoint(id = "logging")
class ApplicationLoggingEndpoint extends LoggingEndpoint {

    private static final Logger log = LoggerFactory.getLogger(ApplicationLoggingEndpoint.class);

    ApplicationLoggingEndpoint(LoggingSystem loggingSystem) {
        super(loggingSystem);
    }

    @Override
    String loggerName() {
        return "hr.ht.rnd.wifiadmin";
    }

    @WriteOperation
    void configure(@Selector LogLevel level) {
        setLogLevel(level);

        info(log).withEvent(Event.APPLICATION_LOG_LEVEL_CHANGED)
                .withField("log_level", level)
                .log();
    }
}
