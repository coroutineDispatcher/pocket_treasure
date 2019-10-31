package com.stavro_xhardha.pockettreasure.ui.settings


import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.google.android.gms.location.LocationResult
import com.stavro_xhardha.core_module.brain.BaseFragment
import com.stavro_xhardha.pockettreasure.R
import com.stavro_xhardha.core_module.background.PrayerTimeWorkManager
import com.stavro_xhardha.core_module.brain.LocationTracker
import com.stavro_xhardha.core_module.brain.LocationTrackerListener
import com.stavro_xhardha.core_module.SharedViewModel
import kotlinx.android.synthetic.main.fragment_settings.*
import java.util.*
import java.util.concurrent.TimeUnit

class SettingsFragment : BaseFragment(),
    LocationTrackerListener {

    private val settingsViewModel by viewModels<SettingsViewModel>()

    private lateinit var sharedViewModel: SharedViewModel

    private var locationTracker: LocationTracker? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
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
            locationTracker =
                LocationTracker(requireActivity(), this)
            locationTracker?.startLocationRequestProcess()
        }

        sharedViewModel = requireActivity().run {
            ViewModelProviders.of(requireActivity()).get(SharedViewModel::class.java)
        }
    }

    override fun observeTheLiveData() {
        settingsViewModel.fajrCheck.observe(viewLifecycleOwner, Observer {
            swFajr.isChecked = it
        })
        settingsViewModel.dhuhrCheck.observe(viewLifecycleOwner, Observer {
            swDhuhr.isChecked = it
        })
        settingsViewModel.asrCheck.observe(viewLifecycleOwner, Observer {
            swAsr.isChecked = it
        })
        settingsViewModel.maghribCheck.observe(viewLifecycleOwner, Observer {
            swMaghrib.isChecked = it
        })
        settingsViewModel.ishaCheck.observe(viewLifecycleOwner, Observer {
            swIsha.isChecked = it
        })
        settingsViewModel.countryAndCapital.observe(viewLifecycleOwner, Observer {
            tvCountryAndCapital.text = it
        })

        settingsViewModel.locationRequestTurnOff.observe(viewLifecycleOwner, Observer {
            if (it) {
                locationTracker?.removeLocationRequest()
                reinitPrayerSchedulers()
                Toast.makeText(
                    requireActivity(),
                    R.string.location_updated_successfully,
                    Toast.LENGTH_LONG
                ).show()
            }
        })
        settingsViewModel.locationerrorVisibility.observe(viewLifecycleOwner, Observer {
            if (it) {
                Toast.makeText(requireActivity(), R.string.invalid_coorinates, Toast.LENGTH_LONG)
                    .show()
            }
        })
        settingsViewModel.serviceNotAvailableVisibility.observe(viewLifecycleOwner, Observer {
            if (it) {
                Toast.makeText(requireActivity(), R.string.service_not_available, Toast.LENGTH_LONG)
                    .show()
            }
        })
        sharedViewModel.onGpsOpened.observe(viewLifecycleOwner, Observer {
            if (it) locationTracker?.updateLocation()
        })
        sharedViewModel.onLocationPermissiongranted.observe(viewLifecycleOwner, Observer {
            if (it) locationTracker?.getUserLocation()
        })
    }

    private fun reinitPrayerSchedulers() {
        WorkManager.getInstance(requireActivity()).cancelAllWork()

        val compressionWork =
            PeriodicWorkRequestBuilder<PrayerTimeWorkManager>(6, TimeUnit.HOURS)
                .build()
        WorkManager.getInstance(requireActivity()).enqueue(compressionWork)
    }

    override fun onLocationError() {
        Toast.makeText(requireActivity(), R.string.values_cannot_be_updated, Toast.LENGTH_LONG)
            .show()
    }

    override fun onLocationResult(locationResult: LocationResult) {
        for (location in locationResult.locations) {
            val geocoder = Geocoder(requireActivity(), Locale.getDefault())
            settingsViewModel.convertToAdress(geocoder, location.latitude, location.longitude)
        }
    }

    override fun onDestroy() {
        locationTracker = null
        super.onDestroy()
    }
}