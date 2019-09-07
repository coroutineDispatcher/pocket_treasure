package com.stavro_xhardha.pockettreasure

import androidx.fragment.app.Fragment
import com.stavro_xhardha.PocketTreasureApplication

abstract class BaseFragment : Fragment() {
    protected val component = PocketTreasureApplication.getPocketTreasureComponent()
}