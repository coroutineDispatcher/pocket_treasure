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
import edu.arbelkilani.compass.Compass
import edu.arbelkilani.compass.CompassListener
import kotlinx.android.synthetic.main.fragment_compass.*

class CompassFragment : BaseFragment() {

    private var qiblaCompass: Compass? = null

    private val compassViewModel by viewModel {
        DaggerCompassComponent.factory().create(applicationComponent).compassViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_compass, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        qiblaCompass = view.findViewById(R.id.qibla_compass)
    }

    override fun initializeComponents() {
        qiblaCompass?.setListener(object : CompassListener {
            override fun onSensorChanged(sensorEvent: SensorEvent?) {
                compassViewModel.observeValues(sensorEvent)
            }

            override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
            }
        })
    }

    override fun observeTheLiveData() {
        compassViewModel.rotateAnimation.observe(viewLifecycleOwner, Observer {
            qiblaCompass?.startAnimation(it)
        })

        compassViewModel.qiblaFound.observe(viewLifecycleOwner, Observer { event ->
            event.getContentIfNotHandled()?.let {
                Toast.makeText(requireActivity(), event.peekContent(), Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onDestroyView() {
        qiblaCompass?.setListener(null)
        qiblaCompass = null
        super.onDestroyView()
    }
}