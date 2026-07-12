import {Eye, Settings, X} from 'lucide-react'
import {useState} from 'react'

import {bandOptions, securityOptions} from '../options'
import styles from '../DashboardPage.module.css'

import type {CpeRecord} from '../types'

import {BandBadge, EncryptionBadge} from './CpeBadges'

type CpeDetailPanelProps = {
    isOpen: boolean
    isEditing: boolean
    record: CpeRecord
    onToggleEditing: () => void
}

function CpeDetailPanel({
    isOpen,
    isEditing,
    record,
    onToggleEditing,
}: CpeDetailPanelProps) {
    const [visiblePasswordRecordId, setVisiblePasswordRecordId] = useState<string | null>(null)
    const isPasswordVisible = visiblePasswordRecordId === record.id
    const password = record.password ?? `${record.id.toLowerCase()}-secure`

    return (
        <aside
            className={`${styles.detailPanel} ${isOpen ? styles.detailPanelOpen : ''}`}
            aria-hidden={!isOpen}
            aria-label={`${record.id} details`}
        >
            <div className={styles.panelHeader}>
                <p>{isEditing ? 'EDIT CONFIGURATION' : 'CPE DETAILS'}</p>
                <h2>{record.id}</h2>
                <button
                    className={styles.panelHeaderButton}
                    type="button"
                    tabIndex={isOpen ? 0 : -1}
                    aria-label={isEditing ? 'Close edit configuration' : 'Edit CPE configuration'}
                    onClick={onToggleEditing}
                >
                    {isEditing ? (
                        <X size={16} strokeWidth={2} aria-hidden="true"/>
                    ) : (
                        <Settings size={16} strokeWidth={2} aria-hidden="true"/>
                    )}
                </button>
            </div>

            {isEditing ? (
                <>
                    <form key={record.id} className={styles.panelBody}>
                        <div className={styles.detailField}>
                            <label>CPE ID</label>
                            <span>{record.id}</span>
                        </div>

                        <div className={styles.detailField}>
                            <label htmlFor={`${record.id}-wifiBand`}>Wi-Fi Band</label>
                            <select
                                id={`${record.id}-wifiBand`}
                                className={styles.detailInput}
                                defaultValue={record.wifiBand}
                                tabIndex={isOpen ? 0 : -1}
                            >
                                {bandOptions.filter((option) => option.value !== 'all').map((option) => (
                                    <option key={option.value} value={option.value}>
                                        {option.label}
                                    </option>
                                ))}
                            </select>
                        </div>

                        <div className={styles.detailField}>
                            <label htmlFor={`${record.id}-ssid`}>SSID</label>
                            <input
                                id={`${record.id}-ssid`}
                                className={styles.detailInput}
                                type="text"
                                defaultValue={record.ssid}
                                tabIndex={isOpen ? 0 : -1}
                            />
                        </div>

                        <div className={styles.detailField}>
                            <label htmlFor={`${record.id}-encryption`}>Encryption</label>
                            <select
                                id={`${record.id}-encryption`}
                                className={styles.detailInput}
                                defaultValue={record.encryptionType}
                                tabIndex={isOpen ? 0 : -1}
                            >
                                {securityOptions.filter((option) => option.value !== 'all').map((option) => (
                                    <option key={option.value} value={option.value}>
                                        {option.label}
                                    </option>
                                ))}
                            </select>
                        </div>

                        <div className={styles.passwordField}>
                            <label htmlFor={`${record.id}-password`}>Password</label>
                            <input
                                id={`${record.id}-password`}
                                className={styles.detailInput}
                                type="text"
                                defaultValue={password}
                                tabIndex={isOpen ? 0 : -1}
                            />
                        </div>
                    </form>

                    <div className={styles.panelActions}>
                        <button type="button" tabIndex={isOpen ? 0 : -1}>
                            Save Changes
                        </button>
                    </div>
                </>
            ) : (
                <>
                    <dl className={styles.panelBody}>
                        <div className={styles.detailField}>
                            <dt>CPE ID</dt>
                            <dd>{record.id}</dd>
                        </div>

                        <div className={styles.detailField}>
                            <dt>Wi-Fi Band</dt>
                            <dd>
                                <BandBadge value={record.wifiBand}/>
                            </dd>
                        </div>

                        <div className={styles.detailField}>
                            <dt>SSID</dt>
                            <dd>{record.ssid}</dd>
                        </div>

                        <div className={styles.detailField}>
                            <dt>Encryption</dt>
                            <dd>
                                <EncryptionBadge value={record.encryptionType}/>
                            </dd>
                        </div>

                        <div className={styles.passwordField}>
                            <dt>PASSWORD</dt>
                            <dd>{isPasswordVisible ? password : '••••••••••'}</dd>
                        </div>
                    </dl>

                    <button
                        className={styles.revealButton}
                        type="button"
                        tabIndex={isOpen ? 0 : -1}
                        aria-pressed={isPasswordVisible}
                        onClick={() =>
                            setVisiblePasswordRecordId((currentRecordId) =>
                                currentRecordId === record.id ? null : record.id,
                            )
                        }
                    >
                        <Eye size={16} strokeWidth={2} aria-hidden="true"/>
                        {isPasswordVisible ? 'Hide password' : 'Reveal password'}
                    </button>
                </>
            )}
        </aside>
    )
}

export default CpeDetailPanel
