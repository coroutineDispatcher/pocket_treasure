package com.stavro_xhardha.core_module.dependency_injection

import android.app.Application
import net.danlew.android.joda.JodaTimeAndroid


class CoreApplication : Application() {

    private lateinit var coreComponent: CoreComponent

    override fun onCreate() {
        super.onCreate()
        JodaTimeAndroid.init(this)
        coreComponent = DaggerCoreComponent.factory().create(this)
        INSTANCE = coreComponent
    }

    companion object {
        private var INSTANCE: CoreComponent? = null

        @JvmStatic
        fun getCoreComponent(): CoreComponent = INSTANCE!!
    }
}