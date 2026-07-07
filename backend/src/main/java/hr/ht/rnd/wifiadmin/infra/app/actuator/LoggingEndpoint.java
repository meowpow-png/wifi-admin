package hr.ht.rnd.wifiadmin.infra.app.actuator;

import org.springframework.boot.logging.LogLevel;
import org.springframework.boot.logging.LoggingSystem;

abstract class LoggingEndpoint {

    protected final LoggingSystem loggingSystem;

    LoggingEndpoint(LoggingSystem loggingSystem) {
        this.loggingSystem = loggingSystem;
    }

    abstract String loggerName();

    protected void setLogLevel(LogLevel level) {
        loggingSystem.setLogLevel(loggerName(), level);
    }
}
