package hr.ht.rnd.wifiadmin.domain.wifi;

public final class TestWifiConfigurations {

    public static final String CPE_ID = "CPE_001";
    public static final String SSID = "Test Wi-Fi";

    public static final WifiPassword PASSWORD = new WifiPassword("password");
    public static final WifiEncryptionType ENCRYPTION_TYPE = WifiEncryptionType.WPA2_PSK;
    public static final WifiBand WIFI_BAND = WifiBand.BAND_2_4_GHZ;

    private TestWifiConfigurations() {}

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private String cpeId = CPE_ID;
        private WifiBand wifiBand = WIFI_BAND;
        private String ssid = SSID;
        private WifiEncryptionType encryptionType = ENCRYPTION_TYPE;
        private WifiPassword password = PASSWORD;

        private Builder() {
        }

        public Builder withCpeId(String cpeId) {
            this.cpeId = cpeId;
            return this;
        }

        public Builder withWifiBand(WifiBand wifiBand) {
            this.wifiBand = wifiBand;
            return this;
        }

        public Builder withSsid(String ssid) {
            this.ssid = ssid;
            return this;
        }

        public Builder withEncryptionType(WifiEncryptionType encryptionType) {
            this.encryptionType = encryptionType;
            return this;
        }

        public Builder withPassword(WifiPassword password) {
            this.password = password;
            return this;
        }

        public WifiConfiguration build() {
            return new WifiConfiguration(
                    cpeId,
                    wifiBand,
                    ssid,
                    encryptionType,
                    password
            );
        }
    }
}
