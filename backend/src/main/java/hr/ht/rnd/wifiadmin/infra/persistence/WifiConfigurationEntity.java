package hr.ht.rnd.wifiadmin.infra.persistence;

import hr.ht.rnd.wifiadmin.domain.WifiBand;
import hr.ht.rnd.wifiadmin.domain.WifiEncryptionType;

import jakarta.persistence.*;

import org.jspecify.annotations.Nullable;

@Entity
@SuppressWarnings("unused")
@Table(name = "wifi_configuration")
public class WifiConfigurationEntity {

    @Id
    @Column(nullable = false, updatable = false)
    private String cpeId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private WifiBand wifiBand;

    @Column(nullable = false)
    private String ssid;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private WifiEncryptionType encryptionType;

    @Column
    @Nullable
    private String password;

    protected WifiConfigurationEntity() {}

    WifiConfigurationEntity(
            String cpeId,
            WifiBand wifiBand,
            String ssid,
            WifiEncryptionType encryptionType,
            @Nullable String password
    ) {
        this.cpeId = cpeId;
        this.wifiBand = wifiBand;
        this.ssid = ssid;
        this.encryptionType = encryptionType;
        this.password = password;
    }

    public String getCpeId() {
        return cpeId;
    }

    public WifiBand getWifiBand() {
        return wifiBand;
    }

    public String getSsid() {
        return ssid;
    }

    public WifiEncryptionType getEncryptionType() {
        return encryptionType;
    }

    @Nullable
    public String getPassword() {
        return password;
    }
}
