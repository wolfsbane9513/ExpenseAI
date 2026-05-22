# FIRE OS — Spec 1: The Engine + Live War Room

> Status: Draft for review
> Date: 2026-05-22
> Author: Ravi Prakash (with Claude Code)
> Supersedes: the "simple single-`monthly_investable` engine" sketch discussed earlier in brainstorming.

## 1. Background & Goal

We are repurposing the existing **ExpenseAI** Android app into **FIRE OS** — a personal
financial operating system that answers one question continuously: *"What does this
decision cost you in FIRE days?"*

The concrete target output for this first slice is a **live, editable, on-device
version of the "War Room"** (`fire40_complete_final.html`) — a document-derived
financial model that Claude.ai produced from the user's real financial documents.
The War Room is not a budgeting view; it is a multi-phase projection that computes a
FIRE date, a corpus trajectory, a projected net worth, and — critically — an **honest
reframe** of whether the goal is actually reachable.

### Decisions already made (brainstorming)

| Decision | Choice | Rationale |
|---|---|---|
| Codebase | **Evolve** the native Android app | Reuses existing Kotlin/Compose/Room/security stack; preserves on-device privacy moat; the FIRE engine is pure math that needs no backend. |
| Privacy | **On-device** for the engine + War Room | Avoids sending financial data to the cloud. |
| FIRE target | **Derived from spending** (`annual_retirement_expenses × 25`) | FIRE-native; ties the goal to lifestyle. |
| Data input (this spec) | **Structured entry / edit forms** | Document extraction is a *separate* spec; these forms also become the confirm-and-correct surface that extraction feeds into. |
| Rename | **Soft rebrand** — display name → "FIRE OS", package `com.expenseai` unchanged | Zero-risk, reversible. |

### Non-goals for Spec 1 (explicitly deferred)

- **Document ingestion / extraction** (the `Home_loan` PDFs) → **Spec 2 (Hybrid Import)**.
- **Scenario overlays** (RSU lumpsum, TransmuteLabs income toggles) → **Spec 1.5**.
- **"Should I Buy This?" purchase-decision screen** → **Spec 1.5**.
- Cloud advisor, time→money converter, realtime/push, SMS auto-classification beyond
  what already exists.
- **Construction-linked loan disbursement modeling** (tranches during construction).
  v1 models the loan from EMI-start only (see §4.4).

## 2. Architecture Overview

```
Edit Model forms ──writes──▶ Room (SQLCipher, on-device)
                                  │
                                  ▼
                            FireEngine (pure Kotlin, domain layer)
                            month-by-month simulation
                                  │
                  ┌───────────────┼───────────────┐
                  ▼               ▼               ▼
            FireDate +      NetWorthAtDate     Reframe
            Trajectory       breakdown      (gap / options)
                  │               │               │
                  └───────────────┼───────────────┘
                                  ▼
                       War Room Compose screens
                       (live, recompute on model/expense change)
```

The engine is a **pure Kotlin domain component** with no Android dependencies, so it is
fully unit-testable on the JVM. It is the single source of truth; the UI is a pure
function of `FireModel → FireResult`.

The **Edit Model forms** are deliberately designed as the same surface that **Spec 2**
will populate from extracted documents — extraction produces a `FireModel` (or partial
fields) that lands in these forms for user confirmation. Spec 1 must keep the
model<->form mapping clean enough that Spec 2 can write into it without rework.

## 3. Data Model (Room + SQLCipher, all local)

All entities are **generic** — no user's numbers are hardcoded. The DB migration adds
these tables to the existing encrypted `ExpenseDatabase` (bump version, add migration).

| Entity | Key fields | Notes |
|---|---|---|
| `FireProfile` (single row) | `id`, `targetMode` (ANNUAL_EXPENSES_25X \| EXPLICIT_TARGET), `annualRetirementExpenses`, `explicitTargetCorpus`, `targetDate`, `equityCagrPct`, `inflationPct`, `startCorpus`, `simStartDate` | `targetCorpus` is derived, not stored. |
| `Asset` | `id`, `label`, `assetClass`, `value`, `liquid` (bool), `countsTowardCorpus` (bool) | Lets EPF / vehicle / real estate be excluded from compoundable corpus like the War Room. |
| `IncomeStream` | `id`, `owner`, `label`, `monthlyNet`, `effectiveFrom`, `effectiveTo?`, `type` (SALARY \| BUSINESS \| PASSIVE), `annualGrowthPct?` | Multiple streams; growth applied per year. |
| `Liability` | `id`, `name`, `principalAtEmiStart`, `annualRatePct`, `tenureMonths`, `emiStartDate`, `currentOutstanding?` | EMI computed from these; pre-`emiStartDate` = no EMI (see §4.4). |
| `RecurringOutflow` | `id`, `label`, `monthlyAmount`, `startDate?`, `endDate?`, `category` | Rent (ends at possession), SIPs, insurance, society, remittances, lifestyle. |
| `OneTimeEvent` | `id`, `label`, `amount`, `date`, `direction` (INFLOW \| OUTFLOW) | Possession own-fund outflow, registration, etc. |
| `Property` | `id`, `name`, `allInCost`, `currentValue`, `appreciatedValueAtTarget?`, `possessionDate`, `linkedLiabilityId?` | For net-worth display; does **not** count toward compoundable corpus. |
| `PhaseBoundary` (optional) | `id`, `label`, `startDate` | User-named phase boundaries; if none, engine auto-derives phases (see §4.5). |

Existing `ExpenseEntity` is **reused**: confirmed expenses in the current month reduce
the active month's lifestyle/discretionary outflow (live FIRE-day feedback). Spec 1
wires this read-only into the engine input; the purchase-decision UI is Spec 1.5.

## 4. The Engine (`FireEngine`)

Pure Kotlin, `domain/` layer. Input: a `FireModel` (assembled from the entities above).
Output: a `FireResult`. Deterministic; heavily unit-tested.

### 4.1 Core loop

Month-by-month simulation from `simStartDate` over a horizon (default 360 months):

```
corpus = startCorpus
for month in 0..horizon:
    income      = sum(active income streams this month, with annual growth applied)
    outflows    = sum(active recurring outflows) + sum(active EMIs)
    investable  = income - outflows                       // may be negative
    corpus      = corpus * (1 + monthlyRate) + investable // monthlyRate = (1+cagr)^(1/12)-1
    apply one-time events dated in this month (+inflow / -outflow on corpus)
    record trajectory point (date, corpus, investable, netWorth)
    if liquidCorpus >= targetCorpus and fireMonth == null: fireMonth = month
```

`targetCorpus` = `annualRetirementExpenses × 25` (or `explicitTargetCorpus`).
`monthlyRate` derived from `equityCagrPct`.

### 4.2 FIRE date

`fireDate` = `simStartDate + fireMonth` (first month corpus ≥ target). If never reached
within horizon, `fireDate = null` and the **Reframe** (§4.6) explains the gap.

### 4.3 Net worth at a date

`netWorth(date)` = compoundable corpus + non-corpus assets (EPF etc., grown at their own
or default rate) + property `appreciatedValueAtTarget` − outstanding loan balances at
that date. Used for the "Projected net worth — Dec 2031" card.

### 4.4 Loan amortization (v1 simplification)

For each `Liability`: before `emiStartDate`, **no EMI** is charged to cashflow (models
the EMI holiday simply). From `emiStartDate`, a standard amortization schedule runs on
`principalAtEmiStart` at `annualRatePct` over `tenureMonths`, producing the monthly EMI
and the declining outstanding balance (used by §4.3).

> v1 does **not** model construction-linked disbursement or interest capitalization
> during the holiday. The user enters `principalAtEmiStart` (post-capitalization). This
> matches what the War Room HTML displays (EMI from ~Jul 2028, ₹1,76,373) and is flagged
> in-app as an assumption.

### 4.5 Phases

Phases are presentation groupings of the trajectory. If the user defines
`PhaseBoundary` rows, use them. Otherwise auto-derive boundaries at points where the
monthly investable changes materially (income step, EMI start, recurring outflow
ending). Each phase summary reports: date range, representative monthly investable,
corpus at phase end.

### 4.6 The Reframe (first-class output)

This is what makes it a War Room, not a calculator. The engine emits a `Reframe`:

- `targetReachableByTargetDate: Boolean`
- if reachable: `fireDate`, `slackMonths`.
- if **not** reachable by `targetDate`:
  - `corpusAtTargetDate`, `gapToTarget`, `percentOfTarget`
  - `requiredExtraMonthlyInvestment` to close the gap by `targetDate`
  - `achievableTargetByTargetDate` (what corpus IS reachable → implied SWR income)
  - `dateTargetIsReachable` (extend-the-timeline option)
- A small set of generated **reframe options** mirroring the HTML's A/B/C:
  - **A — Extend the date:** FIRE later (year the current target becomes reachable).
  - **B — Lower the target:** the corpus actually reachable by `targetDate` + its 4% SWR monthly income.
  - **C — Add an income stream:** the monthly business/extra income from a date that closes the gap (computed, generic — not hardcoded to TransmuteLabs).

### 4.7 Marginal-impact helpers (used by UI now, by Spec 1.5 fully)

- `fireDaysImpact(amount, atDate)`: recompute `fireDate` with `corpus` reduced by
  `amount` from `atDate`; return the day delta. Used for the live FIRE-day chip on
  expenses in Spec 1; the full purchase-decision screen is Spec 1.5.

### 4.8 Result shape

```
FireResult(
  fireDate: LocalDate?,
  targetCorpus: Money,
  trajectory: List<TrajectoryPoint>,        // monthly
  phases: List<PhaseSummary>,
  netWorthAtTarget: NetWorthBreakdown,
  reframe: Reframe,
  progressPercent: Double
)
```

## 5. UI — War Room Screens (Compose, existing dark fintech theme)

Live cards mirroring the HTML sections. Each recomputes from `FireResult` whenever the
model or a confirmed expense changes (StateFlow from a `FireRepository`).

1. **Current position** — net worth, investable corpus, target, progress bar + gap.
2. **Income architecture** — current vs upcoming streams.
3. **Property / loan** — flat all-in cost, funding, SBI loan terms, payment summary.
4. **Phase cashflow** — N phase cards, each with per-phase investable breakdown.
5. **Corpus trajectory** — line chart of `trajectory` (reuse existing chart libs / simple Compose chart). Scenario toggles deferred to 1.5.
6. **Net worth at FIRE date** — breakdown + SWR figures + the "EMI at FIRE" warning if debt service > SWR income.
7. **Honest reframe** — renders `Reframe`: gap math + Options A/B/C cards.
8. **Actions** — a simple manual checklist (static list + checkbox state in DataStore).

Plus:
- **Edit Model** flow — structured forms (one screen/section per entity group:
  Profile, Assets, Income, Liabilities, Outflows, One-time events, Property). Validated
  inputs; save → engine recomputes. **This is the Spec-2 confirm surface.**
- **Empty state** — first launch with no model shows a guided "set up your model" path
  into the Edit Model flow.

## 6. Branding

- Display name (`app_name`) → **FIRE OS**; launcher label, app bar title, tagline.
- Theme unchanged (existing dark fintech palette).
- Package `com.expenseai`, DB names, settings keys unchanged.

## 7. Testing Strategy

- **Engine unit tests (JVM, the bulk):**
  - Constant-investable case matches closed-form FV+FV_SIP.
  - Multi-phase case: income step, EMI start, outflow ending, one-time outflow each move
    the FIRE date in the expected direction and magnitude.
  - Reframe: target-not-reachable path produces correct gap and a positive
    `requiredExtraMonthlyInvestment`; reachable path produces a date.
  - Loan amortization: EMI matches standard formula; outstanding reaches ~0 at tenure end.
  - Reproduce the War Room's headline figures within tolerance from a model encoding the
    same inputs (Phase-1 ₹5.11L investable, corpus ~₹2.57Cr at Jun 2028, base ~₹5.63Cr at
    Dec 2031, ~26.8% of ₹30Cr) — as a regression guard, **entered as test fixtures, not
    shipped as app defaults.**
- **Migration test:** v(n)→v(n+1) adds the new tables without data loss (instrumented).
- **ViewModel tests:** model change → recomputed `FireResult` surfaces to UI state.

## 8. Build Sequencing (for the implementation plan)

1. Data model: entities, DAOs, DB migration, `FireRepository` + `FireModel` assembly.
2. `FireEngine`: core loop, loan amortization, net worth, reframe + unit tests (TDD).
3. Read-only War Room dashboard rendering a `FireResult` (prove the engine end-to-end).
4. Edit Model forms (write path) + empty state + recompute wiring.
5. Branding pass (display name / tagline).

## 9. Cost Posture (prototype)

This is a prototype; minimize standing cost and complexity.

- **Spec 1 is zero marginal cost** — fully on-device (Room/SQLCipher, pure-Kotlin engine,
  on-device Gemma already bundled). No cloud services, no backend, no subscriptions.
- **Deliberately avoided** (vs. the original FIRE OS plan): Supabase, FastAPI, n8n,
  ChromaDB/pgvector, hosted realtime. None are needed for the engine + War Room.
- **Forward note for Spec 2 (cloud extraction):** keep it cheap —
  - Default to the **cheapest capable model** (Claude Haiku) for extraction; escalate to
    Sonnet only per-document when Haiku's confidence/quality is insufficient.
  - Send **only the necessary pages**, not whole multi-MB PDFs; use prompt caching for
    repeated schema/instructions.
  - No always-on server: if a key-holding proxy is required, use a **free-tier
    serverless function** (e.g. Cloudflare Workers / Vercel free) invoked on demand,
    not a standing host.
  - On-device path stays the **default and free**; cloud is opt-in per document.

## 10. Open Assumptions (documented, not blocking)

- All amounts stored in a single base unit (paise/rupees) via a `Money` type to avoid
  float drift; display formats to ₹ Lakh/Crore (`en_IN`).
- Non-corpus asset growth uses a single default rate unless per-asset rate is provided.
- "Lifestyle" recurring outflow is the bucket that confirmed expenses adjust; exact live
  linkage rule (replace vs. add) finalized during implementation.
