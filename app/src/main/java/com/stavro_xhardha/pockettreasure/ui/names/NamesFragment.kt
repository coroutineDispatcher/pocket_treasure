package com.stavro_xhardha.pockettreasure.ui.names


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.stavro_xhardha.pockettreasure.BaseFragment
import com.stavro_xhardha.pockettreasure.R
import dagger.Lazy
import kotlinx.android.synthetic.main.error_layout.*
import kotlinx.android.synthetic.main.fragment_names.*
import javax.inject.Inject

class NamesFragment : BaseFragment() {

    @Inject
    lateinit var factory: Lazy<ViewModelProvider.Factory>
    private val namesViewModel by viewModels<NamesViewModel> { factory.get() }

    private val namesAdapter by lazy {
        NamesAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_names, container, false)
    }

    override fun performDi() {
        component.inject(this)
    }

    override fun observeTheLiveData() {
        namesViewModel.allNamesList.observe(this, Observer {
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

    override fun initializeComponents() {
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