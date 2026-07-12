# Desktop CPE Details

**Source:** Figma node `23:4`

## Layout

| Property      | Value                                        |
|---------------|----------------------------------------------|
| Canvas        | 1440 × 1024                                  |
| Type          | Desktop admin console with split view        |
| Background    | `#000000`                                    |
| Structure     | Header, Filter Bar, Table View, Detail Panel |
| Content Split | Table View (1120px), Detail Panel (320px)    |

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

| Property      | Value                                   |
|---------------|-----------------------------------------|
| Background    | `#0e0e0f`                               |
| Border        | Bottom, `1px solid #39393c`             |
| Title         | Inter, white                            |
| Timestamp     | JetBrains Mono, muted gray              |
| Logout Button | Dark magenta background, magenta border |

## Filter Bar

### Content

| Element       | Value                  |
|---------------|------------------------|
| Search        | Search CPE ID or SSID  |
| Status Filter | Dropdown               |
| Band Filter   | Dropdown               |
| Record Count  | Visible record summary |

### Style

| Property      | Value                                |
|---------------|--------------------------------------|
| Background    | `#151517`                            |
| Controls      | Dark background, `1px solid #39393c` |
| Border Radius | 4px                                  |
| Placeholder   | Muted text                           |
| Record Count  | JetBrains Mono, muted gray           |

### Behavior

- Search filters the table.
- Status and band filters update visible records.
- Record count updates with filtered results.

## Table View

### Content

| Element   | Value                                              |
|-----------|----------------------------------------------------|
| Columns   | CPE ID, Wi-Fi Band, SSID, Encryption Type, Actions |
| Rows      | CPE devices                                        |
| Action    | Chevron opens device details                       |
| Selection | Selected row displayed in detail panel             |
| Sorting   | CPE ID sorted ascending by default                 |

### Style

| Property       | Value                                 |
|----------------|---------------------------------------|
| Width          | 1080px                                |
| Border         | `1px solid #39393c`                   |
| Border Radius  | 4px                                   |
| Header Height  | 33px                                  |
| Row Height     | 38px                                  |
| Header         | JetBrains Mono, lowercase, muted gray |
| Active Sort    | Magenta with ascending indicator      |
| Row Background | Alternating `#151517` / `#0e0e0f`     |
| Selected Row   | Magenta left border and dividers      |
| Action Cell    | Chevron icon                          |

### Behavior

- Supports sorting.
- Selecting a row updates the Detail Panel.
- Table supports scrolling.

## Data

### Content

| Element           | Value                                                               |
|-------------------|---------------------------------------------------------------------|
| Rows              | `CPE_001` – `CPE_012`                                               |
| SSIDs             | Office, Guest, Lab, Home, Shop, Demo networks                       |
| Band Badges       | `BAND_2_4_GHZ`, `BAND_5_GHZ`                                        |
| Encryption Badges | `WPA2_PSK`, `OPEN`, `WPA3_SAE`, `WPA_PSK`, `WPA2_ENTERPRISE`, `WEP` |

### Style

| Property         | Value                              |
|------------------|------------------------------------|
| Band Badge       | Brown/orange (2.4GHz), blue (5GHz) |
| Encryption Badge | Color-coded by encryption type     |
| Badge Typography | JetBrains Mono                     |
| Badge Radius     | 2px                                |

## Detail Panel

### Content

| Element        | Value                                          |
|----------------|------------------------------------------------|
| Header         | CPE DETAILS                                    |
| Device         | Selected CPE ID                                |
| Fields         | CPE ID, Wi-Fi Band, SSID, Encryption, Password |
| Password       | Masked with reveal action                      |
| Primary Action | Configure CPE                                  |

### Style

| Property         | Value                                 |
|------------------|---------------------------------------|
| Width            | 320px                                 |
| Background       | `#161618`                             |
| Border           | Left, `1px solid #39393c`             |
| Padding          | 24px                                  |
| Labels           | Inter, uppercase, muted gray          |
| Values           | JetBrains Mono, white                 |
| Reveal Button    | Dark background, bordered, 4px radius |
| Configure Button | `#f61488`, white text, 4px radius     |

### Behavior

- Displays details for the selected table row
- Password is masked by default
- Reveal button toggles password visibility
- Configure button opens the CPE configuration flow
