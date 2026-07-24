---
name: git-history
description: Consolidate dev branch commit history for solution-branch submission. Use when cleaning up, consolidating, squashing history. Do not use when making routine commits or PRs.
---

# History Consolidation

You are preparing the `solution` branch for final submission
by reducing the development history to a concise, meaningful sequence of commits.

## Instructions

- Review the complete commit history of the `dev` branch
- Identify logical milestones rather than individual code changes
- Propose a plan for consolidating the history into approximately 10–20 commits
- Give each proposed commit a clear purpose and describe which existing commits belong to it
- Explain any commits that should remain separate

## Workflow

- Present the consolidation plan before proposing any rebases or squashes
- Group commits by intent rather than chronology where appropriate
- Record assumptions and uncertainties

## Constraints

- Do not inspect source code or full commit diffs
- Rely on commit messages, commit metadata, and targeted inspection only when needed
- Avoid commands that produce large outputs, such as full git logs with patches
- Inspect individual commits only when their purpose cannot be determined from the history
- Do not rewrite history or perform Git operations unless explicitly instructed
