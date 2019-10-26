package com.stavro_xhardha.pockettreasure.ui.names


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.stavro_xhardha.pockettreasure.R
import com.stavro_xhardha.pockettreasure.brain.viewModel
import com.stavro_xhardha.pockettreasure.ui.BaseFragment
import kotlinx.android.synthetic.main.error_layout.*
import kotlinx.android.synthetic.main.fragment_names.*

class NamesFragment : BaseFragment() {

    private val namesViewModel by viewModel {
        applicationComponent.namesViewModelFactory.create(it)
    }

    private val namesAdapter by lazy {
        NamesAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_names, container, false)
    }

    override fun observeTheLiveData() {
        namesViewModel.namesList.observe(viewLifecycleOwner, Observer {
            rvNames.adapter = namesAdapter
            namesAdapter.submitList(it)
        })
        namesViewModel.progressBarVisibility.observe(viewLifecycleOwner, Observer {
            pbNames.visibility = it
        })
        namesViewModel.errorLayoutVisibility.observe(viewLifecycleOwner, Observer {
            llError.visibility = it
        })
    }

    override fun initializeComponents() {
        btnRetry.setOnClickListener {
            namesViewModel.retryConnection()
        }
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
}