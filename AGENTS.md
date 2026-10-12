# Premium UI/UX & Creative Engineering Rules

These rules govern all frontend development, design decisions, styling, animations, and visual verification across all workspaces.

---

## 1. Project Inspection First (No Assumptions)
- **Inspect Before Changing**: Always inspect the existing codebase, dependencies, build tool, framework (React, Next.js, Vite, Vue, Vanilla, Thymeleaf), package manager, and CSS system before modifying or proposing code.
- **Preserve Existing Modifications**: Respect unstaged git changes, custom user logic, existing architectural decisions, and configuration files. Never overwrite established configurations without explicit purpose.
- **Detect Versions Accurately**: Determine if Tailwind is v3 or v4, React is 18 or 19, Motion is Framer Motion or modern `motion/react`, and ensure compatible packages.

---

## 2. Distinctive Design Direction (Avoid Generic AI Clichés)
- **Activate Design Intelligence**: Use the `frontend-design` and `ui-ux-pro-max` skills to establish an opinionated, brand-appropriate design system before writing code.
- **Avoid AI Tells & Clichés**:
  - Do NOT default to warm cream `#F4F1EA` with terracotta/warm-clay `#D97757` or plain monochrome cards.
  - Do NOT use repetitive SaaS card grids with identical borders and soft grey drop-shadows.
  - Do NOT put tracked-out ALL-CAPS eyebrow labels above every heading or append `→` to every button.
  - Do NOT accent single random words with italic or garish gradients.
- **Subject-Matter Grounding**: Adapt aesthetics directly to the industry and audience (e.g., tactile high-contrast data density for financial dashboards; sleek dark luxury for crypto vault platforms; warm ergonomic simplicity for health apps).
- **Hero-Section Impact**: Make the hero opening memorable with deliberate creative treatments (interactive canvas, glass-embedded metric terminal, spotlight mask, or dynamic live demonstration).

---

## 3. Premium Aesthetic & Dark Mode Tokens
When building dark-mode or premium modern aesthetics:
- **Surfaces**: Use rich near-black layered backgrounds (e.g., `#080B11`, `#0D111C`, `#131B2E`) instead of flat pitch-black `#000000` or washed-out gray.
- **Borders & Separation**: Use subtle translucent borders (e.g., `rgba(255, 255, 255, 0.08)` or `border-slate-800/80`) and fine hairlines rather than heavy solid strokes.
- **Accent Lighting**: Utilize curated ambient glows, spotlight gradients, and radial lighting with restrained opacity (10% - 25%).
- **Typography Scale**: Pair high-grade fonts with deliberate roles (e.g., `Plus Jakarta Sans` or `Inter` for clean body, `Orbitron` or `Syne` or `Outfit` for display headlines, `JetBrains Mono` for numeric metrics and code).
- **Iconography**: Use clean SVGs (Lucide, Heroicons, Phosphor) matching font stroke-weight. Never use raw emojis as UI icons.

---

## 4. Animation & Micro-Interactions Standards
- **Purposeful Motion**: Every animation must inform the user, establish spatial context, or confirm state changes. Avoid gratuitous floating or bouncing elements.
- **Timing & Curves**:
  - Micro-interactions (hover, focus, toggles, button press): 150ms – 250ms with `cubic-bezier(0.16, 1, 0.3, 1)` or fast ease-out.
  - Entrances & reveals: 300ms – 500ms staggered by 40ms – 80ms.
  - Layout morphs: Spring-based interactions (`stiffness: 300, damping: 30`).
- **Aceternity UI & Visual Effects**: Apply spotlight backgrounds, glowing borders, card gradients, or moving beams selectively to the highest-priority focal points. Do not stack multiple competing heavy effects on a single screen.
- **Smooth Scrolling (Lenis)**: When integrating smooth scrolling, preserve native anchor jumping, accessibility keyboard scrolling, and never hijack or lag mobile touch scrolling.
- **Reduced-Motion Mandate**: Every animation, particle system, or smooth scroll must strictly honor `@media (prefers-reduced-motion: reduce)` by providing instant cuts or static presentations.

---

## 5. Accessibility & Touch Standards (WCAG AA Floor)
- **Contrast**: Minimum 4.5:1 for body text and 3:1 for large display text and interactive controls against their background.
- **Interactive Targets**: Minimum 44×44px touch targets on mobile devices with `cursor: pointer` on desktop clickable items.
- **Focus Indicators**: Always provide distinct, visible focus rings for keyboard navigation (`outline: 2px solid var(--accent-ring)` with `offset: 2px`). Never suppress `:focus-visible`.
- **Form Controls**: Always accompany inputs with explicit labels or `aria-label`, contextual helper text, and in-place validation errors.

---

## 6. Development & Browser Verification Loop
Follow the strict verification lifecycle for every UI feature or page:
```
DESIGN → IMPLEMENT → RUN → INSPECT → IDENTIFY DEFECTS → REFINE → VERIFY
```
1. **Run**: Start the development server or test harness.
2. **Inspect**: Render in real browser environments (via Antigravity browser tools or Playwright) at standard viewports:
   - Mobile: 375px
   - Tablet: 768px
   - Desktop: 1440px
3. **Analyze**: Inspect alignment, typography hierarchy, contrast, touch targets, and visual polish.
4. **Fix Defects**: Directly fix identified rendering or responsive defects before completing the task.
5. **Never Claim Without Proof**: Never declare that a feature, test, or server works without verifying runtime execution and output.
