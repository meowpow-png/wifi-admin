import {LogOut, Wifi} from 'lucide-react'

import styles from '../DashboardPage.module.css'

type AdminHeaderProps = {
    onLogout: () => void
}

function AdminHeader({onLogout}: AdminHeaderProps) {
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
                <span className={styles.divider} aria-hidden="true"/>
                <span className={styles.productTitle}>
          <Wifi size={14} strokeWidth={2} aria-hidden="true"/>
          Wi-Fi CPE Configuration
        </span>
            </div>

            <div className={styles.headerActions}>
                <time dateTime="2026-07-05T11:13:04Z">2026-07-05 11:13:4 UTC</time>
                <span className={styles.actionDivider} aria-hidden="true"/>
                <span className={styles.userName}>admin</span>
                <button className={styles.logoutButton} type="button" onClick={onLogout}>
                    <LogOut size={12} strokeWidth={2} aria-hidden="true"/>
                    Logout
                </button>
            </div>
        </header>
    )
}

export default AdminHeader
