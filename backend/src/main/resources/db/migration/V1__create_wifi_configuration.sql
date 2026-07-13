-- ============================
-- wifi_configuration table
-- ============================

CREATE TABLE wifi_configuration (
    cpe_id VARCHAR PRIMARY KEY,
    wifi_band VARCHAR NOT NULL,
    ssid VARCHAR NOT NULL,
    encryption_type VARCHAR NOT NULL,
    password VARCHAR,
    last_synchronized DATE NULL
);
