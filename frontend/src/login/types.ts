export type WifiBand = 'BAND_2_4_GHZ' | 'BAND_5_GHZ'

export type EncryptionType =
  | 'WPA2_PSK'
  | 'OPEN'
  | 'WPA3_SAE'
  | 'WPA_PSK'
  | 'WPA2_ENTERPRISE'
  | 'WEP'

export type CpeRecord = {
  id: string
  wifiBand: WifiBand
  ssid: string
  encryptionType: EncryptionType
  password?: string
}
