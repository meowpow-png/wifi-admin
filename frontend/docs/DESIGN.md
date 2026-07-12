# Design Specifications

Source: Figma file `Admin-login-and-logs-screen` from `figma.json`.

Use this document as the visual source of truth for dimensions, spacing, typography, colors, layout, and Figma node content. Implementation structure, behavior, data modeling, and reusable UI concerns belong in `docs/IMPLEMENTATION.md`.

## LoginScreen

Source: Figma file `Admin-login-and-logs-screen`, node `1:3` (`LoginScreen`).

### Canvas

- Desktop frame: 1440 x 1024
- Background: black (`#000`)
- Layout: two-column desktop grid
- Left brand panel: 400 px wide, full height
- Right login area: 1040 px wide, centered login form

### Brand Panel

- Panel bounds: x 0, y 0, 400 x 1024
- Background: near-black (`#0e0e0f`)
- Right border: 1 px solid dark gray
- Padding: 35 px top, 35-36 px horizontal, 35 px bottom
- Header: Telekom logo 30 x 34.54 px at x 35, y 35
- Brand text: `Hrvatski Telekom`, positioned 40.5 px to the right of the logo, Inter Semi Bold, 14 px, white
- Center content: vertically centered within the remaining panel area
- Eyebrow: Wi-Fi icon 14 x 14 px, `Network Operations`, uppercase magenta (`#f61488`), monospaced 10.5 px
- Heading: `Wi-Fi CPE Configuration`, 178 x 66 px, bold white, two lines
- Description: `Manage Wi-Fi parameters for subscriber CPE devices.`, muted white, 313 x 20 px

### Login Form

- Form container: 336 x 274 px at x 752, y 375
- Title: `Administrator login`, 195 x 28 px, Inter Semi Bold, 21 px, white
- Subtitle: `Sign in to access the Wi-Fi CPE configuration console.`, 314 x 18 px, muted white
- Form starts 49.5 px below title block
- Username and password labels are uppercase, 10.5 px, muted white, letter-spaced
- Inputs: 336 x 40.5 px, dark fill (`#1c1c1e`), 1 px gray border, square corners
- Input text inset: 15 px left, vertically centered
- Username visible value: `admin`
- Password masked value: `••••••••`
- Password visibility button: 23 x 23 px at right 10.5 px, top 8.75 px; icon 16 x 16 px
- Sign-in button: 336 x 38.5 px, magenta (`#f61488`), square corners, white centered `Sign in`

### Responsive Behavior

- This desktop node is paired with a separate mobile node, `5:4` (`login-screen-mobile`).
- Existing implementation breakpoint may still collapse desktop layout below 760 px, but mobile should follow the dedicated mobile composition where available.

## DashboardScreen

Source: Figma file `Admin-login-and-logs-screen`, node `1:101` (`DashboardScreen`).

### Canvas

- Desktop frame: 1440 x 1024
- Screenshot render height is 1122 px because table/content extends beyond the frame bounds
- Background: black (`#000`)
- Overall style: dense dark admin console with square controls and compact table rows

### Header

- Header bounds: 1440 x 49.63 px
- Background: near-black (`#0e0e0f`)
- Bottom border: 1 px dark gray
- Left cluster starts at x 21, y 10.5
- Telekom logo: 24 x 27.63 px
- Divider: 1 x 20 px at x 38
- Product title group: Wi-Fi icon 14 x 14 px and `Wi-Fi CPE Configuration`
- Title text: white, 12-14 px, positioned x 74, y 15.32
- Right cluster: x 1129.75, y 11.07, width 289.25 px
- Timestamp: `2026-07-05 11:13:4 UTC`, muted gray, monospaced 10.5 px
- User label: `admin`
- Logout button: 76.25 x 26.5 px, magenta border, dark magenta fill, logout icon 12 px and `Logout`

### Filters Bar

- Bounds: x 0, y 49.63, 1440 x 48.5 px
- Search input: x 21, y 10.5, 220 x 26.5 px
- Search placeholder: `Search CPE ID or SSID...`
- Band dropdown: x 251.5, y 10.5, 127 x 26.5 px
- Security dropdown: x 389, y 10.5, 145.5 x 26.5 px
- Controls use dark fill, 1 px gray border, square corners, muted text
- Record count: `12 / 12 records`, x 1324, y 16.75, muted monospaced text

### Table

- Table origin: x 21, y 112.13
- Table width: 1398 px; content rows are 1411 px wide including right action column
- Header row height: 33 px
- Data row height: 38 px
- Header labels: `cpe_id`, `wifi_band`, `ssid`, `encryption_type`, plus lock/action column
- Header text: lowercase monospaced labels, muted gray; active sorted `cpe_id` label is magenta with upward sort indicator
- Column starts: CPE ID x 36, Wi-Fi band x 313.44, SSID x 664.4, encryption x 950.93, action x 1390
- Row backgrounds alternate between `#101011` and `#151516`
- Row dividers: 1 px dark gray
- CPE IDs use bold monospaced white text
- Right action column uses 42 px cells with chevron icons

### Table Data

- Rows shown: `CPE_001` through `CPE_012`
- SSIDs shown: `Office-2G`, `Office-5G`, `Guest-2G`, `Guest-5G`, `Lab-Net`, `Lab-Net-5`, `Home-IoT`, `Home-Main`, `Shop-Floor`, `Shop-Office`, `Demo-Open`, `Demo-Secure`
- Band badges: `BAND_2_4_GHZ` orange/brown, `BAND_5_GHZ` blue
- Encryption badges: `WPA2_PSK` blue, `OPEN` gray, `WPA3_SAE` green, `WPA_PSK` orange, `WPA2_ENTERPRISE` blue, `WEP` red

## login-screen-mobile

Source: Figma file `Admin-login-and-logs-screen`, node `5:4` (`login-screen-mobile`).

### Canvas

- Mobile frame: 390 x 844
- Background: black (`#000`)
- Layout: single-column mobile login screen
- Horizontal content padding: 20 px for header, 24 px for main content/form

### Header

- Header bounds: 390 x 62 px
- Brand group: x 20, y 16, 154 x 30 px
- Telekom logo: 26 x 30 px
- Brand text: `Hrvatski Telekom`, x 36, y 6.5, 118 x 17 px, white
- Operations indicator: Wi-Fi icon 14 x 14 px at x 356, y 24, magenta
- Header bottom border: 1 px dark gray

### Main Content

- Main content starts at y 62 and is 601 px tall
- App title block: x 24, y 102, 342 x 58 px
- App title: `Wi-Fi CPE Configuration`, bold white, two lines
- Login form: x 24, y 274, 342 x 314 px
- Form title: `Administrator login`, 333 x 26 px
- Subtitle: `Sign in to access the Wi-Fi CPE configuration console.`, y 32, 333 x 20 px, muted white

### Inputs And Action

- Inputs group: y 84, width 342 px, height 150 px
- Username field: 342 x 65 px
- Password field: y 85, 342 x 65 px
- Labels: uppercase, muted white, 13 px high
- Input containers: 342 x 44 px, dark fill, 1 px gray border, 3-4 px radius
- Input text inset: 14 px left, y 14
- Username visible value: `admin`
- Password masked value: `••••••••`
- Password toggle icon: 18 x 18 px at x 310, y 13
- Sign-in button: x 0, y 266, 342 x 48 px, magenta fill, rounded 4 px, centered white `Sign in`

## admin-mobile

Source: Figma file `Admin-login-and-logs-screen`, node `6:4` (`admin-mobile`).

### Canvas

- Mobile frame: 390 x 1350
- Background: black (`#000`)
- Layout: fixed header, filter controls, record summary, vertical CPE card list
- Horizontal content padding: 20 px

### Header

- Header bounds: 390 x 54 px
- Telekom logo: x 20, y 12, 26 x 30 px
- Title: `WI-FI CPE CONFIGURATION`, x 112, y 20.5, 138 x 13 px, magenta uppercase monospaced text
- Logout button: x 316, y 16, 54 x 22 px, magenta border, dark fill, `Logout`
- Header bottom border: 1 px dark gray

### Filters

- Filters section: x 0, y 72, 390 x 103 px
- Search field: x 20, y 72, 350 x 40 px
- Search icon: 14 x 14 px at x 32, y 85
- Search placeholder: `Search CPE ID or SSID...`, x 54, y 84.5
- Filter chips row: x 20, y 130, height 27 px
- Chips: `All Bands` 74 x 27 px, `Secure Only` 90 x 27 px, `2.4 GHz` 67 x 27 px
- Chips use dark fill, gray border, 4 px radius, white text

### List Header

- Section origin: x 20, y 191
- Record count: `6 / 12 RECORDS`, 84 x 13 px, muted uppercase monospaced text
- Sort control: `Sort by ID ↑`, right-aligned at x 297, magenta

### CPE Cards

- Card stack: x 20, y 218, 350 x 1032 px
- Six cards shown with 12 px vertical gaps
- Card size: 350 x 162 px
- Card fill: dark gray (`#18181a`), 1 px gray border, 6 px radius
- Card padding: 16 px
- Card title: CPE ID, x 16, y 23, monospaced bold white
- Reveal-password icon button: 32 x 32 px, right aligned, dark fill, magenta lock icon
- Detail rows: label on left, value/badge on right
- Labels: `SSID`, `Band`, `Encryption`, muted gray
- Visible cards: `CPE_001`, `CPE_002`, `CPE_003`, `CPE_004`, `CPE_005`, and `CPE_009`

## mobile-cpe-details

Source: Figma file `Admin-login-and-logs-screen`, node `23:293` (`mobile-cpe-details`).

### Canvas

- Mobile frame: 390 x 939
- Background: black (`#000`)
- Layout: iOS status bar, header, device summary, configuration card, advanced information, destructive/admin actions, home indicator
- Horizontal content padding: 20 px

### Status Bar And Header

- Status bar: 390 x 48 px
- Time: `9:41`, x 24, y 14.5
- iOS signal, Wi-Fi, and battery icons: x 286, y 14, 80 x 20 px
- Header: x 0, y 48, 390 x 62 px, near-black fill, bottom border
- Back control: chevron-left 24 x 24 px at x 20, y 67
- Header title: `CPE DETAILS`, x 56, y 72, magenta uppercase monospaced text
- Telekom logo: x 344, y 64, 26 x 30 px

### Device Summary

- Main body starts at y 110
- Summary block: x 20, y 134, 350 x 67 px
- Device name: `CPE_001`, bold monospaced white, 350 x 42 px
- Status row: green 8 x 8 px dot plus `Online • Connected for 12d 4h`, muted white

### Configuration Card

- Card bounds: x 20, y 225, 350 x 354 px
- Card fill: dark gray, 1 px gray border, 8 px radius
- Inner padding: 20 px
- SSID block: label `SSID`, value `Office-2G`
- Wi-Fi band block: label `WI-FI BAND`, orange badge `BAND_2_4_GHZ`, 100 x 23 px
- Encryption block: label `ENCRYPTION`, green badge `WPA2_PSK`, 89 x 23 px with 10 px icon
- Divider: 310 px line at y 209 within the card
- Password block: label `PASSWORD`, masked value `••••••••••••`
- Reveal button: x 40, y 518, 310 x 42 px, dark magenta fill, magenta border, eye icon 18 px and `Reveal password`

### Advanced Information

- Section origin: x 20, y 603, 350 x 116 px
- Heading: `Advanced Information`
- Rows: `MAC Address` -> `00:1A:2B:3C:4D:5E`, `IP Address` -> `192.168.1.254`, `Firmware` -> `v2.4.12-stable`
- Labels are left-aligned muted gray; values are right-aligned white monospaced text

### Actions

- Actions container: x 20, y 743, 350 x 122 px
- `Reboot Device` button: 350 x 45 px, dark fill, 1 px gray border, white centered text
- `Factory Reset` button: y 77, 350 x 45 px, dark fill, 1 px gray border, magenta centered text
- Home indicator: x 128, y 926, 134 x 5 px rounded gray bar
