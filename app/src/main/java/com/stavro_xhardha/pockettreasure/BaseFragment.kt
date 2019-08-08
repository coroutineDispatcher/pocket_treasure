package com.stavro_xhardha.pockettreasure

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.stavro_xhardha.PocketTreasureApplication

abstract class BaseFragment : Fragment() {
    protected val component = PocketTreasureApplication.getPocketTreasureComponent()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        performDi()
        initializeComponents()
        observeTheLiveData()
    }

    abstract fun initializeComponents()

    abstract fun performDi()

    abstract fun observeTheLiveData()
}