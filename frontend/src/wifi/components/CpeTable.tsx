import {ChevronRight, Filter, Lock} from 'lucide-react'

import {BandBadge, EncryptionBadge} from './CpeBadges'
import styles from '../DashboardPage.module.css'

import type {CpeRecord, SortDirection, SortKey} from '../types'

type CpeTableProps = {
    records: CpeRecord[]
    selectedCpeId: string | null
    sortKey: SortKey
    sortDirection: SortDirection
    onSortToggle: (sortKey: SortKey) => void
    onToggleRecord: (record: CpeRecord) => void
}

const tableColumns: Array<{ key: SortKey; label: string }> = [
    {key: 'id', label: 'cpe_id'},
    {key: 'wifiBand', label: 'wifi_band'},
    {key: 'ssid', label: 'ssid'},
    {key: 'encryptionType', label: 'encryption_type'},
]

function CpeTable({
    records,
    selectedCpeId,
    sortKey,
    sortDirection,
    onSortToggle,
    onToggleRecord,
}: CpeTableProps) {
    return (
        <section className={styles.tableShell} aria-label="CPE records">
            <table className={styles.table}>
                <thead>
                <tr>
                    {tableColumns.map((column) => {
                        const isSorted = column.key === sortKey

                        return (
                            <th
                                key={column.key}
                                className={isSorted ? styles.sortedColumn : undefined}
                                scope="col"
                                aria-sort={
                                    isSorted ? (sortDirection === 'asc' ? 'ascending' : 'descending') : 'none'
                                }
                            >
                                <button
                                    className={styles.sortButton}
                                    type="button"
                                    aria-label={`Sort ${column.label} ${
                                        isSorted && sortDirection === 'asc' ? 'descending' : 'ascending'
                                    }`}
                                    onClick={() => onSortToggle(column.key)}
                                >
                                    <Filter size={9} strokeWidth={2} aria-hidden="true"/>
                                    {column.label}
                                    {isSorted ? <span>{sortDirection === 'asc' ? '↑' : '↓'}</span> : null}
                                </button>
                            </th>
                        )
                    })}
                    <th scope="col">
                        <span className={styles.srOnly}>Actions</span>
                        <Lock size={11} strokeWidth={2} aria-hidden="true"/>
                    </th>
                </tr>
                </thead>
                <tbody>
                {records.map((record) => (
                    <tr
                        key={record.id}
                        className={record.id === selectedCpeId ? styles.selectedRow : undefined}
                        tabIndex={0}
                        aria-selected={record.id === selectedCpeId}
                        onClick={() => onToggleRecord(record)}
                        onKeyDown={(event) => {
                            if (event.key === 'Enter' || event.key === ' ') {
                                event.preventDefault()
                                onToggleRecord(record)
                            }
                        }}
                    >
                        <td className={styles.cpeId}>{record.id}</td>
                        <td>
                            <BandBadge value={record.wifiBand}/>
                        </td>
                        <td>{record.ssid}</td>
                        <td>
                            <EncryptionBadge value={record.encryptionType}/>
                        </td>
                        <td className={styles.actionCell}>
                            <button
                                type="button"
                                aria-label={`Open details for ${record.id}`}
                                aria-pressed={record.id === selectedCpeId}
                            >
                                <ChevronRight size={16} strokeWidth={2} aria-hidden="true"/>
                            </button>
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
        </section>
    )
}

export default CpeTable
