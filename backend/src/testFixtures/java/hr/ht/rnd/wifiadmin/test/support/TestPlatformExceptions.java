package hr.ht.rnd.wifiadmin.test.support;

import hr.ht.rnd.wifiadmin.application.exception.CpeNotFoundException;
import hr.ht.rnd.wifiadmin.application.exception.PlatformConnectionException;
import hr.ht.rnd.wifiadmin.application.exception.PlatformResponseException;

public final class TestPlatformExceptions {

    private TestPlatformExceptions() {}

    public static CpeNotFoundException cpeNotFound(String cpeId) {
        var cause = new RuntimeException("missing");
        return new CpeNotFoundException(cpeId, cause);
    }

    public static PlatformConnectionException failedConnection() {
        return failedConnection("failure");
    }

    public static PlatformConnectionException failedConnection(String causeMessage) {
        var cause = new RuntimeException(causeMessage);
        return new PlatformConnectionException("Connection failed", cause);
    }

    public static PlatformResponseException invalidResponse() {
        return invalidResponse("invalid");
    }

    public static PlatformResponseException invalidResponse(String causeMessage) {
        return new PlatformResponseException(new RuntimeException(causeMessage));
    }
}
