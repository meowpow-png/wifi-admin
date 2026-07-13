# Mobile Login Screen

**Source:** Figma node `5:4`

## Layout

| Property        | Value                       |
|-----------------|-----------------------------|
| Canvas          | 390 × 844                   |
| Type            | Single-column mobile layout |
| Background      | `#000000`                   |
| Content Padding | Header: 20px, Main: 24px    |

## Header

### Content

| Element   | Value            |
|-----------|------------------|
| Logo      | Telekom logo     |
| Brand     | Hrvatski Telekom |
| Indicator | Wi-Fi icon       |

### Style

| Property  | Value                       |
|-----------|-----------------------------|
| Border    | Bottom, `1px solid #39393c` |
| Brand     | Inter, white                |
| Indicator | `#f61488`                   |

## Main Content

### Content

| Element    | Value                                                  |
|------------|--------------------------------------------------------|
| Title      | Wi-Fi CPE Configuration                                |
| Form Title | Administrator login                                    |
| Subtitle   | Sign in to access the Wi-Fi CPE configuration console. |

### Style

| Property      | Value              |
|---------------|--------------------|
| Content Width | 342px              |
| Title         | Inter, bold, white |
| Subtitle      | Inter, muted white |

## Login Form

### Content

| Element       | Value                   |
|---------------|-------------------------|
| Username      | Text input (`admin`)    |
| Password      | Password input (masked) |
| Submit Button | Sign in                 |

### Style

| Property         | Value                                        |
|------------------|----------------------------------------------|
| Width            | 342px                                        |
| Input Height     | 44px                                         |
| Input Background | `#1c1c1e`                                    |
| Input Border     | `1px solid #39393c`                          |
| Input Radius     | 4px                                          |
| Input Padding    | `14px`                                       |
| Button Height    | 48px                                         |
| Button           | `#f61488` background, white text, 4px radius |

### Behavior

- Password visibility toggle
- Submit authenticates the administrator
