# Zakat Tracker

A simple, private Android app that keeps track of your personal Zakat ledger:

- **Total Zakat Money** — the full pool you've set aside
- **Used** — the money you've spent/donated so far
- **Remaining** — Total − Used, always calculated automatically, never stored

Open a dialog, enter an amount in Rs., confirm, and the ledger updates instantly.
Everything lives **only on your phone** — no account, no internet, no ads, no cloud.

---

## Installing the app

**Do not compile this yourself.** The easiest path to the APK is documented step
by step, in plain English, in:

👉 **[INSTALL-GUIDE.md](INSTALL-GUIDE.md)**

Short version: create a free GitHub account, upload this folder's contents to a new
repository, and GitHub Actions builds the APK in the cloud. You download
`app-debug.apk` from the run's "Artifacts" box and install it on your phone. About
20–30 minutes of mostly waiting.

---

## How to use the app

### Main screen
- **Big card at top: Remaining.** Green normally; turns **red** if your balance
  drops below zero. The number updates automatically when you donate or change
  the total.
- **Two smaller cards:** "Total Zakat Money" and "Used".
  The ✏️ **pencil icon** on the total card lets you change the total at any time.
- **Past Donations list** below: each entry shows the amount plus date/time, and has
  a 🗑️ delete button. Deleting an entry **subtracts it from Used** automatically.
- **Big green Donate button** pinned at the bottom.

### Donating
1. Tap **Donate**.
2. Enter an amount (numeric keypad; decimals allowed, e.g. `1250.50`).
   Invalid input (empty, negative, zero) shows an inline error message.
3. Tap **Confirm**.
   - If the amount is more than your Remaining balance, the app first asks:
     *"This exceeds your remaining balance — continue?"*. You may go below zero
     if you choose to; the Remaining card turns red.
4. Amount is saved, dialog closes, and a toast appears: **"Rs. X donated"**.

### Editing the total
Tap the pencil ✏️ on the "Total Zakat Money" card, enter the new total, and press
Save. Used is untouched; Remaining recalculates itself.

---

## Starting data

The very first time the app opens, it loads:

- **Total Zakat Money: Rs. 151,169**
- **Used: Rs. 140,145** (visible as three pre-loaded donations: Rs. 85,145,
  Rs. 30,000 and Rs. 25,000)
- **Remaining: Rs. 11,024** (calculated: 151,169 − 140,145)

These are seeded once. Afterwards everything you add/change persists.

---

## Where the data is stored (and what to do if you lose it)

- All data lives in the app's **private storage on your phone** (a Room database for
  donations, a DataStore preference for the total).
- **Survives** app restarts, reboots, and OS updates.
- **Erased** by uninstalling the app, or Settings → Apps → Zakat Tracker →
  *Clear data / Clear storage*. After a wipe, the app starts again at the
  pre-loaded numbers above — **your own ledger is gone and cannot be recovered**
  by this app. Treat it like a physical notebook.

### Recovering a lost ledger
If the app was ever wiped and you still know the numbers, you can reconstruct it:

1. Tap the ✏️ pencil and temporarily set the **Total** to an amount large enough
   (e.g. your total + used, or just a big placeholder like 999,999).
2. Tap **Donate** and enter a single donation equal to your **Used** total
   (e.g. 140,145). Extra-donation warnings don't matter — confirm them.
3. Tap the ✏️ pencil again and set the **Total** back to the real figure
   (e.g. 151,169).

Now Used and Remaining both come out exactly right again (the one reconstructed
donation is dated today).

### Going forward
This version intentionally has no export/backup feature. If you want one (a plain
text backup you could copy out), that's a nice-to-have to request in a future
version — the data itself is sitting in a standard Room database with a single
`donations` table.

---

## Technical summary (for anyone curious)

| | |
|---|---|
| Language / UI | Kotlin + Jetpack Compose, Material 3 |
| Architecture | Single Activity + ViewModel + StateFlow |
| Data | Room (donations) + DataStore Preferences (total, seeded flag) |
| Android API | minSdk 24 (Android 7.0), targetSdk 34 |
| Package | `com.example.zakattracker` |
| Modes | Automatic light & dark mode; survives rotation |
| Permissions | None. No internet, no ads, no tracking |

### How the build works
The file `.github/workflows/build.yml` tells GitHub Actions to:
1. check out the code,
2. install Java 17 + Android SDK + Gradle 8.7,
3. run `gradle assembleDebug`,
4. upload `app/build/outputs/apk/debug/app-debug.apk` as an artifact named
   **ZakatTracker-APK**.

Every push re-triggers the build; you can also press "Run workflow" manually.
The `debug` APK is signed with a debug key, which is exactly what you want for a
personal app: phones install it directly, no signing ceremony needed.

### Money is stored safely
Everything is stored as **paisa** (integer math, 1 Rs = 100 paisa), so `1250.50`
and `151,169` stay exact — no floating-point surprises. Display uses `Rs.` and
comma separators (`Rs. 151,169`), showing paise only when needed (`Rs. 1250.50`).

---

## FAQ

**Q: I changed the total and now Remaining is negative/red.**
A: That's allowed and accurate. The red card just warns you that you've budgeted
more donations than money in the pool. Raise the total with the ✏️ pencil to go
back to green.

**Q: Can I have the app on two phones?**
A: Each phone keeps its own separate data. There's no sync.

**Q: Does it cost anything?**
A: No. The app is free, and GitHub's build minutes for one build cost you nothing
(free allowance).

**Q: Can other people see my numbers?**
A: Only if your GitHub repository is Public and they browse your code — the app's
*runtime data* is never uploaded anywhere; it lives only on your phone.

**Q: Rotation / dark mode?**
A: Both handled. The app has light & dark mode auto-detection and won't crash or
reset state when you rotate.
