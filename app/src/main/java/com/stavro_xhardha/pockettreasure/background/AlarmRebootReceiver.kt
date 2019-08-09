package com.stavro_xhardha.pockettreasure.background

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.stavro_xhardha.PocketTreasureApplication
import com.stavro_xhardha.pockettreasure.brain.APPLICATION_TAG
import com.stavro_xhardha.pockettreasure.brain.DATA_ARE_READY
import com.stavro_xhardha.pockettreasure.brain.isDebugMode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AlarmRebootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action.equals("android.intent.action.BOOT_COMPLETED")) {
            val offlinePrayerScheduler = PocketTreasureApplication.getPocketTreasureComponent().offlineScheduler
            val rocket = PocketTreasureApplication.getPocketTreasureComponent().getSharedPreferences
            GlobalScope.launch(Dispatchers.IO) {
                if (rocket.readBoolean(DATA_ARE_READY)) {
                    offlinePrayerScheduler.initScheduler()
                    if (isDebugMode)
                        Log.d(APPLICATION_TAG, "WorksAfter Reboot")
                }
            }
        }
    }
}