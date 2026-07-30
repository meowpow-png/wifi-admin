export type WifiBand = 'BAND_2_4_GHZ' | 'BAND_5_GHZ'

export type EncryptionType =
    | 'OPEN'
    | 'WEP'
    | 'WPA_PSK'
    | 'WPA2_PSK'
    | 'WPA2_ENTERPRISE'
    | 'WPA3_SAE'

export type BandFilter = 'all' | WifiBand

export type SecurityFilter = 'all' | EncryptionType

export type SortDirection = 'asc' | 'desc'

export type SortKey = 'id' | 'wifiBand' | 'ssid' | 'encryptionType'

export type CpeRecord = {
    id: string
    wifiBand: WifiBand
    ssid: string
    encryptionType: EncryptionType
    password?: string
}
