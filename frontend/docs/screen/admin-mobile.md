# Mobile Dashboard

**Source:** Figma node `6:4`

## Layout

| Property        | Value                                   |
|-----------------|-----------------------------------------|
| Canvas          | 390 × 1350                              |
| Type            | Single-column mobile dashboard          |
| Background      | `#000000`                               |
| Structure       | Header, Filters, List Header, CPE Cards |
| Content Padding | 20px                                    |

## Header

### Content

| Element | Value                   |
|---------|-------------------------|
| Logo    | Telekom logo            |
| Title   | WI-FI CPE CONFIGURATION |
| Action  | Logout button           |

### Style

| Property      | Value                                |
|---------------|--------------------------------------|
| Border        | Bottom, `1px solid #39393c`          |
| Title         | JetBrains Mono, uppercase, `#f61488` |
| Logout Button | Dark background, magenta border      |

## Filters

### Content

| Element      | Value                           |
|--------------|---------------------------------|
| Search       | Search CPE ID or SSID           |
| Filter Chips | All Bands, Secure Only, 2.4 GHz |

### Style

| Property          | Value                                                        |
|-------------------|--------------------------------------------------------------|
| Search Height     | 40px                                                         |
| Search Background | `#1c1c1e`                                                    |
| Search Border     | `1px solid #39393c`                                          |
| Search Radius     | 4px                                                          |
| Chips             | Dark background, white text, `1px solid #39393c`, 4px radius |

### Behavior

- Search filters CPE cards.
- Filter chips update the visible cards.

## List Header

### Content

| Element      | Value                  |
|--------------|------------------------|
| Record Count | Visible record summary |
| Sort         | Sort by ID             |

### Style

| Property     | Value                                 |
|--------------|---------------------------------------|
| Record Count | JetBrains Mono, uppercase, muted gray |
| Sort         | Magenta                               |

### Behavior

- Record count updates with filtered results.
- Sort changes the card order.

## CPE Cards

### Content

| Element      | Value                                                            |
|--------------|------------------------------------------------------------------|
| Cards        | CPE devices                                                      |
| Fields       | SSID, Band, Encryption                                           |
| Action       | Reveal password                                                  |
| Visible Data | `CPE_001`, `CPE_002`, `CPE_003`, `CPE_004`, `CPE_005`, `CPE_009` |

### Style

| Property      | Value                             |
|---------------|-----------------------------------|
| Width         | 350px                             |
| Background    | `#18181a`                         |
| Border        | `1px solid #39393c`               |
| Border Radius | 6px                               |
| Padding       | 16px                              |
| Title         | JetBrains Mono, bold, white       |
| Labels        | Muted gray                        |
| Action Button | Dark background with magenta icon |

### Behavior

- Cards are displayed in a vertical list
- Tapping a card opens the CPE details
- Reveal button toggles password visibility
