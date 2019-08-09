package com.stavro_xhardha.pockettreasure.ui.settings


import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.google.android.gms.location.LocationResult
import com.stavro_xhardha.pockettreasure.BaseFragment
import com.stavro_xhardha.pockettreasure.R
import com.stavro_xhardha.pockettreasure.brain.LocationTracker
import com.stavro_xhardha.pockettreasure.brain.LocationTrackerListener
import com.stavro_xhardha.pockettreasure.brain.startWorkManager
import com.stavro_xhardha.pockettreasure.brain.viewModel
import com.stavro_xhardha.pockettreasure.ui.SharedViewModel
import kotlinx.android.synthetic.main.fragment_settings.*
import java.util.*

class SettingsFragment : BaseFragment(), LocationTrackerListener {

    private val settingsViewModel by viewModel { component.settingsViewModelFactory.create() }

    private lateinit var sharedViewModel: SharedViewModel

    private val locationTracker by lazy {
        LocationTracker(requireActivity(), this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                view.findNavController().popBackStack(R.id.homeFragment, false)
            }
        })
    }

    override fun initializeComponents() {
        swFajr.setOnCheckedChangeListener { _, isChecked ->
            settingsViewModel.onSwFajrClick(isChecked)
        }

        swDhuhr.setOnCheckedChangeListener { _, isChecked ->
            settingsViewModel.onSwDhuhrClick(isChecked)
        }

        swAsr.setOnCheckedChangeListener { _, isChecked ->
            settingsViewModel.onSwAsrClick(isChecked)
        }

        swMaghrib.setOnCheckedChangeListener { _, isChecked ->
            settingsViewModel.onSwMaghribClick(isChecked)
        }

        swIsha.setOnCheckedChangeListener { _, isChecked ->
            settingsViewModel.onSwIshaClick(isChecked)
        }

        llLocation.setOnClickListener {
            locationTracker.startLocationRequestProcess()
        }

        sharedViewModel = requireActivity().run {
            ViewModelProviders.of(requireActivity()).get(SharedViewModel::class.java)
        }
    }

    override fun performDi() {

    }

    override fun observeTheLiveData() {
        settingsViewModel.fajrCheck.observe(this, Observer {
            swFajr.isChecked = it
        })
        settingsViewModel.dhuhrCheck.observe(this, Observer {
            swDhuhr.isChecked = it
        })
        settingsViewModel.asrCheck.observe(this, Observer {
            swAsr.isChecked = it
        })
        settingsViewModel.maghribCheck.observe(this, Observer {
            swMaghrib.isChecked = it
        })
        settingsViewModel.ishaCheck.observe(this, Observer {
            swIsha.isChecked = it
        })
        settingsViewModel.countryAndCapital.observe(this, Observer {
            tvCountryAndCapital.text = it
        })

        settingsViewModel.locationRequestTurnOff.observe(this, Observer {
            if (it) locationTracker.removeLocationRequest()
        })
        settingsViewModel.locationerrorVisibility.observe(this, Observer {
            if (it) {
                Toast.makeText(requireActivity(), R.string.invalid_coorinates, Toast.LENGTH_LONG).show()
            }
        })
        settingsViewModel.serviceNotAvailableVisibility.observe(this, Observer {
            if (it) {
                Toast.makeText(requireActivity(), R.string.service_not_available, Toast.LENGTH_LONG).show()
            }
        })
        sharedViewModel.onGpsOpened.observe(this, Observer {
            if (it) locationTracker.updateLocation()
        })
        sharedViewModel.onLocationPermissiongranted.observe(this, Observer {
            if (it) locationTracker.getUserLocation()
        })
        settingsViewModel.workManagerReadyToStart.observe(this, Observer {
            if (it) {
                startWorkManager(requireActivity())
                Toast.makeText(requireActivity(), R.string.location_updated_successfully, Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onLocationError() {
        Toast.makeText(requireActivity(), R.string.values_cannot_be_updated, Toast.LENGTH_LONG).show()
    }

    override fun onLocationResult(locationResult: LocationResult) {
        for (location in locationResult.locations) {
            val geocoder = Geocoder(requireActivity(), Locale.getDefault())
            settingsViewModel.convertToAdress(geocoder, location.latitude, location.longitude)
        }
    }
}