package com.stavro_xhardha.pockettreasure.ui.quran.aya


import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.stavro_xhardha.pockettreasure.BaseFragment
import com.stavro_xhardha.pockettreasure.R
import dagger.Lazy
import kotlinx.android.synthetic.main.fragment_aya.*
import javax.inject.Inject

class AyaFragment : BaseFragment(), AyaContract {

    @Inject
    lateinit var factory: Lazy<ViewModelProvider.Factory>
    @Inject
    lateinit var mediaPlayer: MediaPlayer

    private val ayaViewModel by viewModels<AyaViewModel> { factory.get() }
    private val ayasAdapter by lazy {
        AyasAdapter(mediaPlayer, this)
    }

    private val args: AyaFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_aya, container, false)
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.stop()
        mediaPlayer.reset()
    }

    override fun initializeComponents() {
        val surahsNumber = args.surahsNumber
        ayaViewModel.startSuraDataBaseCall(surahsNumber)
        rvAya.adapter = ayasAdapter
        pbAya.visibility = View.VISIBLE
    }

    override fun performDi() {
        component.inject(this)
    }

    override fun observeTheLiveData() {
        ayaViewModel.ayas.observe(this, Observer {
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
        Snackbar.make(view!!, R.string.cannot_play_sound_check_connection, Snackbar.LENGTH_LONG).show()
    }
}