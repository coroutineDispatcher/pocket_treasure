package com.stavro_xhardha.pockettreasure.background

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.stavro_xhardha.PocketTreasureApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MidnightScheduler : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        //it's midnight
        val offlinePrayerScheduler = PocketTreasureApplication.getPocketTreasureComponent().offlineScheduler
        GlobalScope.launch(Dispatchers.IO) {
            offlinePrayerScheduler.initScheduler()
        }
    }
}