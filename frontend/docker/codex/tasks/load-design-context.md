# Load Design Context

Generate concise design context documentation for each Figma node referenced in `figma.json`.

## Instructions

1. Read `./figma.json`
2. Use the Figma MCP server to load the referenced file and nodes
3. Call `get_design_context` for each node
4. Create one markdown document per node under `./docs/screen/`
5. Follow `./docs/screen/screen-template.md` template exactly
6. Capture only implementation-relevant design information
7. Omit Figma-specific metadata like coordinates or bounding boxes unless required for implementation
