package com.stavro_xhardha.pockettreasure.ui.names


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.stavro_xhardha.pockettreasure.BaseFragment
import com.stavro_xhardha.pockettreasure.R
import com.stavro_xhardha.pockettreasure.brain.viewModel
import kotlinx.android.synthetic.main.error_layout.*
import kotlinx.android.synthetic.main.fragment_names.*

class NamesFragment : BaseFragment() {

    private val namesViewModel by viewModel { component.namesViewModelFactory.create(it) }

    private val namesAdapter by lazy {
        NamesAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_names, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initializeComponents()
        observeTheLiveData()
    }

    fun observeTheLiveData() {
        namesViewModel.namesList.observe(this, Observer {
            rvNames.adapter = namesAdapter
            namesAdapter.submitList(it)
        })
        namesViewModel.progressBarVisibility.observe(this, Observer {
            pbNames.visibility = it
        })
        namesViewModel.errorLayoutVisibility.observe(this, Observer {
            llError.visibility = it
        })
    }

    fun initializeComponents() {
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