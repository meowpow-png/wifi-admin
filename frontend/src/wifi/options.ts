import type {BandFilter, SecurityFilter} from './types'

export const bandOptions: Array<{ value: BandFilter; label: string }> = [
    {value: 'all', label: 'All bands'},
    {value: 'BAND_2_4_GHZ', label: 'BAND_2_4_GHZ'},
    {value: 'BAND_5_GHZ', label: 'BAND_5_GHZ'},
]

export const securityOptions: Array<{ value: SecurityFilter; label: string }> = [
    {value: 'all', label: 'All encryption'},
    {value: 'WPA3_SAE', label: 'WPA3_SAE'},
    {value: 'WPA2_ENTERPRISE', label: 'WPA2_ENTERPRISE'},
    {value: 'WPA2_PSK', label: 'WPA2_PSK'},
    {value: 'WPA_PSK', label: 'WPA_PSK'},
    {value: 'WEP', label: 'WEP'},
    {value: 'OPEN', label: 'OPEN'},
]
