# Dashboard Screen

**Source:** Figma node `1:101`

## Layout

| Property   | Value                         |
|------------|-------------------------------|
| Canvas     | 1440 × 1024                   |
| Type       | Desktop admin console         |
| Background | `#000000`                     |
| Structure  | Header, Filter Bar, Table     |
| Content    | Table scrolls beyond viewport |

## Header

### Content

| Element   | Value                   |
|-----------|-------------------------|
| Logo      | Telekom logo            |
| Product   | Wi-Fi CPE Configuration |
| Timestamp | UTC timestamp           |
| User      | Current administrator   |
| Action    | Logout button           |

### Style

| Property      | Value                                                |
|---------------|------------------------------------------------------|
| Background    | `#0e0e0f`                                            |
| Border        | Bottom, `1px solid #39393c`                          |
| Title         | White, Inter                                         |
| Timestamp     | JetBrains Mono, muted gray                           |
| Logout Button | Dark magenta background, magenta border, logout icon |

## Filter Bar

### Content

| Element         | Value                  |
|-----------------|------------------------|
| Search          | Search CPE ID or SSID  |
| Band Filter     | Dropdown               |
| Security Filter | Dropdown               |
| Record Count    | Visible record summary |

### Style

| Property      | Value                                |
|---------------|--------------------------------------|
| Background    | Near black                           |
| Controls      | Dark background, `1px solid #39393c` |
| Border Radius | `0`                                  |
| Placeholder   | Muted text                           |
| Record Count  | JetBrains Mono, muted gray           |

### Behavior

- Search filters table by CPE ID or SSID
- Band dropdown filters by Wi-Fi band
- Security dropdown filters by encryption type
- Record count updates with filtered results

## Table

### Content

| Element | Value                                              |
|---------|----------------------------------------------------|
| Columns | CPE ID, Wi-Fi Band, SSID, Encryption Type, Actions |
| Rows    | CPE devices                                        |
| Action  | Row chevron for details                            |
| Sorting | CPE ID sorted ascending by default                 |

### Style

| Property       | Value                                 |
|----------------|---------------------------------------|
| Width          | Full content width                    |
| Header Height  | 33px                                  |
| Row Height     | 38px                                  |
| Header         | JetBrains Mono, lowercase, muted gray |
| Active Sort    | Magenta with ascending indicator      |
| Row Background | Alternating `#101011` / `#151516`     |
| Row Divider    | `1px solid #39393c`                   |
| CPE ID         | JetBrains Mono, bold, white           |
| Action Cell    | Chevron icon                          |

### Behavior

- Supports column sorting
- Selecting a row opens the CPE details
- Table supports scrolling

## Data

### Content

| Element           | Value                                                               |
|-------------------|---------------------------------------------------------------------|
| Rows              | `CPE_001` – `CPE_012`                                               |
| SSIDs             | Office, Guest, Lab, Home, Shop, Demo networks                       |
| Band Badges       | `BAND_2_4_GHZ`, `BAND_5_GHZ`                                        |
| Encryption Badges | `WPA2_PSK`, `OPEN`, `WPA3_SAE`, `WPA_PSK`, `WPA2_ENTERPRISE`, `WEP` |

### Style

| Property         | Value                          |
|------------------|--------------------------------|
| Band Badge       | Color-coded by band            |
| Encryption Badge | Color-coded by encryption type |
| Badge Typography | JetBrains Mono                 |
