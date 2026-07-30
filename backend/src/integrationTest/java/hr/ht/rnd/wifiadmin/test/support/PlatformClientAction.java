package hr.ht.rnd.wifiadmin.test.support;

import hr.ht.rnd.wifiadmin.application.exception.PlatformException;
import hr.ht.rnd.wifiadmin.domain.wifi.WifiConfiguration;

import java.util.function.Supplier;

public interface PlatformClientAction extends Supplier<WifiConfiguration> {

    static PlatformClientAction throwException(PlatformException exception) {
        return () -> {
            throw exception;
        };
    }

    static PlatformClientAction failConnection() {
        return () -> {
            throw TestPlatformExceptions.failedConnection();
        };
    }

    static PlatformClientAction returnInvalidResponse() {
        return () -> {
            throw TestPlatformExceptions.invalidResponse();
        };
    }

    static PlatformClientAction failFindingCpeId(String cpeId) {
        return () -> {
            throw TestPlatformExceptions.cpeNotFound(cpeId);
        };
    }

    static PlatformClientAction returnConfiguration(WifiConfiguration configuration) {
        return () -> configuration;
    }
}
