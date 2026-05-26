# Bloom Finance - UI Design Documentation

## Project Overview
This directory contains the organized UI source files for the Bloom Finance personal finance management application.

---

## Directory Structure

```
stitch_html5_page_development/
├── pages/
│   ├── auth/                 # Authentication pages
│   │   ├── login.html        # Login page
│   │   ├── forgot-password.html  # Password recovery
│   │   └── register.html      # User registration
│   │
│   ├── account/              # Account management
│   │   └── ledger.html       # Transaction ledger /记账本
│   │
│   ├── savings/              # Savings & goals
│   │   └── goals.html        # Financial goals page
│   │
│   └── analytics/             # Financial analytics
│       └── assets.html       # Asset analysis dashboard
│
├── components/               # Reusable UI components
│   ├── sidebar-navigation.html    # Desktop sidebar nav
│   └── mobile-bottom-nav.html     # Mobile bottom tab nav
│
├── styles/                   # Shared stylesheets
│   └── shared.css            # Common CSS utilities
│
├── scripts/                  # Shared JavaScript
│   └── tailwind-config.js    # TailwindCSS configuration
│
└── assets/
    ├── screens/              # Design mockups (PNG)
    │   ├── 01-login-design.png
    │   ├── 02-forgot-password-design.png
    │   ├── 03-ledger-design.png
    │   ├── 04-savings-goals-design.png
    │   ├── 04-assets-analysis-design.png
    │   ├── 05-register-design.png
    │   └── 00-home-design.png
    │
    └── [sakura_breeze]/     # Design system documentation
```

---

## Page Naming Convention

| File | Purpose | Route |
|------|---------|-------|
| `pages/auth/login.html` | User login | `/auth/login` |
| `pages/auth/forgot-password.html` | Password recovery | `/auth/forgot-password` |
| `pages/auth/register.html` | User registration | `/auth/register` |
| `pages/account/ledger.html` | Transaction ledger | `/account/ledger` |
| `pages/savings/goals.html` | Financial goals | `/savings/goals` |
| `pages/analytics/assets.html` | Asset analysis | `/analytics/assets` |

---

## Design System

### Brand Identity
- **Brand Name**: Bloom Finance (繁花金融)
- **Tagline**: "让您的财富绽放" (Let your wealth bloom)
- **Design Philosophy**: Kawaii-inspired, soft, welcoming

### Color Palette
| Token | Hex | Usage |
|-------|-----|-------|
| `primary` | #864e5a | Rose pink - main brand color |
| `primary-container` | #ffb7c5 | Light pink - highlights |
| `tertiary` | #2f6a3f | Mint green - positive/savings |
| `tertiary-container` | #9ad9a4 | Light green - success states |
| `secondary` | #636037 | Olive - neutral accents |
| `secondary-container` | #e7e1ae | Light yellow - secondary highlights |
| `surface` | #fff8f8 | Off-white - backgrounds |
| `surface-container-lowest` | #ffffff | Pure white - cards |
| `on-primary` | #ffffff | White on primary |
| `on-surface` | #24181d | Dark text |
| `on-surface-variant` | #514345 | Muted text |

### Typography
| Style | Font | Size | Weight |
|-------|------|------|--------|
| `display-lg` | Plus Jakarta Sans | 40px | 700 |
| `display-lg-mobile` | Plus Jakarta Sans | 32px | 700 |
| `headline-md` | Plus Jakarta Sans | 24px | 600 |
| `body-lg` | Be Vietnam Pro | 18px | 400 |
| `body-md` | Be Vietnam Pro | 16px | 400 |
| `label-sm` | Be Vietnam Pro | 12px | 600 |

### Spacing System
| Token | Value |
|-------|-------|
| `base` | 8px |
| `gutter` | 16px |
| `element-gap` | 16px |
| `container-padding` | 24px |
| `section-gap` | 40px |

### Border Radius
| Token | Value |
|-------|-------|
| `xl` | 12px |
| `2xl` | 24px |
| `3xl` | 2.5rem |

### Icon Library
- **Source**: Google Material Symbols (Outlined)
- **Usage**: `class="material-symbols-outlined"`

---

## Component Inventory

### Sidebar Navigation (Desktop)
- Fixed left sidebar, 288px width
- Active state with `bg-primary-container` background
- Includes: logo, nav links, add transaction button, logout
- Shadow: `20px 0 40px rgba(240,208,216,0.3)`

### Mobile Bottom Navigation
- Fixed bottom, full width
- 4 tabs: 首页, 目标, 钱包, 分析
- Active state: `text-primary`
- Glassmorphism background

### Card Components
- **Standard Card**: `bg-surface-container-lowest`, `rounded-[32px]`, `soft-glow`
- **Highlight Card**: `bg-primary-container`, `soft-glow`

### Input Fields
- Height: 56px (auth), 14 (app pages)
- Border radius: `rounded-2xl`
- Focus: `bloom-input-focus` class

---

## Technical Stack

| Category | Technology |
|----------|------------|
| CSS Framework | TailwindCSS (CDN) |
| Fonts | Google Fonts (Plus Jakarta Sans, Be Vietnam Pro) |
| Icons | Material Symbols Outlined |
| Animations | Tailwind + CSS transitions |

---

## How to Use

### Running Pages Locally
```bash
# Simply open any HTML file in a browser
# All dependencies are loaded via CDN
```

### Including Shared Components
```html
<!-- For production, extract component HTML and include via JS -->
<!-- Or use server-side includes / framework components -->
```

### Using Shared Styles
```html
<link href="styles/shared.css" rel="stylesheet" />
```

### Using Tailwind Config
```html
<script src="scripts/tailwind-config.js"></script>
```

---

## Original Source Files

The original files have been organized into this structure from:
- `bloom_finance_1/code.html` → `pages/auth/login.html`
- `bloom_finance_2/code.html` → `pages/auth/forgot-password.html`
- `bloom_finance_4/code.html` → `pages/analytics/assets.html`
- `_3/code.html` → `pages/account/ledger.html`
- `_4/code.html` → `pages/savings/goals.html`
- `_5/code.html` → `pages/auth/register.html`

Design screen mockups preserved in `assets/screens/`