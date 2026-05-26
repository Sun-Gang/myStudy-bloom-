# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is **Bloom Finance** (繁花金融/财务管家), a personal finance management application. The repository contains:
- `product-ui（前端美术设计）/html5_page/` — Front-end UI design files (HTML/CSS/JS)
- `product(产品经理)/` — Product requirements documentation (PRD)

## Running the Application

All pages are standalone HTML files using CDN-hosted dependencies. Open any HTML file directly in a browser:
```bash
# No build step required
open product-ui（前端美术设计）/html5_page/pages/home.html
```

## Architecture

### Page Structure
- Pages are in `html5_page/pages/`
- Reusable components (sidebar, header) are in `html5_page/components/`
- Pages load shared components via `<iframe>` tags, not server-side includes or JS imports

### Page Naming Convention
| File | Purpose |
|------|---------|
| `home.html` | Dashboard / financial overview |
| `accounts.html` | Account management |
| `analysis.html` | Asset analysis |
| `bookkeeping.html` | Transaction ledger |
| `goals.html` | Financial goals |
| `pages/auth/login.html` | User login |
| `pages/auth/register.html` | User registration |
| `pages/auth/forgot-password.html` | Password recovery |

### Component Loading Pattern
Pages load sidebar and header via iframe:
```html
<iframe id="sidebar-iframe" src="../components/sidebar.html" class="fixed left-0 top-0 h-screen w-[288px] z-40" frameborder="0" scrolling="no"></iframe>
<main class="md:ml-[288px]">...content...</main>
```
The sidebar uses `target="_top"` on navigation links to navigate the browser to the correct page.

### Design System
- **Brand colors** (cherry blossom pink primary): defined in Tailwind config across all HTML files
- **Color palette tokens**: primary (#864e5a), primary-container (#ffb7c5), tertiary (#2f6a3f), etc.
- **Typography**: Plus Jakarta Sans (headlines), Be Vietnam Pro (body)
- **Icons**: Google Material Symbols Outlined
- **Shadows**: Custom soft-glow (pink tinted) instead of black shadows
- Full design tokens in `html5_page/assets/DESIGN.md`

### Stylesheets
- `html5_page/styles/shared.css` — Common utilities
- `html5_page/styles/common.css` — Additional shared styles
- `html5_page/scripts/tailwind-config.js` — Shared Tailwind config (inline in pages)

## Key Files

- `html5_page/components/sidebar.html` — Fixed left sidebar with navigation links
- `html5_page/components/header.html` — Top header with user greeting
- `html5_page/assets/DESIGN.md` — Complete design system documentation
- `html5_page/assets/screens/` — Design mockup PNGs
- `product(产品经理)/财务管家系统PRD.md` — Product requirements document