import {
  ChevronDown,
  ChevronRight,
  Eye,
  Filter,
  Lock,
  LogOut,
  Shield,
  ShieldAlert,
  ShieldCheck,
  ShieldOff,
  Settings,
  Wifi,
} from 'lucide-react'
import { useMemo, useState } from 'react'

import { cpeRecords } from './cpeData'
import styles from './DashboardPage.module.css'

import type { CpeRecord, EncryptionType, WifiBand } from './types'

type DashboardPageProps = {
  onLogout: () => void
}

type BandFilter = 'all' | WifiBand
type SecurityFilter = 'all' | EncryptionType
type SortDirection = 'asc' | 'desc'
type SortKey = 'id' | 'wifiBand' | 'ssid' | 'encryptionType'

const tableColumns: Array<{ key: SortKey; label: string }> = [
  { key: 'id', label: 'cpe_id' },
  { key: 'wifiBand', label: 'wifi_band' },
  { key: 'ssid', label: 'ssid' },
  { key: 'encryptionType', label: 'encryption_type' },
]

const bandOptions: Array<{ value: BandFilter; label: string }> = [
  { value: 'all', label: 'All bands' },
  { value: 'BAND_2_4_GHZ', label: 'BAND_2_4_GHZ' },
  { value: 'BAND_5_GHZ', label: 'BAND_5_GHZ' },
]

const securityOptions: Array<{ value: SecurityFilter; label: string }> = [
  { value: 'all', label: 'All encryption' },
  { value: 'WPA3_SAE', label: 'WPA3_SAE' },
  { value: 'WPA2_ENTERPRISE', label: 'WPA2_ENTERPRISE' },
  { value: 'WPA2_PSK', label: 'WPA2_PSK' },
  { value: 'WPA_PSK', label: 'WPA_PSK' },
  { value: 'WEP', label: 'WEP' },
  { value: 'OPEN', label: 'OPEN' },
]

function DashboardPage({ onLogout }: DashboardPageProps) {
  const [searchQuery, setSearchQuery] = useState('')
  const [bandFilter, setBandFilter] = useState<BandFilter>('all')
  const [securityFilter, setSecurityFilter] = useState<SecurityFilter>('all')
  const [sortKey, setSortKey] = useState<SortKey>('id')
  const [sortDirection, setSortDirection] = useState<SortDirection>('asc')
  const [selectedCpeId, setSelectedCpeId] = useState<string | null>(null)
  const [detailRecord, setDetailRecord] = useState<CpeRecord>(cpeRecords[0])

  const visibleRecords = useMemo(() => {
    const normalizedQuery = searchQuery.trim().toLowerCase()

    return cpeRecords
      .filter((record) => {
        const matchesSearch =
          normalizedQuery.length === 0 ||
          record.id.toLowerCase().includes(normalizedQuery) ||
          record.ssid.toLowerCase().includes(normalizedQuery)
        const matchesBand = bandFilter === 'all' || record.wifiBand === bandFilter
        const matchesSecurity =
          securityFilter === 'all' || record.encryptionType === securityFilter

        return matchesSearch && matchesBand && matchesSecurity
      })
      .toSorted((left, right) => {
        const comparison = left[sortKey].localeCompare(right[sortKey])
        return sortDirection === 'asc' ? comparison : comparison * -1
      })
  }, [bandFilter, searchQuery, securityFilter, sortDirection, sortKey])

  function handleSortToggle(nextSortKey: SortKey) {
    if (nextSortKey !== sortKey) {
      setSortKey(nextSortKey)
      setSortDirection('asc')
      return
    }

    setSortDirection((currentDirection) => (currentDirection === 'asc' ? 'desc' : 'asc'))
  }

  function handleRecordToggle(record: CpeRecord) {
    if (selectedCpeId === record.id) {
      setSelectedCpeId(null)
      return
    }

    setDetailRecord(record)
    setSelectedCpeId(record.id)
  }

  return (
    <main className={styles.page} aria-label="Wi-Fi CPE dashboard">
      <AdminHeader onLogout={onLogout} />
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
          selectedCpeId !== null ? styles.contentAreaWithPanel : ''
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
        <CpeDetailPanel isOpen={selectedCpeId !== null} record={detailRecord} />
      </div>
    </main>
  )
}

type AdminHeaderProps = {
  onLogout: () => void
}

function AdminHeader({ onLogout }: AdminHeaderProps) {
  return (
    <header className={styles.header}>
      <div className={styles.headerBrand}>
        <scale-logo
          className={styles.logo}
          variant="magenta"
          transparent
          size={24}
          language=""
          logo-hide-title
        />
        <span className={styles.divider} aria-hidden="true" />
        <span className={styles.productTitle}>
          <Wifi size={14} strokeWidth={2} aria-hidden="true" />
          Wi-Fi CPE Configuration
        </span>
      </div>

      <div className={styles.headerActions}>
        <time dateTime="2026-07-05T11:13:04Z">2026-07-05 11:13:4 UTC</time>
        <span className={styles.actionDivider} aria-hidden="true" />
        <span className={styles.userName}>admin</span>
        <button className={styles.logoutButton} type="button" onClick={onLogout}>
          <LogOut size={12} strokeWidth={2} aria-hidden="true" />
          Logout
        </button>
      </div>
    </header>
  )
}

type FilterBarProps = {
  searchQuery: string
  bandFilter: BandFilter
  securityFilter: SecurityFilter
  visibleCount: number
  totalCount: number
  onSearchQueryChange: (value: string) => void
  onBandFilterChange: (value: BandFilter) => void
  onSecurityFilterChange: (value: SecurityFilter) => void
}

function FilterBar({
  searchQuery,
  bandFilter,
  securityFilter,
  visibleCount,
  totalCount,
  onSearchQueryChange,
  onBandFilterChange,
  onSecurityFilterChange,
}: FilterBarProps) {
  return (
    <section className={styles.filterBar} aria-label="CPE filters">
      <input
        className={styles.searchInput}
        type="search"
        placeholder="Search CPE ID or SSID..."
        value={searchQuery}
        onChange={(event) => onSearchQueryChange(event.target.value)}
      />

      <div className={styles.selectControl}>
        <Filter className={styles.selectIcon} size={10} strokeWidth={2} aria-hidden="true" />
        <select
          className={styles.selectInput}
          aria-label="Filter by Wi-Fi band"
          value={bandFilter}
          onChange={(event) => onBandFilterChange(event.target.value as BandFilter)}
        >
          {bandOptions.map((option) => (
            <option key={option.value} value={option.value}>
              {option.label}
            </option>
          ))}
        </select>
        <ChevronDown className={styles.selectChevron} size={10} strokeWidth={2} aria-hidden="true" />
      </div>

      <div className={styles.selectControl}>
        <Filter className={styles.selectIcon} size={10} strokeWidth={2} aria-hidden="true" />
        <select
          className={styles.securitySelect}
          aria-label="Filter by security"
          value={securityFilter}
          onChange={(event) => onSecurityFilterChange(event.target.value as SecurityFilter)}
        >
          {securityOptions.map((option) => (
            <option key={option.value} value={option.value}>
              {option.label}
            </option>
          ))}
        </select>
        <ChevronDown className={styles.selectChevron} size={10} strokeWidth={2} aria-hidden="true" />
      </div>

      <span className={styles.recordCount}>
        {visibleCount} / {totalCount} records
      </span>
    </section>
  )
}

type CpeTableProps = {
  records: CpeRecord[]
  selectedCpeId: string | null
  sortKey: SortKey
  sortDirection: SortDirection
  onSortToggle: (sortKey: SortKey) => void
  onToggleRecord: (record: CpeRecord) => void
}

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
                    <Filter size={9} strokeWidth={2} aria-hidden="true" />
                    {column.label}
                    {isSorted ? <span>{sortDirection === 'asc' ? '↑' : '↓'}</span> : null}
                  </button>
                </th>
              )
            })}
            <th scope="col">
              <span className={styles.srOnly}>Actions</span>
              <Lock size={11} strokeWidth={2} aria-hidden="true" />
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
                <BandBadge value={record.wifiBand} />
              </td>
              <td>{record.ssid}</td>
              <td>
                <EncryptionBadge value={record.encryptionType} />
              </td>
              <td className={styles.actionCell}>
                <button
                  type="button"
                  aria-label={`Open details for ${record.id}`}
                  aria-pressed={record.id === selectedCpeId}
                >
                  <ChevronRight size={16} strokeWidth={2} aria-hidden="true" />
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </section>
  )
}

type CpeDetailPanelProps = {
  isOpen: boolean
  record: CpeRecord
}

function CpeDetailPanel({ isOpen, record }: CpeDetailPanelProps) {
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
        <p>CPE DETAILS</p>
        <h2>{record.id}</h2>
      </div>

      <dl className={styles.panelBody}>
        <div className={styles.detailField}>
          <dt>CPE ID</dt>
          <dd>{record.id}</dd>
        </div>
        <div className={styles.detailField}>
          <dt>Wi-Fi Band</dt>
          <dd>
            <BandBadge value={record.wifiBand} />
          </dd>
        </div>
        <div className={styles.detailField}>
          <dt>SSID</dt>
          <dd>{record.ssid}</dd>
        </div>
        <div className={styles.detailField}>
          <dt>Encryption</dt>
          <dd>
            <EncryptionBadge value={record.encryptionType} />
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
        <Eye size={16} strokeWidth={2} aria-hidden="true" />
        {isPasswordVisible ? 'Hide password' : 'Reveal password'}
      </button>

      <div className={styles.panelActions}>
        <button type="button" tabIndex={isOpen ? 0 : -1}>
          <Settings size={14} strokeWidth={2} aria-hidden="true" />
          Configure CPE
        </button>
      </div>
    </aside>
  )
}

type BandBadgeProps = {
  value: WifiBand
}

function BandBadge({ value }: BandBadgeProps) {
  return <span className={`${styles.badge} ${styles[`badge${value}`]}`}>{value}</span>
}

type EncryptionBadgeProps = {
  value: EncryptionType
}

function EncryptionBadge({ value }: EncryptionBadgeProps) {
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

export default DashboardPage
