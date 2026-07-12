import type {CpeRecord} from './types'

export const cpeRecords: CpeRecord[] = [
    {
        id: 'CPE_001',
        wifiBand: 'BAND_2_4_GHZ',
        ssid: 'Office-2G',
        encryptionType: 'WPA2_PSK',
    },
    {
        id: 'CPE_002',
        wifiBand: 'BAND_5_GHZ',
        ssid: 'Office-5G',
        encryptionType: 'WPA2_PSK',
    },
    {
        id: 'CPE_003',
        wifiBand: 'BAND_2_4_GHZ',
        ssid: 'Guest-2G',
        encryptionType: 'OPEN',
    },
    {
        id: 'CPE_004',
        wifiBand: 'BAND_5_GHZ',
        ssid: 'Guest-5G',
        encryptionType: 'WPA3_SAE',
    },
    {
        id: 'CPE_005',
        wifiBand: 'BAND_2_4_GHZ',
        ssid: 'Lab-Net',
        encryptionType: 'WPA_PSK',
    },
    {
        id: 'CPE_006',
        wifiBand: 'BAND_5_GHZ',
        ssid: 'Lab-Net-5',
        encryptionType: 'WPA2_ENTERPRISE',
    },
    {
        id: 'CPE_007',
        wifiBand: 'BAND_2_4_GHZ',
        ssid: 'Home-IoT',
        encryptionType: 'WPA2_PSK',
    },
    {
        id: 'CPE_008',
        wifiBand: 'BAND_5_GHZ',
        ssid: 'Home-Main',
        encryptionType: 'WPA2_PSK',
    },
    {
        id: 'CPE_009',
        wifiBand: 'BAND_2_4_GHZ',
        ssid: 'Shop-Floor',
        encryptionType: 'WEP',
    },
    {
        id: 'CPE_010',
        wifiBand: 'BAND_5_GHZ',
        ssid: 'Shop-Office',
        encryptionType: 'WPA2_PSK',
    },
    {
        id: 'CPE_011',
        wifiBand: 'BAND_2_4_GHZ',
        ssid: 'Demo-Open',
        encryptionType: 'OPEN',
    },
    {
        id: 'CPE_012',
        wifiBand: 'BAND_5_GHZ',
        ssid: 'Demo-Secure',
        encryptionType: 'WPA3_SAE',
    },
]
