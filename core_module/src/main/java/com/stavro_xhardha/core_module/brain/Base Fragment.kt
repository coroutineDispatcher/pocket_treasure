package com.stavro_xhardha.core_module.brain

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.stavro_xhardha.core_module.dependency_injection.CoreApplication

abstract class BaseFragment : Fragment() {

    val applicationComponent = CoreApplication.getCoreComponent()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initializeComponents()
        observeTheLiveData()
    }

    abstract fun observeTheLiveData()

    abstract fun initializeComponents()
}