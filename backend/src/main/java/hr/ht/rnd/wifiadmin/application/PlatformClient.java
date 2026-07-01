package hr.ht.rnd.wifiadmin.application;

import hr.ht.rnd.wifiadmin.domain.WifiConfiguration;

public interface PlatformClient {

    WifiConfiguration retrieveConfiguration(String cpeId);

    WifiConfiguration updateConfiguration(WifiConfiguration configuration);
}
