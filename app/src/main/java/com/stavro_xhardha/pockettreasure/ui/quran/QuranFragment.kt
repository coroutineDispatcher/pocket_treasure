package com.stavro_xhardha.pockettreasure.ui.quran


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.stavro_xhardha.pockettreasure.BaseFragment
import com.stavro_xhardha.pockettreasure.R
import com.stavro_xhardha.pockettreasure.brain.viewModel
import dagger.Lazy
import kotlinx.android.synthetic.main.error_layout.*
import kotlinx.android.synthetic.main.fragment_quran.*
import javax.inject.Inject

class QuranFragment : BaseFragment(), QuranAdapterContract {


    private val quranViewModel by viewModel { component.quranViewModelFactory.create() }

    private val quranAdapter by lazy {
        QuranAdapter(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_quran, container, false)
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
        rvSuras.adapter = quranAdapter
        btnRetry.setOnClickListener {
            quranViewModel.startQuranImplementation()
        }
    }

    override fun performDi() {

    }

    override fun observeTheLiveData() {
        quranViewModel.surahs.observe(this, Observer {
            quranAdapter.submitList(it)
        })

        quranViewModel.errorVisibility.observe(this, Observer {
            llError.visibility = it
        })

        quranViewModel.progressVisibility.observe(this, Observer {
            pbQuran.visibility = it
        })

        quranViewModel.listVisibility.observe(this, Observer {
            rvSuras.visibility = it
        })
    }

    override fun onSurahClicked(surahsNumber: Int) {
        val action = QuranFragmentDirections.actionQuranFragmentToAyaFragment(surahsNumber)
        findNavController().navigate(action)
    }
}