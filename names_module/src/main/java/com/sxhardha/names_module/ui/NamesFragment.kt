package com.sxhardha.names_module.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.Observer
import com.stavro_xhardha.core_module.brain.BaseFragment
import com.stavro_xhardha.core_module.brain.savedStateViewModel
import com.sxhardha.names_module.R
import com.sxhardha.names_module.di.DaggerNameComponent
import com.sxhardha.names_module.di.NamesDatabaseModule
import com.sxhardha.names_module.di.NamesNetworkModule
import kotlinx.android.synthetic.main.fragment_names.*
import javax.inject.Inject

class NamesFragment : BaseFragment() {
    @Inject
    lateinit var namesViewModelFactory: NamesViewModel.Factory

    private lateinit var btnRetry: Button
    private lateinit var pbNames: View

    private val namesViewModel by savedStateViewModel {
        namesViewModelFactory.create(it)
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
        provideDI()
        btnRetry.setOnClickListener {
            namesViewModel.retryConnection()
        }
    }

    private fun provideDI() {
        DaggerNameComponent.builder().coreComponent(applicationComponent)
            .namesNetworkModule(NamesNetworkModule(applicationComponent.retrofit))
            .namesDatabaseModule(NamesDatabaseModule(applicationComponent.application))
            .build().inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnRetry = view.findViewById(R.id.btnRetry)
        pbNames = view.findViewById(R.id.pbNames)
    }

    override fun onDestroyView() {
        rvNames.adapter = null
        super.onDestroyView()
    }
}