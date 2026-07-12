# Login Screen

**Source:** Figma node `1:3`

## Layout

| Property     | Value                |
|--------------|----------------------|
| Canvas       | 1440 × 1024          |
| Type         | Two-column desktop   |
| Sidebar      | Fixed 400px          |
| Content Area | Fill remaining width |
| Alignment    | Center login form    |
| Background   | `#000000`            |

## Brand Panel

### Content

| Element     | Value                                              |
|-------------|----------------------------------------------------|
| Logo        | Telekom logo                                       |
| Brand       | Hrvatski Telekom                                   |
| Eyebrow     | Network Operations                                 |
| Heading     | Wi-Fi CPE Configuration                            |
| Description | Manage Wi-Fi parameters for subscriber CPE devices |

### Style

| Property    | Value                                        |
|-------------|----------------------------------------------|
| Background  | `#0e0e0f`                                    |
| Border      | Right, `1px solid #39393c`                   |
| Padding     | `35px`                                       |
| Eyebrow     | JetBrains Mono, 10.5px, uppercase, `#f61488` |
| Heading     | Inter, bold, white                           |
| Description | Inter, muted white                           |

## Login Form

### Content

| Element       | Value                                                  |
|---------------|--------------------------------------------------------|
| Title         | Administrator login                                    |
| Subtitle      | Sign in to access the Wi-Fi CPE configuration console. |
| Username      | Text input (`admin`)                                   |
| Password      | Password input (masked)                                |
| Submit Button | Sign in                                                |

### Style

| Property         | Value                            |
|------------------|----------------------------------|
| Width            | 336px                            |
| Input Height     | ~40px                            |
| Input Background | `#1c1c1e`                        |
| Input Border     | `1px solid #39393c`              |
| Input Radius     | `0`                              |
| Input Padding    | `15px`                           |
| Button           | `#f61488` background, white text |

### Behavior

- Password visibility toggle.
- Submit authenticates the administrator.

## Variants

| Variant | Description                                      |
|---------|--------------------------------------------------|
| Desktop | This layout                                      |
| Mobile  | Dedicated design (`5:4` — `login-screen-mobile`) |
