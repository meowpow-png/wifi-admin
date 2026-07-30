# Mobile CPE Details

**Source:** Figma node `23:293`

## Layout

| Property        | Value                                                                     |
|-----------------|---------------------------------------------------------------------------|
| Canvas          | 390 × 939                                                                 |
| Type            | Single-column mobile detail view                                          |
| Background      | `#000000`                                                                 |
| Structure       | Header, Device Summary, Configuration Card, Advanced Information, Actions |
| Content Padding | 20px                                                                      |

## Header

### Content

| Element | Value        |
|---------|--------------|
| Back    | Back button  |
| Title   | CPE DETAILS  |
| Logo    | Telekom logo |

### Style

| Property   | Value                                |
|------------|--------------------------------------|
| Background | `#0e0e0f`                            |
| Border     | Bottom, `1px solid #39393c`          |
| Title      | JetBrains Mono, uppercase, `#f61488` |

### Behavior

- Back button returns to the previous screen

## Device Summary

### Content

| Element | Value                                 |
|---------|---------------------------------------|
| Device  | Selected CPE ID                       |
| Status  | Online status and connection duration |

### Style

| Property | Value                                 |
|----------|---------------------------------------|
| Device   | JetBrains Mono, bold, white           |
| Status   | Green indicator with muted white text |

## Configuration Card

### Content

| Element    | Value            |
|------------|------------------|
| SSID       | Current SSID     |
| Wi-Fi Band | Band badge       |
| Encryption | Encryption badge |
| Password   | Masked           |
| Action     | Reveal password  |

### Style

| Property      | Value                                   |
|---------------|-----------------------------------------|
| Background    | `#18181a`                               |
| Border        | `1px solid #39393c`                     |
| Border Radius | 8px                                     |
| Padding       | 20px                                    |
| Labels        | Uppercase, muted gray                   |
| Values        | White                                   |
| Reveal Button | Dark magenta background, magenta border |

### Behavior

- Password is masked by default
- Reveal button toggles password visibility

## Advanced Information

### Content

| Element     | Value                      |
|-------------|----------------------------|
| MAC Address | Device MAC address         |
| IP Address  | Device IP address          |
| Firmware    | Installed firmware version |

### Style

| Property | Value                 |
|----------|-----------------------|
| Labels   | Muted gray            |
| Values   | JetBrains Mono, white |

## Actions

### Content

| Element          | Value         |
|------------------|---------------|
| Primary Action   | Reboot Device |
| Secondary Action | Factory Reset |

### Style

| Property             | Value                         |
|----------------------|-------------------------------|
| Reboot Button        | Dark background, white text   |
| Factory Reset Button | Dark background, magenta text |
| Border               | `1px solid #39393c`           |
| Border Radius        | 4px                           |

### Behavior

- Reboot Device restarts the selected CPE
- Factory Reset restores the selected CPE to factory defaults
