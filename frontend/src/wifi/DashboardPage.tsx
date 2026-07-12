import {useMemo, useState} from 'react'

import AdminHeader from './components/AdminHeader'
import CpeDetailPanel from './components/CpeDetailPanel'
import CpeTable from './components/CpeTable'
import FilterBar from './components/FilterBar'
import {cpeRecords} from './cpeData'
import styles from './DashboardPage.module.css'

import type {BandFilter, CpeRecord, SecurityFilter, SortDirection, SortKey,} from './types'

type DashboardPageProps = {
    onLogout: () => void
}

function DashboardPage({onLogout}: DashboardPageProps) {
    const [searchQuery, setSearchQuery] = useState('')
    const [bandFilter, setBandFilter] = useState<BandFilter>('all')
    const [securityFilter, setSecurityFilter] = useState<SecurityFilter>('all')
    const [sortKey, setSortKey] = useState<SortKey>('id')
    const [sortDirection, setSortDirection] = useState<SortDirection>('asc')
    const [selectedCpeId, setSelectedCpeId] = useState<string | null>(null)
    const [detailRecord, setDetailRecord] = useState<CpeRecord>(cpeRecords[0])
    const [isEditing, setIsEditing] = useState(false)

    const visibleRecords = useMemo(() => {
        const normalizedQuery = searchQuery.trim().toLowerCase()

        return cpeRecords.filter((record) => {
            const matchesSearch =
                normalizedQuery.length === 0 ||
                record.id.toLowerCase().includes(normalizedQuery) ||
                record.ssid.toLowerCase().includes(normalizedQuery)

            const matchesBand =
                bandFilter === 'all' || record.wifiBand === bandFilter

            const matchesSecurity =
                securityFilter === 'all' ||
                record.encryptionType === securityFilter

            return matchesSearch && matchesBand && matchesSecurity
        }).toSorted((left, right) => {
            const comparison = left[sortKey].localeCompare(right[sortKey])

            return sortDirection === 'asc'
                ? comparison
                : comparison * -1
        })
    }, [
        bandFilter,
        searchQuery,
        securityFilter,
        sortDirection,
        sortKey,
    ])

    function handleSortToggle(nextSortKey: SortKey) {
        if (nextSortKey !== sortKey) {
            setSortKey(nextSortKey)
            setSortDirection('asc')
            return
        }

        setSortDirection((currentDirection) =>
            currentDirection === 'asc' ? 'desc' : 'asc',
        )
    }

    function handleRecordToggle(record: CpeRecord) {
        if (selectedCpeId === record.id) {
            setSelectedCpeId(null)
            return
        }

        setIsEditing(false)
        setDetailRecord(record)
        setSelectedCpeId(record.id)
    }

    return (
        <main className={styles.page} aria-label="Wi-Fi CPE dashboard">
            <AdminHeader onLogout={onLogout}/>

            <FilterBar
                searchQuery={searchQuery}
                bandFilter={bandFilter}
                securityFilter={securityFilter}
                visibleCount={visibleRecords.length}
                totalCount={cpeRecords.length}
                onSearchQueryChange={setSearchQuery}
                onBandFilterChange={setBandFilter}
                onSecurityFilterChange={setSecurityFilter}
            />

            <div
                className={`${styles.contentArea} ${
                    selectedCpeId !== null
                        ? styles.contentAreaWithPanel
                        : ''
                }`}
            >
                <div className={styles.tablePane}>
                    <CpeTable
                        records={visibleRecords}
                        selectedCpeId={selectedCpeId}
                        sortKey={sortKey}
                        sortDirection={sortDirection}
                        onSortToggle={handleSortToggle}
                        onToggleRecord={handleRecordToggle}
                    />
                </div>

                <CpeDetailPanel
                    isOpen={selectedCpeId !== null}
                    isEditing={isEditing}
                    record={detailRecord}
                    onToggleEditing={() =>
                        setIsEditing((current) => !current)
                    }
                />
            </div>
        </main>
    )
}

export default DashboardPage
