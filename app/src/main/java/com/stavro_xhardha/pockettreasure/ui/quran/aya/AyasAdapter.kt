package com.stavro_xhardha.pockettreasure.ui.quran.aya

import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.stavro_xhardha.pockettreasure.R
import com.stavro_xhardha.pockettreasure.brain.DIFF_UTIL_AYA
import com.stavro_xhardha.core_module.brain.FIRST_SURAH_SENTENCE
import com.stavro_xhardha.core_module.model.Aya
import kotlinx.android.synthetic.main.single_item_aya.view.*

class AyasAdapter(private val mediaPlayer: MediaPlayer, private val ayaContract: AyaContract) :
    PagedListAdapter<Aya, AyasAdapter.AyasViewHolder>(DIFF_UTIL_AYA) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AyasViewHolder =
        AyasViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.single_item_aya, parent, false))

    override fun onBindViewHolder(holder: AyasViewHolder, position: Int) {
        getItem(position).let { holder.bind(it, mediaPlayer, ayaContract) }
    }

    class AyasViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(
            aya: Aya?,
            mediaPlayer: MediaPlayer,
            ayaContract: AyaContract
        ) = with(itemView) {
            if (aya?.ayatText!!.contains(FIRST_SURAH_SENTENCE) && aya.surahNumber != 1) {
                val newAya = aya.ayatText.substring(FIRST_SURAH_SENTENCE.length, aya.ayatText.length)
                tvAyaText.text = newAya
            } else
                tvAyaText.text = aya.ayatText

            tvAyaNumber.text = aya.ayatNumber.toString()

            ivPlayImage.setOnClickListener {
                if (!mediaPlayer.isPlaying) {
                    try {
                        val audioUrl = if (aya.audioUrl.contains("https")) aya.audioUrl else
                            aya.audioUrl.replace("http", "https")
                        mediaPlayer.setDataSource(audioUrl)
                        mediaPlayer.prepareAsync()

                        mediaPlayer.setOnPreparedListener { mediaPlayer ->
                            mediaPlayer.start()
                            ivPlayImage.setImageResource(R.drawable.ic_stop_black_24dp)
                            ivPlayImage.tag = R.string.playing
                        }
                    } catch (exception: Exception) {
                        exception.printStackTrace()
                        ivPlayImage.setImageResource(R.drawable.ic_play_arrow_black_24dp)
                        ivPlayImage.tag = R.string.stoped
                        ayaContract.onMediaPlayerError()
                    }
                    mediaPlayer.setOnCompletionListener { mediaPlayer ->
                        mediaPlayer.stop()
                        mediaPlayer.reset()
                        ivPlayImage.setImageResource(R.drawable.ic_play_arrow_black_24dp)
                        ivPlayImage.tag = R.string.stoped
                    }
                } else {
                    if (ivPlayImage.tag == R.string.playing) {
                        mediaPlayer.stop()
                        mediaPlayer.reset()
                        ivPlayImage.setImageResource(R.drawable.ic_play_arrow_black_24dp)
                        ivPlayImage.tag = R.string.stoped
                    }
                }
            }

        }
    }
}