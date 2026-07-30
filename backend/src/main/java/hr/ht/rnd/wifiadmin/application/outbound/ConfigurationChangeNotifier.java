package hr.ht.rnd.wifiadmin.application.outbound;

/**
 * Publishes notifications when the
 * Wi-Fi configuration collection changes.
 */
public interface ConfigurationChangeNotifier {

    /**
     * Notifies subscribers that the
     * Wi-Fi configuration collection changed.
     */
    void notifyConfigurationsChanged();

}
