# Implementation Specifications

Source: Figma file `Admin-login-and-logs-screen` from `figma.json`.

Use `docs/DESIGN.md` as the visual source of truth for dimensions, spacing, typography, and colors. 
This document translates each Figma node into implementation structure, behavior, data, and reusable UI concerns.
Figma may report fractional pixel values; implementation CSS should round pixel values to whole numbers
unless a fractional value is required for a technical reason.

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

## desktop-cpe-details-panel

Node: `23:4`

Implement as the desktop authenticated dashboard state with an inline right-side CPE details panel.
This should reuse the same data, filtering, sorting, badges, header, logout behavior, and table primitives
as `DashboardScreen`, rather than creating a separate static page. The primary difference from
`DashboardScreen` is the selected-row state and the persistent `DetailPanel` column.

The layout should be a full-height admin shell with `AdminHeader`, `FilterBar`, `CpeTable`, and
`CpeDetailPanel` regions. On desktop widths matching the Figma frame, reserve 320 px for the detail panel
and let the table region fill the remaining width. Keep the table inside a 20 px content inset and preserve
the compact 33 px header and 38 px row rhythm.

Selection should be modeled explicitly, for example as `selectedCpeId`, defaulting to `CPE_001` for the
prototype state shown in Figma. The selected table row should display a magenta left border and should drive
the detail panel values. Row chevron actions should update the selected CPE or navigate to a detail route,
depending on the final routing model.

The filter model differs slightly from `DashboardScreen`: this node uses search, status, and band controls.
If both desktop dashboard variants are supported, keep a shared filter state shape that can tolerate optional
status and security filters. The record count should remain derived from filtered records.

`CpeDetailPanel` should render from the selected shared CPE record and include CPE ID, Wi-Fi band badge,
SSID, encryption badge, password value, reveal password action, and `Configure CPE` action. The password
reveal state should be scoped to the detail panel and should not leak into mobile card reveal controls.

Use existing or shared badge primitives for band and encryption colors. Avoid Figma asset URLs in the
implementation; map icons to local icon primitives or the existing icon library. The Figma screenshot shows
header/table/detail panel styling that is close to `DashboardScreen`, so any implementation difference should
be intentional and documented if the existing desktop route is extended instead of duplicated.

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
