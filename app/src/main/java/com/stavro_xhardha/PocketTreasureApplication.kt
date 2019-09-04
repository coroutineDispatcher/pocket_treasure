package com.stavro_xhardha

import android.app.Application
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import com.stavro_xhardha.pockettreasure.dependency_injection.DaggerPocketTreasureComponent
import com.stavro_xhardha.pockettreasure.dependency_injection.PocketTreasureComponent
import net.danlew.android.joda.JodaTimeAndroid


class PocketTreasureApplication : Application() {
    private lateinit var pocketTreasureComponent: PocketTreasureComponent

    override fun onCreate() {
        super.onCreate()
        JodaTimeAndroid.init(this)
        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)
        if (AppCompatDelegate.getDefaultNightMode() == MODE_NIGHT_YES) {
            Log.d("YES", "YES")
        }
        pocketTreasureComponent = DaggerPocketTreasureComponent.factory().create(this)
        INSTANCE = pocketTreasureComponent
    }

    companion object {
        private var INSTANCE: PocketTreasureComponent? = null

        @JvmStatic
        fun getPocketTreasureComponent(): PocketTreasureComponent = INSTANCE!!
    }
}