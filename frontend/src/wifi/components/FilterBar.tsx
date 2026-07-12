import {ChevronDown, Filter} from 'lucide-react'

import {bandOptions, securityOptions} from '../options'
import styles from '../DashboardPage.module.css'

import type {BandFilter, SecurityFilter} from '../types'

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
                <Filter className={styles.selectIcon} size={10} strokeWidth={2} aria-hidden="true"/>
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
                <ChevronDown
                    className={styles.selectChevron}
                    size={10}
                    strokeWidth={2}
                    aria-hidden="true"
                />
            </div>
            <div className={styles.selectControl}>
                <Filter className={styles.selectIcon} size={10} strokeWidth={2} aria-hidden="true"/>
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
                <ChevronDown
                    className={styles.selectChevron}
                    size={10}
                    strokeWidth={2}
                    aria-hidden="true"
                />
            </div>
            <span className={styles.recordCount}>
        {visibleCount} / {totalCount} records
      </span>
        </section>
    )
}

export default FilterBar
