import {useCallback, useEffect, useState} from 'react'

import * as React from 'react'

import {getWifiConfigurations} from '../api'

import type {CpeRecord} from '../types'

type UseWifiConfigurationsResult = {
    records: CpeRecord[]
    isLoading: boolean
    isRefreshing: boolean
    errorMessage: string | null
    reload: () => void
}

function useWifiConfigurations(
    token: string,
): UseWifiConfigurationsResult {
    const [records, setRecords] = useState<CpeRecord[]>([])
    const [isLoading, setIsLoading] = useState(true)
    const [isRefreshing, setIsRefreshing] = useState(false)
    const [errorMessage, setErrorMessage] = useState<string | null>(null)

    const reload = useCallback(() => {
        setIsRefreshing(true)
        setErrorMessage(null)

        void loadConfigurations(
            token,
            setRecords,
            setErrorMessage,
            setIsRefreshing,
        )
    }, [token])

    useEffect(() => {
        void loadConfigurations(
            token,
            setRecords,
            setErrorMessage,
            setIsLoading,
        )
    }, [token])

    return {
        records,
        isLoading,
        isRefreshing,
        errorMessage,
        reload,
    }
}

async function loadConfigurations(
    token: string,
    setRecords: React.Dispatch<React.SetStateAction<CpeRecord[]>>,
    setErrorMessage: React.Dispatch<React.SetStateAction<string | null>>,
    setLoading: React.Dispatch<React.SetStateAction<boolean>>,
): Promise<void> {
    try {
        const records = await getWifiConfigurations(token)

        setRecords(records)
        setErrorMessage(null)
    }
    catch (error) {
        setErrorMessage(
            error instanceof Error
                ? error.message
                : 'Unable to load Wi-Fi configurations.',
        )
    }
    finally {
        setLoading(false)
    }
}

export default useWifiConfigurations
