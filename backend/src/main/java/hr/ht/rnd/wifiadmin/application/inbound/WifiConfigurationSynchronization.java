package hr.ht.rnd.wifiadmin.application.inbound;

import hr.ht.rnd.wifiadmin.application.exception.CpeNotFoundException;
import hr.ht.rnd.wifiadmin.application.exception.PlatformException;

/**
 * Synchronizes Wi-Fi configurations.
 */
public interface WifiConfigurationSynchronization {

    /**
     * Synchronizes the Wi-Fi configuration
     * of the CPE device with the given ID.
     *
     * @param cpeId the CPE device identifier
     *
     * @throws NullPointerException if {@code cpeId} is {@code null}
     * @throws CpeNotFoundException if the CPE device does not exist
     * @throws PlatformException if communication with the external platform fails
     */
    void synchronize(String cpeId);
}
