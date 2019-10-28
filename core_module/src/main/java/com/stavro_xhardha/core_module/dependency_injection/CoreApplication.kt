package com.stavro_xhardha.core_module.dependency_injection

import android.app.Application


class CoreApplication : Application() {

    private lateinit var coreComponent: CoreComponent

    override fun onCreate() {
        super.onCreate()
        coreComponent = DaggerCoreComponent.factory().create(this)
    }

    companion object {
        private var INSTANCE: CoreComponent? = null

        @JvmStatic
        fun getCoreComponent(): CoreComponent = INSTANCE!!
    }
}