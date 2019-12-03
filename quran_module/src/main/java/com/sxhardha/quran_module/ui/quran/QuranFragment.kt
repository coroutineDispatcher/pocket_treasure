package com.sxhardha.quran_module.ui.quran

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.stavro_xhardha.core_module.brain.BaseFragment
import com.stavro_xhardha.core_module.brain.viewModel
import com.sxhardha.quran_module.R
import com.sxhardha.quran_module.di.DaggerQuranComponent
import com.sxhardha.quran_module.di.QuranApiModule
import com.sxhardha.quran_module.di.QuranComponent
import com.sxhardha.quran_module.di.QuranDatabaseModule
import kotlinx.android.synthetic.main.fragment_quran.*

class QuranFragment : BaseFragment(), QuranAdapterContract {
    private var btnRetry: Button? = null
    private lateinit var quranComponent: QuranComponent
    private var pbQuran: RelativeLayout? = null
    private val quranViewModel by viewModel {
        quranComponent.quranViewModel
    }

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
        btnRetry = view.findViewById(R.id.btnRetry)
        pbQuran = view.findViewById(R.id.pbQuran)
    }

    override fun initializeComponents() {
        quranComponent = DaggerQuranComponent.builder().coreComponent(applicationComponent)
            .quranDatabaseModule(QuranDatabaseModule(applicationComponent.application))
            .quranApiModule(QuranApiModule(applicationComponent.retrofit))
            .build()
        rvSuras.adapter = quranAdapter
        btnRetry?.setOnClickListener {
            quranViewModel.remakeQuranCall()
        }
    }

    override fun observeTheLiveData() {
        quranViewModel.surahs.observe(viewLifecycleOwner, Observer {
            quranAdapter.submitList(it)
        })

        quranViewModel.errorVisibility.observe(viewLifecycleOwner, Observer {
            llError.visibility = it
        })

        quranViewModel.progressVisibility.observe(viewLifecycleOwner, Observer {
            pbQuran?.visibility = it
        })

        quranViewModel.listVisibility.observe(viewLifecycleOwner, Observer {
            rvSuras.visibility = it
        })
    }

    override fun onSurahClicked(surahsNumber: Int) {
        val action =
            QuranFragmentDirections.actionQuranFragmentToAyaFragment(
                surahsNumber
            )
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        rvSuras.adapter = null
        btnRetry = null
        pbQuran = null
        super.onDestroyView()
    }
}