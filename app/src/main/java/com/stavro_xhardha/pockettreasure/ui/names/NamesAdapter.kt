package com.stavro_xhardha.pockettreasure.ui.names

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.stavro_xhardha.pockettreasure.R
import com.stavro_xhardha.pockettreasure.brain.DIFF_UTIL_NAMES
import com.stavro_xhardha.pockettreasure.model.Name
import kotlinx.android.synthetic.main.single_item_name.view.*

class NamesAdapter : ListAdapter<Name, NamesAdapter.NamesViewHolder>(DIFF_UTIL_NAMES) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NamesViewHolder =
        NamesViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.single_item_name, parent, false))

    override fun onBindViewHolder(holder: NamesViewHolder, position: Int) {
        getItem(position).let { holder.bind(it) }
    }

    class NamesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(name: Name) = with(itemView) {
            itemView.tvArabicName.text = name.arabicName
            itemView.tvMeaning.text = name.meaning
            itemView.tvTransliteration.text = name.transliteration
        }
    }
}