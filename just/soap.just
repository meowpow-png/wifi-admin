# Retrieve Wi-Fi configuration from the SOAP platform
[arg("cpe-id", help="CPE device identifier")]
soap-get cpe-id:
    @export CPE_ID="{{cpe-id}}" && \
    envsubst < just/soap/GetCpeIdRequest.xml | \
    just soap "getCpeID"

# Update Wi-Fi configuration on the SOAP platform
[arg("cpe-id", help="CPE device identifier")]
[arg("wifi-band", help="Wi-Fi frequency band", pattern="BAND_2_4_GHZ|BAND_5_GHZ")]
[arg("ssid", help="Wireless network name")]
[arg("encryption-type", help="Wi-Fi encryption type", pattern="OPEN|WEP|WPA_PSK|WPA2_PSK|WPA3_SAE|WPA2_ENTERPRISE")]
[arg("password", help="Wireless network password")]
soap-put cpe-id ssid wifi-band='BAND_2_4_GHZ' encryption-type='OPEN' password='':
    @export \
        CPE_ID="{{cpe-id}}" \
        WIFI_BAND="{{wifi-band}}" \
        SSID="{{ssid}}" \
        ENCRYPTION_TYPE="{{encryption-type}}" \
        PASSWORD="{{password}}" && \
    envsubst < just/soap/UpdateCpeIdRequest.xml | \
    just soap "updateCpeId"

[private]
soap action:
    @curl \
        -s \
        -H "Content-Type: text/xml; charset=utf-8" \
        -H "SOAPAction: http://wifi-admin.local/platform/v1#{{action}}" \
        --data-binary @- \
        {{platform-url}}
