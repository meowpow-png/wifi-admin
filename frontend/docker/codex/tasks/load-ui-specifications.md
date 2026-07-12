# Load UI Specifications

Update UI specifications from the Figma design.

## Instructions

1. Read Figma MCP URL from `./figma.json`
2. Use Figma MCP metadata to identify target UI nodes
3. Capture a Figma screenshot for each target node
4. Store screenshots in `./docs/screenshots`
5. Update `./docs/DESIGN.md`
6. Document each target Figma node as a separate level 2 markdown section (`##`)
7. Include key UI details: layout, copy, spacing, colors, controls, and responsive variants

## Notes

- Section names must match Figma node names exactly
- Each section must include the Figma node ID and screenshot link
- Screenshots must come from Figma MCP, not generated images
- Use `get_design_context` only if implementation-ready details are needed
- If `./docs` is not writable, write outputs to `./shared` and document why
