package hr.ht.rnd.wifiadmin.application.inbound;

import hr.ht.rnd.wifiadmin.domain.WifiConfiguration;

public interface WifiAdministration {

    WifiConfiguration retrieveConfiguration(String cpeId);

    WifiConfiguration updateConfiguration(WifiConfiguration configuration);
}
