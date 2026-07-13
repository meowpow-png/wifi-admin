package hr.ht.rnd.wifiadmin.application.outbound;

import hr.ht.rnd.wifiadmin.domain.WifiConfiguration;

/**
 * Outbound port for interacting
 * with the external Wi-Fi platform.
 */
public interface PlatformClient {

    /**
     * Retrieves the Wi-Fi configuration of a CPE device.
     *
     * @param cpeId the CPE device identifier
     *
     * @return the Wi-Fi configuration
     * @throws NullPointerException if {@code cpeId} is {@code null}
     * @throws CpeNotFoundException if the CPE device does not exist
     * @throws PlatformTransportException if communication with the platform fails
     * @throws PlatformResponseException if the platform returns an invalid response
     */
    WifiConfiguration retrieveConfiguration(String cpeId);

    /**
     * Updates the Wi-Fi configuration of a CPE device.
     *
     * @param configuration the Wi-Fi configuration to update
     *
     * @return the updated Wi-Fi configuration
     * @throws NullPointerException if {@code configuration} is {@code null}
     * @throws PlatformTransportException if communication with the platform fails
     * @throws PlatformResponseException if the platform returns an invalid response
     */
    WifiConfiguration updateConfiguration(WifiConfiguration configuration);
}
