package com.sxhardha.quran_module.quran

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.stavro_xhardha.core_module.brain.BaseFragment
import com.stavro_xhardha.core_module.brain.viewModel
import com.sxhardha.quran_module.QuranWorker
import com.sxhardha.quran_module.R
import kotlinx.android.synthetic.main.fragment_quran.*

class QuranFragment : BaseFragment(), QuranAdapterContract {
    private lateinit var btnRetry: Button

    private val quranViewModel by viewModel{
        DaggerQuranComponent.factory().create(applicationComponent).quranViewModel
    }

    private lateinit var compressionWork: WorkRequest

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
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    view.findNavController().popBackStack(R.id.homeFragment, false)
                }
            })
    }

    override fun initializeComponents() {
        startQuranWorker()
        rvSuras.adapter = quranAdapter
        btnRetry.setOnClickListener {
            startQuranWorker()
        }
    }

    private fun startQuranWorker() {
        quranViewModel.showProgress()

        compressionWork = OneTimeWorkRequestBuilder<QuranWorker>().build()

        WorkManager.getInstance(requireActivity()).enqueue(compressionWork)

        WorkManager.getInstance(requireActivity()).getWorkInfoByIdLiveData(compressionWork.id)
            .observe(viewLifecycleOwner, Observer {
                if (it != null && it.state == WorkInfo.State.SUCCEEDED) {
                    quranViewModel.startQuranDatabaseCall()
                } else {
                    if (it != null && it.state == WorkInfo.State.FAILED) {
                        quranViewModel.showError()
                    }
                }
            })
    }

    override fun observeTheLiveData() {
        quranViewModel.surahs.observe(viewLifecycleOwner, Observer {
            quranAdapter.submitList(it)
        })

        quranViewModel.errorVisibility.observe(viewLifecycleOwner, Observer {
            llError.visibility = it
        })

        quranViewModel.progressVisibility.observe(viewLifecycleOwner, Observer {
            pbQuran.visibility = it
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

    override fun onDestroy() {
        WorkManager.getInstance(requireActivity()).cancelWorkById(compressionWork.id)
        super.onDestroy()
    }
}