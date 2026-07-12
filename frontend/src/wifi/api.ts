import type {CpeRecord} from './types'

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL

type LoginResponse = {
    token: string
}

type WifiConfigurationResponse = {
    cpeId: string
    wifiBand: CpeRecord['wifiBand']
    ssid: string
    encryptionType: CpeRecord['encryptionType'] | null
    password: string | null
}

type UpdateWifiConfigurationRequest = {
    cpeId: string
    wifiBand: CpeRecord['wifiBand']
    ssid: string
    encryptionType: CpeRecord['encryptionType']
    password?: string | null
}

export async function login(
    username: string,
    password: string,
): Promise<string> {
    const response = await fetch(`${API_BASE_URL}/auth/login`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            username,
            password,
        }),
    })

    if (!response.ok) {
        throw new Error(getResponseErrorMessage(response.status))
    }

    const {token}: LoginResponse = await response.json()

    return token
}

export async function getWifiConfigurations(
    token: string,
): Promise<CpeRecord[]> {
    const response = await fetch(`${API_BASE_URL}/wifi-parameters`, {
        headers: {
            Authorization: `Bearer ${token}`,
        },
    })

    if (!response.ok) {
        throw new Error(getResponseErrorMessage(response.status))
    }

    const configurations: WifiConfigurationResponse[] = await response.json()

    return configurations.map(toCpeRecord)
}

export async function updateWifiConfiguration(
    token: string,
    record: CpeRecord,
): Promise<CpeRecord> {
    const response = await fetch(`${API_BASE_URL}/wifi-parameter`, {
        method: 'PUT',
        headers: {
            Authorization: `Bearer ${token}`,
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            cpeId: record.id,
            wifiBand: record.wifiBand,
            ssid: record.ssid,
            encryptionType: record.encryptionType,
            password: record.encryptionType === 'OPEN' || record.password === ''
                    ? null
                    : record.password,
        } satisfies UpdateWifiConfigurationRequest),
    })

    if (!response.ok) {
        throw new Error(getResponseErrorMessage(response.status))
    }

    const configuration: WifiConfigurationResponse = await response.json()

    return toCpeRecord(configuration)
}

function toCpeRecord(
    configuration: WifiConfigurationResponse,
): CpeRecord {
    return {
        id: configuration.cpeId,
        wifiBand: configuration.wifiBand,
        ssid: configuration.ssid,
        encryptionType: configuration.encryptionType ?? 'OPEN',
        password: configuration.password ?? undefined,
    }
}

function getResponseErrorMessage(status: number): string {
    if (status === 400) {
        return 'Enter a username and password.'
    }

    if (status === 401) {
        return 'The username or password is incorrect.'
    }

    return 'Unable to sign in. Please try again.'
}
