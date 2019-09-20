package com.stavro_xhardha.pockettreasure.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.stavro_xhardha.PocketTreasureApplication
import com.stavro_xhardha.pockettreasure.R
import com.stavro_xhardha.pockettreasure.background.PrayerTimeWorkManager
import com.stavro_xhardha.pockettreasure.brain.APPLICATION_TAG
import com.stavro_xhardha.pockettreasure.brain.PLAY_STORE_URL
import com.stavro_xhardha.pockettreasure.brain.viewModel
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.concurrent.TimeUnit

class HomeFragment : Fragment() {

    private val homeViewModel by viewModel {
        PocketTreasureApplication.getPocketTreasureComponent().homeViewModelFactory.create(it)
    }
    private val picasso = PocketTreasureApplication.getPocketTreasureComponent().picasso
    private var homeAdapter: HomeAdapter = HomeAdapter(picasso)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeViewModel.loadPrayerTimes()

        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    activity?.finish()
                }
            })
        setHasOptionsMenu(true)

        observeTheLiveData()
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

    fun observeTheLiveData() {
        homeViewModel.homeData.observe(this, Observer {
            homeAdapter.submitList(it)
            rvHomePrayerTimes.adapter = homeAdapter
        })

        homeViewModel.contentVisibility.observe(this, Observer {
            rvHomePrayerTimes.visibility = it
        })
        homeViewModel.showErrorToast.observe(this, Observer {
            if (it) Toast.makeText(activity!!, R.string.error_occured, Toast.LENGTH_LONG).show()
        })

        homeViewModel.progressBarVisibility.observe(this, Observer {
            pbHome.visibility = it
        })

        homeViewModel.workManagerHasBeenFired.observe(this, Observer {
            if (!it) {
                val compressionWork =
                    PeriodicWorkRequestBuilder<PrayerTimeWorkManager>(6, TimeUnit.HOURS)
                        .build()
                WorkManager.getInstance(requireActivity()).enqueue(compressionWork)
                homeViewModel.updateWorkManagerFiredState()
            }
        })
    }
}
