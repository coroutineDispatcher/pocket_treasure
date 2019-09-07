package com.stavro_xhardha.pockettreasure.ui.setup

import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.afollestad.materialdialogs.MaterialDialog
import com.google.android.gms.location.LocationResult
import com.stavro_xhardha.pockettreasure.BaseFragment
import com.stavro_xhardha.pockettreasure.R
import com.stavro_xhardha.pockettreasure.brain.LocationTracker
import com.stavro_xhardha.pockettreasure.brain.LocationTrackerListener
import com.stavro_xhardha.pockettreasure.brain.viewModel
import com.stavro_xhardha.pockettreasure.ui.SharedViewModel
import java.util.*

class SetupFragment : BaseFragment(), LocationTrackerListener {


    private val setupViewModel by viewModel {
        component.setupViewModelFactory.create(it)
    }

    private lateinit var sharedViewModel: SharedViewModel

    private val locationTracker by lazy {
        //todo singleton
        LocationTracker(requireActivity(), this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_setup, container, false)
    }

    private fun askForNotifyingUser() {
        MaterialDialog(activity!!).show {
            title(R.string.app_name)
            message(R.string.do_you_want_to_get_notified)
            positiveButton(text = activity!!.resources.getString(R.string.yes)) {
                setupViewModel.updateNotificationFlags()
                //todo pop the backstack
                it.dismiss()
            }
            cancelable(false)
            negativeButton(text = activity!!.resources.getString(R.string.no)) {
                //todo pop the backstack
                it.dismiss()
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initializeComponents()
        observeTheLiveData()
    }

    fun initializeComponents() {
        sharedViewModel = requireActivity().run {
            ViewModelProviders.of(this).get(SharedViewModel::class.java)
        }
        locationTracker.startLocationRequestProcess()
    }

    fun observeTheLiveData() {
        setupViewModel.locationRequestTurnOff.observe(this, Observer {
            if (it) locationTracker.removeLocationRequest()
        })
        setupViewModel.locationErrorVisibility.observe(this, Observer {
            if (it) {
                Toast.makeText(requireActivity(), R.string.invalid_coorinates, Toast.LENGTH_LONG)
                    .show()
                requireActivity().finish()
            }
        })
        setupViewModel.serviceNotAvailableVisibility.observe(this, Observer {
            if (it) {
                Toast.makeText(requireActivity(), R.string.service_not_available, Toast.LENGTH_LONG)
                    .show()
                requireActivity().finish()
            }
        })
        setupViewModel.prayerNotificationDialogViaibility.observe(this, Observer {
            if (it) askForNotifyingUser()
        })
        sharedViewModel.onGpsOpened.observe(this, Observer {
            if (it) locationTracker.updateLocation()
        })
        sharedViewModel.onLocationPermissiongranted.observe(this, Observer {
            if (it) locationTracker.getUserLocation()
        })
    }

    override fun onLocationError() {
        requireActivity().finish()
    }

    override fun onLocationResult(locationResult: LocationResult) {
        for (location in locationResult.locations) {
            val geocoder = Geocoder(requireActivity(), Locale.getDefault())
            setupViewModel.convertToAdress(geocoder, location.latitude, location.longitude)
        }
    }
}