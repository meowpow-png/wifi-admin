# Implementation Specifications

Source: Figma file `Admin-login-and-logs-screen` from `figma.json`.

Use `docs/DESIGN.md` as the visual source of truth for dimensions, spacing, typography, and colors. 
This document translates each Figma node into implementation structure, behavior, data, and reusable UI concerns.

## LoginScreen

Node: `1:3`

Implement as the desktop unauthenticated route. The screen should be composed from a two-part layout: 
a brand panel and a login form region. Keep the brand panel, brand header, product message, form header, 
username field, password field, password visibility control, and sign-in action as separate 
semantic subcomponents or clearly separated JSX blocks.

The username and password controls can be static defaults for the prototype, but they should be real
form controls so validation and submission can be added without restructuring. The password visibility 
control should be implemented as a button with an accessible label and should be wired to toggle 
the password input type when behavior is added.

Use shared design tokens or CSS custom properties for recurring surfaces, borders, magenta accent, 
muted text, and input backgrounds. Use reusable logo and icon components where available; avoid embedding
Figma-generated image URLs directly because MCP asset URLs are short-lived.

This node pairs with `login-screen-mobile`. Desktop and mobile should not rely on scaling the same composition
when the structure differs; use responsive component variants or CSS branches that preserve each node's intended hierarchy.

## DashboardScreen

Node: `1:101`

Implement as the authenticated desktop admin route. Break the screen into `AdminHeader`, `FilterBar`,
`CpeTable`, `StatusBadge` or equivalent badge primitives, and row action controls. 
The table should be data-driven rather than hard-coded per row.

Represent CPE rows with a shared record shape containing at least `id`, `wifiBand`, `ssid`, `encryptionType`,
and optional detail fields reused by mobile details. Define enum-like values for Wi-Fi bands and encryption types
so badge labels, colors, filter behavior, and sorting stay consistent across desktop and mobile surfaces.

Filters should support search by CPE ID or SSID, Wi-Fi band selection, and security/encryption selection. 
The record count should derive from the filtered dataset. CPE ID sorting should be modeled explicitly 
so the active sort indicator and mobile `Sort by ID` behavior share the same state.

The row action column should navigate to a selected CPE detail view or open an equivalent 
details panel when that route exists. Logout should be an explicit button action exposed 
from the header, even if the prototype only resets local UI state.

## login-screen-mobile

Node: `5:4`

Implement as the mobile variant of the unauthenticated login route. The mobile hierarchy differs
from `LoginScreen`: header, app title, and form are distinct vertical sections. Keep it as a 
dedicated responsive variant rather than compressing the desktop brand panel.

Reuse the same login form state and submit handler as the desktop route. The mobile form should 
still expose username, password, password visibility, and sign-in controls as real interactive elements. 
Preserve accessible names independently from visual labels where icon-only controls are used.

The mobile header should use the same logo/icon primitives as other screens. Use a shared auth 
layout API or component props for desktop/mobile variants if that keeps markup clear; avoid 
introducing a generic layout abstraction if it makes the two Figma structures harder to match.

## admin-mobile

Node: `6:4`

Implement as the mobile authenticated list route. This is not a mobile table; it should render 
CPE records as cards using the same CPE dataset and filtering/sorting state as `DashboardScreen`.

Break the screen into mobile header, search/filter controls, list summary, sort control, and `CpeCard` 
components. Each card should display CPE ID, SSID, Wi-Fi band badge, encryption badge, and a reveal-password 
action. The card action should navigate to `mobile-cpe-details` or select the card for details.

Mobile filter chips should map to the same filter model as desktop dropdowns. The visible list 
can be filtered to the same records shown in the design, but the implementation should derive 
`shown / total` counts from data rather than hard-coding the summary text.

The reveal-password icon button should have an accessible label and a predictable 
disabled/loading state if password retrieval is asynchronous later. Badge rendering 
should reuse the same band/encryption badge primitives as desktop.

## mobile-cpe-details

Node: `23:293`

Implement as the mobile detail route for a selected CPE. The route should receive or 
resolve a CPE ID, look up the shared CPE record, and render summary, configuration, 
advanced information, password controls, and device actions from that data.

Use a header with back navigation that returns to `admin-mobile`. The status bar and home 
indicator are design-frame artifacts; implement them only if the application is intentionally 
simulating a device shell. Otherwise keep the app header and content structure.

The password reveal control should manage masked/unmasked state separately from the 
list card reveal action. Device actions should be real buttons with confirmation/error 
states planned for destructive operations, especially factory reset.

Advanced information should be modeled as fields on the selected CPE record or as 
a typed details object. Reuse badge and button primitives where possible so encryption,
Wi-Fi band, and action styling remain consistent with the list and dashboard screens.
