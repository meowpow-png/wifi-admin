import {
    Lock,
    Shield,
    ShieldAlert,
    ShieldCheck,
    ShieldOff,
} from 'lucide-react'

import styles from '../DashboardPage.module.css'

import type { EncryptionType, WifiBand } from '../types'

type BandBadgeProps = {
    value: WifiBand
}

export function BandBadge({ value }: BandBadgeProps) {
    return <span className={`${styles.badge} ${styles[`badge${value}`]}`}>{value}</span>
}

type EncryptionBadgeProps = {
    value: EncryptionType
}

export function EncryptionBadge({ value }: EncryptionBadgeProps) {
    return (
        <span className={`${styles.badge} ${styles[`badge${value}`]}`}>
      <EncryptionIcon value={value} />
            {value}
    </span>
    )
}

function EncryptionIcon({ value }: EncryptionBadgeProps) {
    if (value === 'OPEN') {
        return <ShieldOff size={11} strokeWidth={2} aria-hidden="true" />
    }

    if (value === 'WEP') {
        return <ShieldAlert size={11} strokeWidth={2} aria-hidden="true" />
    }

    if (value === 'WPA2_PSK' || value === 'WPA_PSK') {
        return <Lock size={11} strokeWidth={2} aria-hidden="true" />
    }

    if (value === 'WPA2_ENTERPRISE') {
        return <Shield size={11} strokeWidth={2} aria-hidden="true" />
    }

    return <ShieldCheck size={11} strokeWidth={2} aria-hidden="true" />
}
