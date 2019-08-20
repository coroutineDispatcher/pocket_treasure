package com.stavro_xhardha.pockettreasure.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.stavro_xhardha.pockettreasure.BaseFragment
import com.stavro_xhardha.pockettreasure.R
import com.stavro_xhardha.pockettreasure.brain.APPLICATION_TAG
import com.stavro_xhardha.pockettreasure.brain.PLAY_STORE_URL
import com.stavro_xhardha.pockettreasure.brain.startPrayerTimesWorkManager
import com.stavro_xhardha.pockettreasure.brain.viewModel
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : BaseFragment() {

    private val homeViewModel by viewModel { component.homeViewModelFactory.create(it) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                activity?.finish()
            }
        })
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.share_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_share)
            shareApp()
        return super.onOptionsItemSelected(item)
    }

    private fun shareApp() {
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, APPLICATION_TAG)
        sharingIntent.putExtra(Intent.EXTRA_TEXT, PLAY_STORE_URL)
        startActivity(Intent.createChooser(sharingIntent, resources.getString(R.string.share_via)))
    }

    override fun onResume() {
        super.onResume()
        homeViewModel.loadPrayerTimes()
    }

    override fun initializeComponents() {
        homeViewModel.initWorker()
    }

    override fun observeTheLiveData() {

        observeTiming()

        observeColors()

        homeViewModel.locationSecton.observe(this, Observer {
            tvCurrentLocation.text = it
        })

        homeViewModel.monthSection.observe(this, Observer {
            tvDateTimeTitle.text = it
        })

        homeViewModel.showErroToast.observe(this, Observer {
            if (it) Toast.makeText(activity!!, R.string.error_occured, Toast.LENGTH_LONG).show()
        })

        homeViewModel.progressBarVisibility.observe(this, Observer {
            pbHome.visibility = it
        })

        homeViewModel.contentVisibility.observe(this, Observer {
            cvFajr.visibility = it
            cvDhuhr.visibility = it
            cvAsr.visibility = it
            cvMaghrib.visibility = it
            cvIsha.visibility = it
            tvDateTimeTitle.visibility = it
            tvCurrentLocation.visibility = it
        })

        homeViewModel.workManagerHasBeenFired.observe(this, Observer {
            if (!it) {
                startPrayerTimesWorkManager(requireActivity())
                homeViewModel.updateWorkManagerFiredState()
            }
        })
    }

    private fun observeColors() {
        homeViewModel.fajrColor.observe(this, Observer {
            cvFajr.setBackgroundColor(ContextCompat.getColor(context!!, it))
        })

        homeViewModel.dhuhrColor.observe(this, Observer {
            cvDhuhr.setBackgroundColor(ContextCompat.getColor(context!!, it))
        })

        homeViewModel.asrColor.observe(this, Observer {
            cvAsr.setBackgroundColor(ContextCompat.getColor(context!!, it))
        })

        homeViewModel.maghribColor.observe(this, Observer {
            cvMaghrib.setBackgroundColor(ContextCompat.getColor(context!!, it))
        })

        homeViewModel.ishaColor.observe(this, Observer {
            cvIsha.setBackgroundColor(ContextCompat.getColor(context!!, it))
        })
    }

    private fun observeTiming() {
        homeViewModel.fajrTime.observe(this, Observer {
            tvFajrTime.text = it
        })

        homeViewModel.dhuhrtime.observe(this, Observer {
            tvDhuhrTime.text = it
        })

        homeViewModel.asrTime.observe(this, Observer {
            tvAsrTime.text = it
        })

        homeViewModel.maghribTime.observe(this, Observer {
            tvMagribTime.text = it
        })

        homeViewModel.ishaTime.observe(this, Observer {
            tvIshaTime.text = it
        })
    }
}
