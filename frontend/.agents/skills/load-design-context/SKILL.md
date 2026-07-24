---
name: load-design-context
description: Generate design context documentation under ./docs/screen/ from the Figma file referenced in references/figma.json. Trigger when asked to pull design context, sync Figma designs, or generate/update screen specs from Figma. Do not trigger for implementing a screen that already has a spec.
---

Generate concise design context documentation for each Figma node referenced in `references/figma.json`.

## Instructions

1. Read `references/figma.json`
2. Use the Figma MCP server to load the referenced file and nodes
3. Call `get_design_context` for each node
4. Create one markdown document per node under `./docs/screen/`
5. Follow `./docs/screen/screen-template.md` template exactly
6. Capture only implementation-relevant design information
7. Omit Figma-specific metadata like coordinates or bounding boxes unless required for implementation
