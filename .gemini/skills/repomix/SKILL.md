---
name: repomix
description: "Official Agent Skill from yamadashy/repomix: Analyze or explore a codebase (remote or local repository) by packing it with the Repomix CLI (npx repomix), reading metrics, token estimation, and generating clean XML/Markdown context for LLMs."
---

# /repomix (from yamadashy/repomix)

Pack and explore local or remote repositories into AI-friendly formats (XML, Markdown, JSON, Plain) with token metrics, directory trees, and security checking.

Based on the official Repomix Agent Skill: https://github.com/yamadashy/repomix/blob/main/skills/repomix-explorer/SKILL.md

## Trigger Scenarios
- **Pack local project**: "pack this codebase", "/repomix", "đóng gói mã nguồn", "export context for AI"
- **Explore structure**: "what's the structure of this repo", "show me largest files", "token count"
- **Analyze remote repo**: `npx repomix@latest --remote <owner/repo>`
- **Module packing**: `npx repomix@latest --include "src/main/java/**/APICLIENT/**"`

## Usage Workflows

### 1. Local Codebase Packing (Default)
```bash
# Đóng gói toàn bộ dự án hiện tại theo repomix.config.json:
npx repomix

# Đóng gói và tự động copy vào Clipboard:
npx repomix --copy

# Đóng gói chỉ định thư mục cụ thể:
npx repomix ./src --output src-context.xml

# Đóng gói tiết kiệm token (nén Tree-sitter cho dự án lớn):
npx repomix --compress
```

### 2. Remote Repository Exploration
```bash
# Quét và đóng gói một repository từ GitHub mà không cần git clone:
npx repomix@latest --remote <owner/repo> --output /tmp/<repo-name>.xml
```

### 3. File Filtering & Security
- Tự động đọc file `repomix.config.json` để loại bỏ `target/`, `.idea/`, `.git/`, `*.class`, `*.jar`.
- Tự động chạy bộ kiểm tra bảo mật (Secret scanning) để phát hiện mật khẩu hoặc token bị lộ.
