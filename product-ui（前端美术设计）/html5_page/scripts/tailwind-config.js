// ==========================================
// Bloom Finance - TailwindCSS Config
// Shared configuration for all pages
// ==========================================

tailwind.config = {
  darkMode: "class",
  theme: {
    extend: {
      "colors": {
        "on-tertiary": "#ffffff",
        "on-error": "#ffffff",
        "on-tertiary-container": "#256036",
        "on-secondary": "#ffffff",
        "on-error-container": "#93000a",
        "surface-container-highest": "#f2dde4",
        "primary-fixed": "#ffd9df",
        "on-secondary-fixed": "#1e1c00",
        "inverse-surface": "#3a2d32",
        "tertiary-fixed": "#b2f2bb",
        "surface-container": "#fee8f0",
        "on-secondary-fixed-variant": "#4b4822",
        "tertiary": "#2f6a3f",
        "surface-container-high": "#f8e3ea",
        "secondary": "#636037",
        "on-primary-container": "#7b4551",
        "error": "#ba1a1a",
        "secondary-container": "#e7e1ae",
        "surface-dim": "#ead5dc",
        "on-secondary-container": "#67643b",
        "outline-variant": "#d6c2c4",
        "background": "#fff8f8",
        "on-tertiary-fixed": "#00210b",
        "on-background": "#24181d",
        "primary-fixed-dim": "#fbb3c1",
        "secondary-fixed-dim": "#cdc897",
        "on-tertiary-fixed-variant": "#145129",
        "inverse-primary": "#fbb3c1",
        "surface-container-lowest": "#ffffff",
        "on-primary-fixed": "#360c19",
        "outline": "#837375",
        "surface-variant": "#f2dde4",
        "on-primary": "#ffffff",
        "tertiary-fixed-dim": "#96d5a0",
        "primary-container": "#ffb7c5",
        "on-surface-variant": "#514345",
        "inverse-on-surface": "#ffecf2",
        "tertiary-container": "#9ad9a4",
        "surface-tint": "#864e5a",
        "on-primary-fixed-variant": "#6b3743",
        "error-container": "#ffdad6",
        "surface": "#fff8f8",
        "surface-container-low": "#fff0f4",
        "on-surface": "#24181d",
        "primary": "#864e5a",
        "surface-bright": "#fff8f8",
        "secondary-fixed": "#eae4b1"
      },
      "borderRadius": {
        "DEFAULT": "0.25rem",
        "lg": "0.5rem",
        "xl": "0.75rem",
        "2xl": "1.5rem",
        "3xl": "2.5rem",
        "full": "9999px"
      },
      "spacing": {
        "section-gap": "40px",
        "element-gap": "16px",
        "gutter": "16px",
        "container-padding": "24px",
        "base": "8px"
      },
      "fontFamily": {
        "body-md": ["Be Vietnam Pro"],
        "label-sm": ["Be Vietnam Pro"],
        "display-lg": ["Plus Jakarta Sans"],
        "headline-md": ["Plus Jakarta Sans"],
        "display-lg-mobile": ["Plus Jakarta Sans"],
        "body-lg": ["Be Vietnam Pro"]
      },
      "fontSize": {
        "body-md": ["16px", {"lineHeight": "24px", "fontWeight": "400"}],
        "label-sm": ["12px", {"lineHeight": "16px", "letterSpacing": "0.05em", "fontWeight": "600"}],
        "display-lg": ["40px", {"lineHeight": "48px", "letterSpacing": "-0.02em", "fontWeight": "700"}],
        "headline-md": ["24px", {"lineHeight": "32px", "fontWeight": "600"}],
        "display-lg-mobile": ["32px", {"lineHeight": "38px", "letterSpacing": "-0.01em", "fontWeight": "700"}],
        "body-lg": ["18px", {"lineHeight": "28px", "fontWeight": "400"}]
      }
    },
  },
}