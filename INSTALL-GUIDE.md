# INSTALL GUIDE — Zakat Tracker

This guide Turn your code into a working app on your Android phone. You do **not**
need to install Android Studio, Gradle, or anything else on your PC. GitHub's computers
do the hard work for you, for free.

**The whole process takes about 20–30 minutes, and almost all of it is waiting.**

---

## The big picture

1. You create a free GitHub account (if you don't have one).
2. You create a "repository" (a folder in the cloud) and upload the project files.
3. GitHub automatically builds an APK file for you and puts it in an "Artifacts" box.
4. You download the APK and install it on your phone.

That's it. GitHub is doing something that normally needs a whole development setup —
but you never see any of it.

---

## PART 1 — Create a GitHub account (skip if you already have one)

1. Open your web browser (Chrome, Edge, Firefox — anything) and go to **https://github.com**
2. Click **Sign up** (top-right corner).
3. Enter your email address, create a password, and pick a username.
   *(The username doesn't matter. Any name is fine, e.g. `hamza-dev-2026`.)*
4. GitHub shows a puzzle to prove you're human. Solve it.
5. GitHub sends you a verification code to your email. Open the email, copy the code,
   paste it back on the GitHub page.
6. GitHub will ask a few setup questions ("How much programming experience…").
   You can leave them as they are and keep clicking **Continue** / **Skip** until
   you land on the GitHub home page.

Done. You now have a free account.

---

## PART 2 — Create the repository

A "repository" is just a folder that lives on GitHub's servers.

1. While logged in to GitHub, click the **+** icon (top-right corner) and choose
   **New repository**.
2. Fill in exactly this:
   - **Repository name:** `zakat-tracker` (no spaces — this is safest)
   - **Description (optional):** anything you like
   - **Private / Public:** either works. **Private** is fine and free.
     *(Leave it Private if you want the numbers to stay just on your account.)*
3. **IMPORTANT:** Leave every checkbox **UNchecked**:
   - ❌ Do NOT tick "Add a README file"
   - ❌ Do NOT tick "Add .gitignore"
   - ❌ Do NOT tick "Choose a license"
4. Click the green **Create repository** button.
5. You now see an empty repository page with setup instructions. Among the offers
   GitHub makes, find and click the link that says **"uploading an existing file"**
   (it's a small link inside the "Quick setup" section).

You are now on the file-upload page. On to Part 3.

---

## PART 3 — Upload the project files

Your project files are in the folder `W:\xampp\htdocs\projects\zakat`
(this folder was created for you; it contains `app\`, `.github\`, `README.md`,
`INSTALL-GUIDE.md` and a few `.gradle.kts` files).

**The critical detail:** GitHub's upload area must receive the files that are
*inside* your project folder — with the correct internal structure (`.github\workflows\...`,
`app\src\...`). Dragging every file individually would be painful, so do this:

1. On the GitHub upload page, you'll see a dashed rectangle that says
   **"Choose your files or drag them here"**.
2. Open **Windows File Explorer** and navigate to `W:\xampp\htdocs\projects\zakat`.
3. Click once in the empty part of the folder window, then press **Ctrl + A**
   to select EVERYTHING inside the folder (all files and all subfolders, including `.github`).
4. Now drag the whole selection onto the dashed rectangle in your browser.
5. GitHub starts uploading. Each item appears with a progress bar. Small files finish
   in a second; be patient until every progress bar has disappeared.
6. When the uploads are done, scroll down to the box named **"Commit changes"**.
   - The text field there already says something like "Add files via upload".
     Leave it as it is.
7. Click the green **Commit changes** button.

**Check you did it right:** the repository's main page should NOW show this list of
files and folders (plus or minus order):

- `.github` (folder)
- `app` (folder)
- `app/build.gradle.kts`
- `.gitignore`
- `build.gradle.kts`
- `gradle.properties`
- `INSTALL-GUIDE.md`
- `README.md`
- `settings.gradle.kts`

If you see a folder named `zakat-tracker` INSIDE the list (a folder whose name is the
same as the project folder), you dragged the folder itself instead of its contents.
Fix it: manually create the missing files with "Create new file" and cancel the repo, or
easiest: delete the repository (Settings ⚙ → Danger Zone → Delete this repository) and
restart from Part 2 with the correct drag.

**The most important single check:** click into the `.github` folder → it must contain
`workflows` → and inside that, a file named `build.yml`.
GitHub will not build anything without this exact path: `.github/workflows/build.yml`.

---

## PART 4 — Let GitHub build the APK

Good news: uploading the files already *started* the build automatically. If it didn't,
you can trigger it by hand. Both routes below end at the same place.

1. Click the **Actions** tab at the top of the repository (the icon with a small clock/arrow).
   - If you see a yellow banner saying something like
     *"Workflows aren't being run on this forked repository"* or *"Enable workflow"*,
     click the **Enable** / **"I understand my workflows, go ahead and enable them"** button.
2. In the left sidebar you should see a workflow named **"Build Zakat Tracker APK"**.
   Click it.
3. You should see a running job (a yellow circle, file name "Build Zakat Tracker APK",
   started "a few seconds/minutes ago").
   If there's NO run at all:
   - Click **"Run workflow"** (right side, next to the workflow name).
   - A small panel opens. Leave everything unchanged. Click the green
     **Run workflow** button.
   - The build starts.
4. Wait. Click the running entry and you'll see it work through steps like
   "Get the code", "Set up Java", "Set up Android SDK", "Build the APK".
   **A normal build takes roughly 6–10 minutes.**
   A yellow spinning circle = working. A green checkmark ✅ = success.
5. Your internet will not be used by the build other than to run it. It's genuinely
   free — GitHub gives public repos free unlimited build minutes, and even Private
   accounts get generous free minutes (you'll use less than a tenth of your free
   monthly allowance for one build).

If it finishes with a red ❌ instead, see **Troubleshooting** at the end.

---

## PART 5 — Download your APK

1. Click the **Actions** tab → click the (now green ✅) run named
   "Build Zakat Tracker APK".
2. Scroll to the bottom of the page, to the box titled **"Artifacts"**.
   There is ONE file there: **ZakatTracker-APK**.
3. Click **ZakatTracker-APK** to download it.
   It downloads as a compressed `ZakatTracker-APK.zip` (this is normal).

Now you have `app-debug.apk` inside that zip, and you need it on your phone.
Two good routes:

### Route 1 (easiest) — do it all on your phone

1. Open the repository in your **phone's** web browser (Chrome) by going to
   `github.com` and logging in.
2. Go to: your repo → **Actions** → the green ✅ run → **Artifacts** → tap
   **ZakatTracker-APK**.
3. Your phone downloads the zip file. This will live in your phone's
   `Downloads` folder. (Chrome may warn "this file may be dangerous" — choose
   **Download anyway**; normal for APKs.)
4. Open your **Files** app (may be called "Files", "My Files", "File Manager"...).
   Navigate to **Downloads**.
5. Tap `ZakatTracker-APK.zip` → choose **Extract**.
   (If your Files app can't extract, any free zip app like RAR can do it, but most
   phones can do it natively.)
6. Inside the extracted folder you'll find `app-debug.apk`.
   Continue to PART 6 to install.

### Route 2 — move it from your computer with a USB cable

1. On your PC, find the downloaded `ZakatTracker-APK.zip` (usually `Downloads` folder);
   right-click → **Extract All** → open the extracted folder → you see `app-debug.apk`.
2. Connect your phone to the computer with a USB cable.
3. On your phone, a notification appears about the USB connection.
   Tap it and change the mode to **"File transfer / MTP"** (wording varies by phone).
4. On your PC, **File Explorer** now shows the phone under "This PC" →
   "Your phone's name → Internal storage".
5. Copy `app-debug.apk` into the phone's **Downloads** folder.
6. Continue to PART 6.

### Route 3 (no cable) — Google Drive

1. Upload the extracted `app-debug.apk` from your PC to **drive.google.com**.
2. Open the **Drive** app on your phone and download it from there.
3. Continue to PART 6.

---

## PART 6 — Install the APK on your phone

Because the app comes from outside the Play Store, Android asks one (small) thing first:

1. In your Files app, tap **app-debug.apk**.
2. Android says: *"For your security, your phone is not allowed to install unknown
   apps from this source"* with a button called **Settings** (or "Allow from this source").
   Tap it.
3. Flip ON the switch: **"Allow from this source"**.
   *(This only allows THIS one file manager to install apps; if you ever end up on the
   "Install unknown apps" page, find the app you used — Files / Chrome / Drive —
   and allow it.)*
4. Go back (Back button / X) and tap **"Install"**.
5. Google Play Protect may appear: tap **"Install anyway"** (it says "scan first" —
   either choice is fine).
6. Wait for "App installed" → tap **Open**.

You may see your phone screen transform briefly while the app initially loads and
seeds its starting data (about 1 second).

---

## PART 7 — First open checklist

Open "Zakat Tracker" from your home screen. It should show:

- **Remaining: Rs. 11,024** (big green card)
- Total Zakat Money: Rs. 151,169 (with a small pencil ✏️ to change it)
- Used: Rs. 140,145
- A big green **Donate** button at the bottom
- Three past donations totalling Rs. 140,145 listed underneath

Tap a few things. Donate something and the numbers update immediately. Delete a
donation and it subtracts from Used. Everything you see is saved on the phone
automatically — you can reboot the phone and reopen the app days later;
all numbers are still there.

---

## Troubleshooting

| What you see | What it means | What to do |
|---|---|---|
| Actions runs started, then ❌ red check | The build hit a problem | Click it, click **Re-run all jobs**. Transient internet errors retry. If it fails 3× in a row the same way, check the upload in Part 3 happened for ALL files, especially `.github/workflows/build.yml`. |
| "Enable workflow" yellow banner | New account flow to authorize Actions | Click it, then re-run the workflow. |
| No Actions tab | You may have no commits | Failing that, use "Run workflow" option in Part 4. |
| Build finished but no Artifacts visible | You're not at the right place | Click directly on the specific completed run (green ✅ line), not the main Actions list. |
| Nothing happens when dragging files | Browser blocked it | Instead click into the dashed area to use the file-picker, navigate into the folder, Ctrl+A, Open. GitHub keeps folder structure for picked files too (use "Choose files" — it accepts multi-select). |
| GitHub rejects the upload silently | Folder was dragged with ZERO items inside? | Walk through Part 3 again slowly, make sure you dragged the CONTENTS (Ctrl+A inside the folder). |
| Install blocked "Play Protect" | Standard for non-Play apps | Tap "More details" → "Install anyway". |
| Your phone lacks Google's latest Android | Older phones are fine — this app targets Android 7.0+ | Nothing to do, it should just work. |

---

## Uninstall / reinstall

Uninstalling the app (or Settings → Apps → Zakat Tracker → Clear data) **erases your
ledger** and returns the app to its starting numbers on next install. The leaked
holdings are NOT the app's job — treat it like an installed pocket calculator.
Rebooting the phone does **not** delete your data; only uninstalling or clearing
storage does.

---

*Need help with any step? Ask the person (or AI) who gave you this folder —
they can see every file that gets uploaded and can watch the Actions run with you.*
