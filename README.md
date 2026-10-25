# Koup OS — Starter Project 0.1

This is a first Android Studio source project for a lightweight custom home screen designed for an Android 8.1 car head unit.

## Current prototype
- Dark minimalist landscape home screen
- Koup branding and red accent
- Clock and date
- D-Play launch attempt by app label
- Quick access for navigation, music, phone, and installed apps
- Includes Android HOME intent so it can be selected as a launcher

## Important limitations
- This is source code, not a compiled APK.
- The D-Play button searches visible app labels for D-Play / DPlay / DiPlay. If your installed app has a different name, use Apps for now; we can add its exact package name after checking it on the head unit.
- The temperature display is intentionally omitted until we determine a reliable source. Android apps generally cannot read the car's external temperature sensor unless the head unit exposes it.
- It does not replace Android or change MCU firmware.
- Keep your original launcher installed until this is tested successfully.

## Build
1. Install Android Studio on a computer.
2. Open this folder as a project.
3. Allow Gradle sync to complete. Android SDK Platform 33 and a compatible JDK (usually JDK 11 for this Gradle setup) may be requested.
4. Build > Build APK(s).
5. Copy the APK to the head unit and install it. You may need to enable installation from the file manager/source you use.
6. Press Home and select Koup OS. Choose “Just once” for the first test. Do not choose “Always” until you have verified it works.

## First compatibility check
Before we set it permanently, test:
- Screen fits the full display in landscape
- Touch targets are comfortable
- D-Play opens
- Returning Home brings back Koup OS
- Radio, Bluetooth, reverse camera, steering-wheel controls and sleep/wake behaviour still work

If D-Play does not open from the main button, send the exact app name shown in Apps or its package name and we can wire it directly.
