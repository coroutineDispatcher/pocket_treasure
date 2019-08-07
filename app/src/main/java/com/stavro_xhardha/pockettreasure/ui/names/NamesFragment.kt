package com.stavro_xhardha.pockettreasure.ui.names


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.stavro_xhardha.pockettreasure.BaseFragment
import com.stavro_xhardha.pockettreasure.R
import kotlinx.android.synthetic.main.error_layout.*
import kotlinx.android.synthetic.main.fragment_names.*
import javax.inject.Inject

class NamesFragment : BaseFragment() {
    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private lateinit var namesViewModel: NamesViewModel
    private lateinit var namesAdapter: NamesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_names, container, false)
    }

    override fun performDi() {
        component.inject(this)
    }

    override fun initViewModel() {
        namesViewModel = ViewModelProviders.of(this, factory)
            .get(NamesViewModel::class.java)
    }

    override fun observeTheLiveData() {
        namesViewModel.allNamesList.observe(this, Observer {
            namesAdapter = NamesAdapter(it)
            rvNames.adapter = namesAdapter
        })
        namesViewModel.progressBarVisibility.observe(this, Observer {
            pbNames.visibility = it
        })
        namesViewModel.errorLayoutVisibility.observe(this, Observer {
            llError.visibility = it
        })
    }

    override fun initializeComponents() {
        rvNames.layoutManager = LinearLayoutManager(activity)
        btnRetry.setOnClickListener {
            namesViewModel.retryConnection()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                view.findNavController().popBackStack(R.id.homeFragment, false)
            }
        })
    }
}