---
name: Sakura Breeze
colors:
  surface: '#fff8f8'
  surface-dim: '#ead5dc'
  surface-bright: '#fff8f8'
  surface-container-lowest: '#ffffff'
  surface-container-low: '#fff0f4'
  surface-container: '#fee8f0'
  surface-container-high: '#f8e3ea'
  surface-container-highest: '#f2dde4'
  on-surface: '#24181d'
  on-surface-variant: '#514345'
  inverse-surface: '#3a2d32'
  inverse-on-surface: '#ffecf2'
  outline: '#837375'
  outline-variant: '#d6c2c4'
  surface-tint: '#864e5a'
  primary: '#864e5a'
  on-primary: '#ffffff'
  primary-container: '#ffb7c5'
  on-primary-container: '#7b4551'
  inverse-primary: '#fbb3c1'
  secondary: '#636037'
  on-secondary: '#ffffff'
  secondary-container: '#e7e1ae'
  on-secondary-container: '#67643b'
  tertiary: '#2f6a3f'
  on-tertiary: '#ffffff'
  tertiary-container: '#9ad9a4'
  on-tertiary-container: '#256036'
  error: '#ba1a1a'
  on-error: '#ffffff'
  error-container: '#ffdad6'
  on-error-container: '#93000a'
  primary-fixed: '#ffd9df'
  primary-fixed-dim: '#fbb3c1'
  on-primary-fixed: '#360c19'
  on-primary-fixed-variant: '#6b3743'
  secondary-fixed: '#eae4b1'
  secondary-fixed-dim: '#cdc897'
  on-secondary-fixed: '#1e1c00'
  on-secondary-fixed-variant: '#4b4822'
  tertiary-fixed: '#b2f2bb'
  tertiary-fixed-dim: '#96d5a0'
  on-tertiary-fixed: '#00210b'
  on-tertiary-fixed-variant: '#145129'
  background: '#fff8f8'
  on-background: '#24181d'
  surface-variant: '#f2dde4'
typography:
  display-lg:
    fontFamily: Plus Jakarta Sans
    fontSize: 40px
    fontWeight: '700'
    lineHeight: 48px
    letterSpacing: -0.02em
  display-lg-mobile:
    fontFamily: Plus Jakarta Sans
    fontSize: 32px
    fontWeight: '700'
    lineHeight: 38px
    letterSpacing: -0.01em
  headline-md:
    fontFamily: Plus Jakarta Sans
    fontSize: 24px
    fontWeight: '600'
    lineHeight: 32px
  body-lg:
    fontFamily: Be Vietnam Pro
    fontSize: 18px
    fontWeight: '400'
    lineHeight: 28px
  body-md:
    fontFamily: Be Vietnam Pro
    fontSize: 16px
    fontWeight: '400'
    lineHeight: 24px
  label-sm:
    fontFamily: Be Vietnam Pro
    fontSize: 12px
    fontWeight: '600'
    lineHeight: 16px
    letterSpacing: 0.05em
rounded:
  sm: 0.25rem
  DEFAULT: 0.5rem
  md: 0.75rem
  lg: 1rem
  xl: 1.5rem
  full: 9999px
spacing:
  base: 8px
  container-padding: 24px
  element-gap: 16px
  section-gap: 40px
  gutter: 16px
---

## Brand & Style

This design system is built on the pillars of warmth, approachability, and soft optimism. It transforms the traditionally rigid financial landscape into a "bubbly" and inviting sanctuary. The target audience values emotional resonance and simplicity over cold efficiency. 

The aesthetic style is a hybrid of **Soft Minimalism** and **Modern Kawaii**. It prioritizes extreme roundedness, generous whitespace (breathing room), and a tactile, "marshmallow-like" quality. Every interaction should feel like a gentle tap on a soft surface rather than a click on a machine. High-contrast blacks are strictly forbidden; instead, deep plums and warm greys provide the necessary legibility without the harshness.

## Colors

The palette is anchored by a flagship **Cherry Blossom Pink** (#FFB7C5), used for primary actions and key brand moments. The core background is a lighter, diluted version of this pink to maintain a "total immersion" feel. 

- **Creamy Yellow (#FFF9C4):** Used for highlighting gains, rewards, or secondary call-to-outs.
- **Mint Green (#B2F2BB):** Reserved for positive financial indicators and success states, replacing standard corporate greens with a softer alternative.
- **Lavender (#E5DBFF):** Used for information tags, tertiary buttons, and subtle structural divisions.
- **Text & Stroke:** We use a warm, muted Plum-Grey (#5D4E54) instead of pure black to maintain the "soft" visual threshold.

## Typography

The typography strategy employs **Plus Jakarta Sans** for headlines to leverage its soft, geometric curves and modern friendliness. For body copy and data, **Be Vietnam Pro** provides exceptional readability while maintaining a warm, contemporary tone.

To ensure the "bubbly" aesthetic, tracking (letter spacing) is slightly tightened on larger headlines and slightly loosened on small labels. Avoid all-caps styling unless used for very small, high-contrast labels. Numbers in financial tables should remain monospaced or have consistent tabular lining, but the typeface choice must remain rounded.

## Layout & Spacing

The layout follows a **Fluid Soft-Grid** model. We avoid dense information clusters, opting instead for large, padded containers that allow financial data to "breathe."

- **Desktop:** 12-column grid with 24px gutters and wide 80px side margins to center the content and create a focused, cozy feeling.
- **Mobile:** Single column with 24px side margins. 
- **Rhythm:** All spacing must be a multiple of 8px. Use 24px or 32px for internal card padding to ensure the content feels cushioned and protected within its container.

## Elevation & Depth

This design system rejects traditional black shadows. Depth is created through **Tonal Stacking** and **Soft Colored Glows**. 

- **Level 0 (Floor):** The Cherry Blossom background.
- **Level 1 (Cards):** White or pale cream surfaces with a very soft, diffused glow (Blur 20px, Spread 0) in a slightly darker pink or lavender tint (#F0D0D8).
- **Level 2 (Active Elements):** For hovered buttons or active modals, use a more pronounced glow that matches the element's primary color (e.g., a pink glow for a pink button) to create a "neon-soft" effect.
- **Glassmorphism:** Use sparingly for navigation bars or overlays with a heavy background blur (20px) and a white semi-transparent (60%) fill.

## Shapes

The shape language is the primary driver of the "friendly" personality. 
- **Cards & Containers:** Use a consistent 24px corner radius.
- **Buttons:** All buttons must be fully pill-shaped (100px radius) to emphasize the bubbly, tactile nature.
- **Selection Indicators:** Checkboxes and Radio buttons should have increased rounding (4px for checkboxes, making them feel like "squarcles").
- **Inputs:** Use 16px radius to balance space for text while maintaining the overall curved aesthetic.

## Components

### Buttons
Primary buttons are pill-shaped, using the Cherry Blossom Pink with white text. Secondary buttons use a thick 2px Plum-Grey outline or a Mint Green fill. Always include a slight "bounce" or scale-down animation on press to reinforce the tactile nature.

### Cards
Cards should be white or soft cream. Avoid borders; instead, use the soft pink glow described in the Elevation section. Padding within cards should be a generous 24px-32px to ensure financial data doesn't feel cramped.

### Input Fields
Inputs are large (height 56px) with a soft 16px radius and a light lavender background. On focus, the background shifts to white with a 2px Cherry Blossom Pink border.

### Financial Chips/Tags
Use the pastel palette for category tags (e.g., "Food" in Yellow, "Savings" in Green). Tags are always pill-shaped with "Be Vietnam Pro" SemiBold text in a darker shade of the tag's color.

### Progress Bars
Thick, rounded tracks (12px height) with a Mint Green fill. The "track" (empty part) should be a very pale lavender or off-white to maintain softness.

### Icons
Use "rounded" or "filled" icon sets. Avoid thin, sharp strokes. Every icon should have rounded terminals and soft corners to match the typography.