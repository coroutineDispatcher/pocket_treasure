package com.sxhardha.quran_module.ui.quran

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.stavro_xhardha.core_module.dependency_injection.FragmentScoped
import com.sxhardha.quran_module.R
import com.sxhardha.quran_module.model.Surah
import com.sxhardha.quran_module.utils.DIFF_UTIL_QURAN
import kotlinx.android.synthetic.main.single_item_surah.view.*
import javax.inject.Inject

@FragmentScoped
class QuranAdapter @Inject constructor(private val quranAdapterContract: QuranAdapterContract) :
    ListAdapter<Surah, QuranAdapter.QuranViewHolder>(DIFF_UTIL_QURAN) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuranViewHolder =
        QuranViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.single_item_surah,
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: QuranViewHolder, position: Int) {
        getItem(position).let { holder.bind(it, quranAdapterContract) }
    }

    class QuranViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bind(
            surah: Surah?,
            quranAdapterContract: QuranAdapterContract
        ) {
            with(itemView) {
                tvSurahTitle.text = "${surah?.surahNumber}. ${surah?.englishName}"
                tvRelevation.text = "Relevation: ${surah?.revelationType}"
                tvarabicSurahName.text = "${surah?.surahArabicName}"

                cvSurah.setOnClickListener {
                    surah?.surahNumber?.let { clickedSurah ->
                        quranAdapterContract.onSurahClicked(
                            clickedSurah
                        )
                    }
                }
            }
        }
    }
}