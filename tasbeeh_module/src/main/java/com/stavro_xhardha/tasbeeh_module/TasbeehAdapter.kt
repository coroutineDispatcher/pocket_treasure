package com.stavro_xhardha.tasbeeh_module

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.single_item_tasbeeh.view.*

class TasbeehAdapter : ListAdapter<Tasbeeh, TasbeehAdapter.TasbeehViewHolder>(DIFF_UTIL_TASBEEH) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasbeehViewHolder =
        TasbeehViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.single_item_tasbeeh,
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: TasbeehViewHolder, position: Int) {
        getItem(position).let { holder.bind(it) }
    }

    class TasbeehViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(tasbeeh: Tasbeeh) {
            with(itemView) {
                tvArabic.text = tasbeeh.arabicPhrase
                tvTransliteration.text = tasbeeh.transliteration
                tvTranslation.text = tasbeeh.translation
                tvTasbeehNumber.text = "0"
                var incrementer = 0
                fabIncrement.setOnClickListener {
                    try {
                        if (tvTasbeehNumber.text.toString().toInt() < 33) {
                            incrementer++
                            tvTasbeehNumber.text = "$incrementer"
                        }
                    } catch (e: NumberFormatException) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }
}

val DIFF_UTIL_TASBEEH = object : DiffUtil.ItemCallback<Tasbeeh>() {
    override fun areItemsTheSame(oldItem: Tasbeeh, newItem: Tasbeeh): Boolean =
        oldItem.arabicPhrase == newItem.arabicPhrase

    override fun areContentsTheSame(oldItem: Tasbeeh, newItem: Tasbeeh): Boolean =
        oldItem.arabicPhrase == newItem.arabicPhrase
                && oldItem.translation == newItem.translation
                && oldItem.transliteration == newItem.transliteration

}