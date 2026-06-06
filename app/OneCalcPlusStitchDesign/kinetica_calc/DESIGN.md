# Design System Document: Precision Editorial

## 1. Overview & Creative North Star

### The Creative North Star: "Kinetic Brutalism"
This design system rejects the "toy-like" appearance of standard calculators in favor of a sophisticated, high-end editorial experience. We treat numbers as data art. By combining the structural integrity of **Space Grotesk** with a deep, obsidian-toned environment, we create a tool that feels like a professional instrument rather than a basic utility.

The system breaks the "template" look through **Intentional Asymmetry**. We do not use equal padding on all sides of a grid; instead, we lean into a weighted bottom-heavy layout that prioritizes the thumb's natural arc, while the display area utilizes expansive "white space" (negative space) to allow complex equations to breathe.

---

## 2. Colors & Surface Philosophy

### The Pallete
The palette is rooted in a deep charcoal base (`#0e0e10`), accented by high-frequency vibrants that signal function through chroma.

- **Surface Strategy:** We strictly follow the **"No-Line" Rule**. Designers are prohibited from using 1px solid borders to section the interface. Separation is achieved exclusively through tonal shifts:
    - **Base Layer:** `surface` (#0e0e10)
    - **Secondary Input Area:** `surface_container_low` (#131315)
    - **Primary Action Keys:** `surface_container_high` (#1f1f22)
- **The "Glass & Gradient" Rule:** To provide "soul" to the primary actions (like `=` or `convert`), we move beyond flat hex codes. Use a linear gradient from `primary` (#6ffb85) to `primary_container` (#1db94d) at a 135-degree angle. For floating scientific panels, apply `surface_container_highest` with a 12px backdrop-blur and 80% opacity to create a "frosted glass" depth.

| Role | Token | Value |
| :--- | :--- | :--- |
| **Background** | `surface` | #0e0e10 |
| **Primary Action** | `primary` | #6ffb85 |
| **Secondary Action** | `secondary` | #e4e2e4 |
| **Tertiary/Scientific** | `tertiary` | #ffb153 |
| **Error/Clear** | `error` | #ff7351 |

---

## 3. Typography: The Editorial Voice

We utilize a dual-typeface system to balance technical precision with modern readability.

*   **Display & Headlines (Space Grotesk):** This is our "data" font. Its geometric, slightly quirky terminals provide a high-end, bespoke feel to the numbers.
    *   `display-lg`: 3.5rem (The Main Result. Tracking: -0.02em).
    *   `headline-md`: 1.75rem (Current Equation String).
*   **Body & Labels (Manrope):** A workhorse sans-serif used for functional UI elements (History, Settings, Scientific toggles).
    *   `title-md`: 1.125rem (Button Labels).
    *   `label-sm`: 0.6875rem (Secondary functions atop buttons).

**Hierarchy Note:** The "Result" (`display-lg`) should always feel significantly more authoritative than the "Input." Use `on_surface` for results and `on_surface_variant` for the calculation string to create a temporal hierarchy.

---

## 4. Elevation & Depth: Tonal Layering

Traditional shadows and borders are replaced by **Tonal Stacking**. 

1.  **The Layering Principle:** The main button grid sits on `surface_container`. When a button is pressed, it does not move "down"—it transforms to `surface_bright` or `primary_dim`, creating a "light-up" effect rather than a physical depression.
2.  **Ambient Shadows:** For floating modals (e.g., Currency Selection), use a shadow color tinted with `#000000` at 12% opacity with a `24` blur. 
3.  **The Ghost Border Fallback:** If a distinction is required between two similar dark tones, use `outline_variant` at 15% opacity. Never use 100% opaque outlines.
4.  **Glassmorphism:** Use for the sliding scientific panel. Apply `surface_container_highest` at 70% opacity with a 20px backdrop blur to allow the main grid to "ghost" through, maintaining the user's spatial context.

---

## 5. Components

### The Button Grid
The heart of the system. Buttons are not just targets; they are tactile regions.
- **Standard Button:** `Roundedness: md` (1.5rem). Background: `surface_container_high`. Text: `on_surface`.
- **Action Button (=):** `Roundedness: xl` (3rem) to break the grid's rigidity. Background: Gradient `primary` to `primary_container`. Text: `on_primary_container`.
- **Scientific Modifier:** `Roundedness: sm` (0.5rem). Background: `transparent`. Text: `tertiary`.

### Input Fields (The Display)
- No boxes. The display is a limitless field. Use `Spacing: 12` (2.75rem) as the bottom margin for the result to separate it from the keypad.
- **Error State:** Instead of a red box, the text color shifts to `error` (#ff7351) with a subtle "shake" haptic feedback.

### Lists (History & Memory)
- **The No-Divider Rule:** Forbid 1px dividers. Separate history items using a vertical `Spacing: 4` (0.9rem) and a background shift to `surface_container_low` for every second item (zebra striping) at 5% opacity.

---

## 6. Do’s and Don’ts

### Do:
- **Do** use `Spacing: 2` (0.4rem) between buttons to create a "tight" professional grid.
- **Do** use `primary_fixed` for the most important conversion or "equals" action to ensure it anchors the layout.
- **Do** allow the display text to scale down dynamically (from `display-lg` to `display-sm`) as the character count increases, preserving the single-line editorial look.

### Don't:
- **Don't** use 1px solid borders. It breaks the "high-end" immersion.
- **Don't** use pure black (#000000) for anything other than `surface_container_lowest`. It creates "black smear" on OLED screens; use `surface` (#0e0e10) instead.
- **Don't** use standard Material 3 "elevated" cards with heavy shadows. Use tonal shifts and glass blurs for depth.