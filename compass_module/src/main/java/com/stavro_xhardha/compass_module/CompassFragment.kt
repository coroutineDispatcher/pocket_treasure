package com.stavro_xhardha.compass_module

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import com.stavro_xhardha.core_module.brain.BaseFragment
import com.stavro_xhardha.core_module.brain.viewModel
import com.stavro_xhardha.core_module.dependency_injection.CoreApplication
import edu.arbelkilani.compass.CompassListener
import kotlinx.android.synthetic.main.fragment_compass.*

class CompassFragment : BaseFragment(), CompassListener {

    private val compassViewModel by viewModel {
        val coreComponent = CoreApplication.getCoreComponent()
        DaggerCompassComponent.factory().create(coreComponent).compassViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_compass, container, false)
    }

    override fun initializeComponents() {
        qibla_compass.setListener(this)
    }

    override fun observeTheLiveData() {
        compassViewModel.rotateAnimation.observe(viewLifecycleOwner, Observer {
            qibla_compass.startAnimation(it)
        })

        compassViewModel.qiblaFound.observe(viewLifecycleOwner, Observer {
            if (it) Toast.makeText(requireActivity(), R.string.found, Toast.LENGTH_LONG).show()
        })
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
    }

    override fun onSensorChanged(sensorEvent: SensorEvent?) {
        compassViewModel.observeValues(sensorEvent)
    }
}
