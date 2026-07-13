# Load Implementation Specifications

Extract implementation-ready specifications from the Figma design.

## Instructions

1. Read Figma MCP URL from `./figma.json`
2. Use Figma MCP server to identify the referenced design file and target nodes
3. Use Figma `get_design_context` for each target node before implementation work
4. Update or create implementation documentation in `./docs/IMPLEMENTATION.md`
5. Document each Figma node as a separate level 2 markdown section (`##`)
6. Include implementation-relevant details for layout, typography, spacing, colors etc.
7. Note any differences between the Figma design and the current code implementation

## Notes

- Each section name should match each Figma node name exactly
- Do not download or include screenshots in implementation specifications
- Keep the document focused on engineering translation only; 
  do not duplicate visual specs already captured in `DESIGN.md`.
