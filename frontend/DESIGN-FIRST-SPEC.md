# Frontend Product Design Document — Design-First Spec

Goal

Create a sleek, minimalist frontend that feels intentionally crafted — modern, editorial, and confident. Prioritize clarity, spacing, and typography; avoid decoration and template-like visuals.

**Design Principles**

- **Minimalism:** Only include elements that serve a purpose.
- **Whitespace as feature:** Generous vertical and horizontal gaps.
- **Straight edges only:** border-radius: 0 always.
- **Subtle contrast:** Rely on neutrals and one accent color.
- **Typography-first:** Hierarchy through size, weight, and spacing.
- **Purity:** No gradients, no shadows, no decorative fonts.

**Color System (Strict)**

- Primary background: #FAF9F7 (warm cream)
- Secondary section: #F2F1EE
- Primary text: #111111
- Secondary text: #666666
- Tertiary/labels: #888888
- Borders: #E5E5E5 (1px subtle)
- Accent (single): choose one — examples: #30343A (charcoal), #162236 (muted navy), or #2F3A2E (deep olive)

Rules:
- Use only neutrals + ONE accent.
- Accent only for active states, links, and focus outlines.
- No gradients.

**Typography**

Font family: modern sans-serif (Inter, IBM Plex Sans, Source Sans 3).

Hierarchy rules:
- Line-height ≥ 1.5 for body text.
- Headings: editorial scale (larger, confident, letter-spacing slightly increased for H1/H2 where appropriate).
- Avoid ALL CAPS except for very small labels.
- No heavy or decorative weights — keep to 300–700 range conservatively.

Suggested scale (example):
- H1: 40px / 48px line-height
- H2: 28px / 36px
- H3: 20px / 28px
- Body: 16px / 24px
- Small label: 12px / 18px

**Layout & Spacing**

- Use a grid with clear columns (e.g., 12-column up to desktop). Center content in a constrained max-width (900–1100px) for editorial balance.
- Consistent spacing scale (base = 8px). Use multiples: 8, 16, 24, 32, 48, 64.
- Large vertical gaps between sections (32–64px).
- Containers: straight edges, subtle 1px border for separations; no shadows.

**Containers & Borders**

- border-radius: 0
- border: 1px solid #E5E5E5 when separation is needed
- Backgrounds remain neutral

**Components (Design Constraints)**

Buttons
- Rectangular, sharp edges; no gradient/shadow.
- Primary (solid accent background + white or near-black text depending on contrast) or Secondary (transparent background, accent text, 1px accent border).
- Hover: subtle background or text color shift (100–150ms transition), e.g., opacity or 5–8% darken of accent.
- Disabled: muted gray background (#F2F1EE) + tertiary text.

Inputs / Forms
- Flat design: either thin full border (1px #E5E5E5) or single bottom border.
- Labels above inputs (not floating). Keep label weight medium and small text in tertiary color.
- Focus: outline or border change to accent color (use high contrast that meets accessibility ratios).
- Error state: accent may be replaced by a tightly controlled error color; keep usage minimal and clear.

Cards / Sections
- No elevation or shadows.
- Separate via spacing or thin borders.
- Backgrounds remain neutral; avoid colored cards.

Interaction & Motion
- Only minimal transitions (opacity, color) at 100–150ms.
- No scale, bounce, parallax, or attention-grabbing micro-interactions.

Accessibility
- Maintain contrast ratios (WCAG AA minimum for text).
- Ensure focus outlines are visible; use the accent for keyboard focus with a clear 3–4px outline or similar that respects the straight-edge aesthetic.
- Use semantic HTML; form labels linked to inputs.

Anti-Patterns to Avoid
- Rounded corners
- Gradients
- Box shadows
- Bright/varied color palette
- Excessive icons or 'card everywhere' layouts
- Template-style hero sections

Implementation Guidance

- Prefer custom CSS over component libraries. If a library is used, override defaults extensively to match rules above.
- Keep CSS explicit and semantic: component names should describe purpose (e.g., `PrimaryNav`, `ProductHero`, `CallToAction`), not generic UI library names.
- Centralize tokens in CSS variables for color, type, spacing.

Example CSS tokens (variables)

```css
:root{
  /* Colors */
  --bg-primary: #FAF9F7;
  --bg-secondary: #F2F1EE;
  --text-primary: #111111;
  --text-secondary: #666666;
  --text-tertiary: #888888;
  --border: #E5E5E5;
  --accent: #30343A; /* choose one */

  /* Typography */
  --font-family-system: "Inter", system-ui, -apple-system, "Segoe UI", Roboto, "Helvetica Neue", Arial;
  --fs-h1: 40px;
  --fs-h2: 28px;
  --fs-h3: 20px;
  --fs-body: 16px;
  --lh-body: 1.5;

  /* Spacing */
  --space-1: 8px;
  --space-2: 16px;
  --space-3: 24px;
  --space-4: 32px;
  --space-5: 48px;
}
```

Example component CSS snippets

```css
.button{display:inline-block;padding:12px 20px;border-radius:0;border:0;font-weight:600;transition:color 120ms ease, background-color 120ms ease;}
.button--primary{background:var(--accent);color:#fff}
.button--primary:hover{opacity:0.92}
.button--secondary{background:transparent;border:1px solid var(--accent);color:var(--accent)}

.input{padding:10px;border:1px solid var(--border);border-radius:0}
.input:focus{outline:3px solid var(--accent);outline-offset:2px}

.section{background:var(--bg-primary);padding:var(--space-4) 0}
.container{max-width:1100px;margin:0 auto;padding:0 var(--space-2)}
```

Example HTML structure (minimal)

```html
<section class="section">
  <div class="container">
    <h1 style="font-size:var(--fs-h1)">Confident, Calm Product</h1>
    <p style="color:var(--text-secondary);line-height:var(--lh-body)">Short supporting sentence that orients the user.</p>
    <div style="margin-top:var(--space-3)">
      <a class="button button--primary">Primary action</a>
      <a class="button button--secondary" style="margin-left:var(--space-2)">Secondary</a>
    </div>
  </div>
</section>
```

Hand-off Notes

- Fonts: include Inter (or chosen sans) via local hosting or variable fonts. Keep font weights limited (300,400,600,700).
- Deliver tokens as `:root` CSS variables plus a `tokens.css` file.
- Provide a small styleguide page with examples of headings, body, buttons, form fields, and an accessible color-check.
- When implementing, test keyboard focus states and mobile spacing.

Next steps (implementation suggestions)

- Add `frontend/src/styles/tokens.css` with variables above.
- Add `frontend/src/styles/base.css` for resets and typographic rules.
- Create `frontend/src/components` with semantic components: `TopBar`, `ProductHero`, `MainGrid`, `Form` and style them using tokens.

---

Design intent summary: calm, editorial, restrained. Let typography, spacing, and subtle contrast lead — not decoration.
