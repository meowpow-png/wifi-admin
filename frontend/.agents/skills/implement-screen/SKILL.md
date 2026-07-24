---
name: implement-screen
description: Implement a frontend screen/page from this project's design documentation. Trigger when asked to build, implement, or update a specific screen or page in the frontend. Do not trigger for backend, API, or non-UI work.
---

Implement a screen from the design documentation.

## Instructions

1. Determine the target screen from the current user request
2. Read the corresponding screen specification from `./docs/screen/`
3. Read `./docs/ARCHITECTURE.md` before making architectural decisions
4. Use screenshots from `./docs/screen/refs/` only when additional visual clarification is needed
5. Reuse existing components and patterns whenever possible
6. Match the documented layout, styling, and behavior
7. Do not implement functionality not described in the design or architecture documentation
8. If the implementation differs from the design, explain the reason in your final summary

## Notes

- Treat the screen specification as the source of truth for the UI
- Use screenshots only for visual reference, not for extracting specifications
- Prefer composition over creating new reusable components unless multiple screens require them
- Keep changes focused on the requested screen
