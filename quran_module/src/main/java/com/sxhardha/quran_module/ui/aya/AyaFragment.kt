package com.sxhardha.quran_module.ui.aya

import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.stavro_xhardha.core_module.SharedViewModel
import com.stavro_xhardha.core_module.brain.BaseFragment
import com.stavro_xhardha.core_module.brain.viewModel
import com.stavro_xhardha.core_module.dependency_injection.CoreApplication
import com.sxhardha.quran_module.R
import com.sxhardha.quran_module.di.DaggerQuranComponent
import com.sxhardha.quran_module.di.QuranApiModule
import com.sxhardha.quran_module.di.QuranComponent
import com.sxhardha.quran_module.di.QuranDatabaseModule
import kotlinx.android.synthetic.main.fragment_aya.*

class AyaFragment : BaseFragment(), AyaContract {
    private lateinit var component: QuranComponent

    private val mediaPlayer: MediaPlayer by lazy {
        CoreApplication.getCoreComponent().mediaPlayer
    }

    private val ayaViewModel by viewModel {
        component.ayaViewModel
    }

    private val ayasAdapter by lazy {
        AyasAdapter(mediaPlayer, this)
    }

    private val sharedViewModel: SharedViewModel by lazy {
        requireActivity().run {
            ViewModelProviders.of(this).get(SharedViewModel::class.java)
        }
    }

    private val args: AyaFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_aya, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (args.surahsNumber == 9) {
            sharedViewModel.removeToolbarTitle()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.stop()
        mediaPlayer.reset()
    }

    override fun initializeComponents() {
        component = DaggerQuranComponent.builder().coreComponent(applicationComponent)
            .quranApiModule(QuranApiModule(applicationComponent.retrofit))
            .quranDatabaseModule(QuranDatabaseModule(applicationComponent.application))
            .build()
        val surahsNumber = args.surahsNumber
        ayaViewModel.startSuraDataBaseCall(surahsNumber)
        rvAya.adapter = ayasAdapter
        pbAya.visibility = View.VISIBLE
    }

    override fun observeTheLiveData() {
        ayaViewModel.ayas.observe(viewLifecycleOwner, Observer {
            if (it.size > 0) {
                ayasAdapter.submitList(it)
                pbAya.visibility = View.GONE
                rvAya.visibility = View.VISIBLE
            } else {
                llError.visibility = View.VISIBLE
                rvAya.visibility = View.GONE
                pbAya.visibility = View.GONE
            }
        })
    }

    override fun onMediaPlayerError() {
        Snackbar.make(view!!, R.string.cannot_play_sound_check_connection, Snackbar.LENGTH_LONG)
            .show()
    }
}