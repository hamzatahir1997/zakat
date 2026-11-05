# RUN-LOCALLY GUIDE — see the app on your computer (no APK needed)

You do **not** need to build an APK to see the app working. A free program called
**Android Studio** can show a **fake phone inside a window on your laptop** (this
is called an "emulator"), run the app instantly from the source code, and update
it in seconds whenever you change something. When you're happy with your edits,
push them to GitHub and your APK gets rebuilt automatically, like before.

**Honest numbers before you start:** Android Studio + the Android kit for this
project is a roughly **10 GB** download/install. The first setup takes around
30–45 minutes of mostly waiting. After that, "edit → see it run" takes only
seconds. If your laptop is old or has under 8 GB of RAM, the emulator may be
slow but still usable — or just skip this guide and keep installing APKs from
GitHub (Option B at the bottom).

---

## Step 1 — Install Android Studio (one time, ~30–45 min)

1. Go to **https://developer.android.com/studio**
2. Click the big green **Download Android Studio** button, accept terms, save the
   `.exe` installer (about 1.2 GB).
3. Run the installer. Keep clicking **Next** (Default/Standard settings are fine
   all the way). When asked about components, leave everything ticked.
4. When it finishes, Android Studio opens and shows a Wizard:
   - Choose **Standard** setup → **Next** → **Accept** the licenses.
   - It now downloads the "Android SDK" (a few GB). Let it finish.

If Windows shows a scary warning at the end about "HAXM" or "Windows Hypervisor
Platform", see the **If the emulator refuses to start** section below.

---

## Step 2 — Open the Zakat Tracker project

1. In Android Studio, click **Open** (the welcome window's big button).
2. Browse to and select the folder **`W:\xampp\htdocs\projects\zakat`**
   (select the folder itself, not a file) → **OK**.
3. If asked "Trust and Open project?" → **Trust Project**.
4. Now the **Gradle sync** starts (the bar at the bottom fills up).
   - This downloads the build tools & libraries (one time, 5–20 minutes
     depending on your internet). This is normal: "Gradle sync in progress…" is
     GitHub's equivalent — your code is fine, it's just fetching parts.
   - If it asks about "Gradle wrapper" anything → accept defaults; the wrapper
     is already included in the project (gradlew + gradle/wrapper).
5. Done when the bottom bar says **"BUILD SUCCESSFUL"** or the project tree
   (left side) shows `app` with all its folders without red squiggles.

---

## Step 3 — Make a fake phone (one time)

1. In the right-hand toolbar (or Tools menu), click **Device Manager**
   (phone-tablet icon).
2. Click **Create virtual device** (the **+**).
3. Pick a phone model — e.g. **Pixel 6** — press **Next**.
4. Choose a system image — **API 34 (UpsideDownCake)** is safest; if it has a
   ⬇ download arrow, click it and let the ~1.2 GB image download.
5. Press **Next → Finish**. Occasionally a first-run popup asks to let the
   emulator use more RAM — accept.

---

## Step 4 — RUN the app

1. Top toolbar: make sure the device dropdown next to the ▶ button shows the
   Pixel device you created.
2. Click the green **Run ▶** button.
3. The emulator window boots (30–90 seconds the first time), unlocks itself,
   and **Zakat Tracker** appears — with Remaining Rs. 11,024 (green), Total
   Rs. 151,169, Used Rs. 140,145 and the three old donations.
4. Use the mouse as your finger: click the **Donate** button, try amounts,
   delete an entry, use the ✏️ pencil. It behaves exactly like it does on a
   real phone.

To try dark mode: in the emulator, pull down the quick-settings shade from the
top-right of the fake phone and tap **Dark theme**. To try rotation: click one
of the **rotate** buttons in the bar beside the emulator window.

---

## Step 5 — Edit code, see it change, and ship changes to your phone

- Change anything in the Kotlin code — e.g. in
  `app/src/main/java/com/example/zakattracker/ui/ZakatScreen.kt` change the
  "Donate" text to "Add Zakat" — then press **Run ▶** again. The emulator
  reinstalls in seconds and shows the change.
- Yellow warnings about unused things are harmless. Red errors stop the run;
  undo your change (Ctrl+Z) if you didn't mean it, and Run again.

**When your edits look good**, get them onto your phone by rebuilding the APK:

1. If *you* (or whoever is helping you) manage the repo: just commit & push
   like before — GitHub rebuilds an APK automatically and you download the new
   artifact exactly as you did earlier.
2. Or ask your AI/code helper to make the commit for you.

Note: the emulator keeps its **own separate data**. Deleting the emulator
(waste-bin icon in Device Manager) or wiping it does not touch your phone's
data.

---

## If the emulator refuses to start

Windows emulators need "virtualization" switched on. Fix in two minutes:

1. Start menu → search **"Turn Windows features on or off"** → open it.
2. Tick **Windows Hypervisor Platform** (and, if listed and ticking is
   possible, **Hyper-V**) → OK → **Restart the PC**.
3. If it still complains, restart into your BIOS/UEFI (usually F2/Del/F10 while
   booting, varies by laptop) → find an option named **Virtualization / Intel
   VT-x / AMD SVM** → set it to **Enabled** → Save & Exit.
4. Open Android Studio again and press Run.

---

## Option B — skip the laptop entirely

If you'd rather not install Android Studio: your phone **is** the preview
device. Keep editing nothing by hand; request changes (from me or whoever edits
the code), let them push, then install the newest `app-debug.apk` artifact from
https://github.com/hamzatahir1997/zakat/actions. You lose instant edit-preview,
but gain zero setup on your PC.
