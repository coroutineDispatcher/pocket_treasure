package com.stavro_xhardha.core_module.ui

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.lifecycle.Observer
import com.stavro_xhardha.core_module.R
import com.stavro_xhardha.core_module.brain.APPLICATION_TAG
import com.stavro_xhardha.core_module.brain.BaseFragment
import com.stavro_xhardha.core_module.brain.PLAY_STORE_URL
import com.stavro_xhardha.core_module.brain.savedStateViewModel
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : BaseFragment() {

    private val homeViewModel by savedStateViewModel {
        applicationComponent.homeViewModelFactory.create(it)
    }
    private var pbHome: RelativeLayout? = null
    private val picasso = applicationComponent.picasso
    private var homeAdapter: HomeAdapter =
        HomeAdapter(picasso)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pbHome = view.findViewById(R.id.single_item_progress)
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
        sharingIntent.putExtra(
            Intent.EXTRA_SUBJECT,
            APPLICATION_TAG
        )
        sharingIntent.putExtra(
            Intent.EXTRA_TEXT,
            PLAY_STORE_URL
        )
        startActivity(Intent.createChooser(sharingIntent, resources.getString(R.string.share_via)))
    }

    override fun initializeComponents() {
        homeViewModel.loadPrayerTimes()
    }

    override fun observeTheLiveData() {
        homeViewModel.homeData.observe(viewLifecycleOwner, Observer {
            homeAdapter.submitList(it)
            rvHomePrayerTimes.adapter = homeAdapter
        })

        homeViewModel.contentVisibility.observe(viewLifecycleOwner, Observer {
            rvHomePrayerTimes.visibility = it
        })
        homeViewModel.showErrorToast.observe(viewLifecycleOwner, Observer { event ->
            event.getContentIfNotHandled()?.let {
                Toast.makeText(activity!!, event.peekContent(), Toast.LENGTH_LONG).show()
            }
        })

        homeViewModel.progressBarVisibility.observe(viewLifecycleOwner, Observer {
            pbHome?.visibility = it
        })
    }

    override fun onDestroyView() {
        pbHome = null
        rvHomePrayerTimes.adapter = null
        super.onDestroyView()
    }
}