# 🔋 BSC (Battery Stats Changer)
[![GitHub license](https://img.shields.io/github/license/superisuer/battery-stats-changer)](https://github.com/superisuer/battery-stats-changer/blob/main/LICENSE)
[![Android](https://img.shields.io/badge/Android-10+-green)](https://developer.android.com)

This is an open-source application for visually changing battery data in the Android system.
Created with [Sketchware Pro](https://sketchware.pro).
## How does this work??!
Through Root or Shizuku, dumpsys/cmd commands are executed, which can change some battery data.
## What can it do
- Parse whether OnePlus' custom dumpsys is being used
- Change battery percentage (the ability to set negative values ​​and values >100 depends on the ROM)
- Change the capacity in mAh
- Change the temperature (some firmware may turn off the phone at certain values)
- Change USB connection status
## How to reset it
There are several ways to do this:
1. Execute in shell `dumpsys battery reset`.
2. Press "Reset" button in my app.
3. Reboot your device.
## Building
Since this app was made in Sketchware Pro, I recommend compiling it in Sketchware Pro. You can find the SWB file for Sketchware Pro in the Releases tab.

For Android Studio, simply clone the repo, open it there, and build.
