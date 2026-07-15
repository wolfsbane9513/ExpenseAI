# ExpenseAI: Material 3 Design Strategy & UI/UX Gaps

## 1. App Understanding
ExpenseAI is an on-device, privacy-first expense management app that uses MediaPipe (Gemma) for AI-powered receipt scanning and transaction parsing. It supports:
- **Automatic Data Sources:** SMS parsing (for Indian banks) and Email Share Intent.
- **Privacy First:** No cloud processing; all AI and data stay on the device.
- **Review Workflow:** Gatekeeper mechanism where auto-detected expenses are reviewed before database entry.

## 2. Identified UI/UX Gaps

### A. Visual Consistency & "OLED" Polish
- **Current Theme:** Uses a DarkColorScheme, but the "OLED" mode mentioned in specs isn't fully realized (uses `#0F172A` Slate-950 instead of true black `#000000`).
- **Surface Elevation:** Relying on `surfaceVariant` with `copy(alpha = 0.5f)` is functional but lacks the depth and expressive glassmorphism common in modern fintech apps.

### B. Dashboard "Alive-ness"
- **Static Summaries:** The dashboard has a "Total Spending" card, but lacks interactive visualization (e.g., a mini spending trend line or category distribution ring).
- **Missing Empty States:** Needs expressive illustrations for "No expenses yet" or "Waiting for scan."

### C. Review Pipeline Feedback
- **Feedback Loop:** When the AI parses a receipt or SMS, there's a need for "thinking" animations or progress states that feel technical but friendly.
- **Batch Actions:** If scanning 10 SMS, reviewing one-by-one is tedious. A "Review All" or batch-swipe gesture would improve UX.

### D. Insights Depth
- **Predictive Spending:** AI can predict next month's spending based on history.
- **Anomalies:** Highlighting a spike in a specific category (e.g., "You spent 40% more on Dining this week").

---

## 3. Material Design 3 Expressive Strategy

### Visual Language: "Prism Tech"
A blend of deep blacks, vibrant accents (Emerald/Sky), and subtle gradients.

1.  **Glassmorphism:** Use `Subtle Glass` cards for secondary information.
2.  **Typography:** Utilize `Product Sans` (or standard Roboto) with strong weight contrasts (H1 vs Overline).
3.  **Haptics & Animation:** Shared element transitions between the Dashboard and Expense Details.

---

## 4. UI Mockup Concepts (Markdown Wireframes)

### Dashboard (Revised)
```text
+-----------------------------------+
| [Icon] ExpenseAI        [Model OK]|
+-----------------------------------+
| < April 2026 >                    |
+-----------------------------------+
|  +-----------------------------+  |
|  | TOTAL SPENDING              |  |
|  | ₹ 12,450.00                 |  |
|  | [Progress Bar: 60% of limit]|  |
|  +-----------------------------+  |
+-----------------------------------+
| SOURCES HEALTH                    |
| [SMS: Active] [Email: Linked]     |
+-----------------------------------+
| RECENT EXPENSES                   |
| (•) Starbucks         - ₹ 450     |
| (•) Petrol Pump      - ₹ 2,200    |
| (•) Amazon           - ₹ 1,150    |
+-----------------------------------+
| [Scan FAB]                        |
+-----------------------------------+
```

### Review Sheet (The "Gatekeeper")
```text
+-----------------------------------+
| REVIEW DETECTED EXPENSE           |
+-----------------------------------+
| SOURCE: SMS (HDFC Bank)           |
|                                   |
| [  ₹ 1,200.00  ] <- Editable      |
|                                   |
| VENDOR: Zomato                    |
| DATE:   Today, 8:45 PM            |
| CATEGORY: [ (🍽️) Dining  v ]      |
|                                   |
| [ DISCARD ]      [ CONFIRM ]      |
+-----------------------------------+
```

## 5. Implementation Recommendations
1.  **Dynamic Colors:** If on Android 12+, use `dynamicDarkColorScheme`.
2.  **Animated Visibility:** Use `AnimatedVisibility` for the `ModelStatusIndicator` transitions.
3.  **Custom Modifiers:** Create a `glassy()` modifier for cards.
