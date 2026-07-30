# Authenticate administrator
[arg("username", help="Administrator username")]
[arg("password", help="Administrator password")]
login username="${ADMIN_USERNAME}" password="${ADMIN_PASSWORD}":
    @export \
        USERNAME="{{username}}" \
        PASSWORD="{{password}}" && \
    envsubst < just/api/LoginRequest.json \
        | just/scripts/api.sh POST /auth/login --extract '.token' .jwt
    @printf 'Authentication successful.\n'

# Retrieve Wi-Fi configuration
[arg("cpe-id", help="CPE device identifier")]
wifi-get cpe-id:
    @just/scripts/api.sh GET "/wifi-parameter/{{cpe-id}}"

# Update Wi-Fi configuration
[arg("cpe-id", help="CPE device identifier")]
[arg("ssid", help="Wireless network name")]
[arg("wifi-band", help="Wi-Fi frequency band")]
[arg("encryption-type", help="Wi-Fi encryption type")]
[arg("password", help="Wireless network password")]
wifi-update cpe-id ssid wifi-band='BAND_2_4_GHZ' encryption-type='OPEN' password='null':
    @export \
        CPE_ID="{{cpe-id}}" \
        WIFI_BAND="{{wifi-band}}" \
        SSID="{{ssid}}" \
        ENCRYPTION_TYPE="{{encryption-type}}" \
        PASSWORD="{{password}}" && \
    envsubst < just/api/UpdateWifiConfigurationRequest.json \
        | just/scripts/api.sh \
            PUT \
            "/wifi-parameter"

# Change administrator password
[arg("current-password", help="Current administrator password")]
[arg("new-password", help="New administrator password")]
change-password current-password new-password:
    @export \
        CURRENT_PASSWORD="{{current-password}}" \
        NEW_PASSWORD="{{new-password}}" && \
    envsubst < just/api/ChangePasswordRequest.json \
        | just/scripts/api.sh \
            PUT \
            /admin/password && \
    printf 'Password changed successfully.\n'
