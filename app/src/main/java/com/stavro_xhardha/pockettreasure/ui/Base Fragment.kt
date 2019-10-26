package com.stavro_xhardha.pockettreasure.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.stavro_xhardha.PocketTreasureApplication

abstract class BaseFragment : Fragment() {

    val applicationComponent = PocketTreasureApplication.getPocketTreasureComponent()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initializeComponents()
        observeTheLiveData()
    }

    abstract fun observeTheLiveData()

    abstract fun initializeComponents()
}