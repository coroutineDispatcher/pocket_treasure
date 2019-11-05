package com.stavro_xhardha.core_module.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.stavro_xhardha.core_module.R
import com.stavro_xhardha.core_module.brain.DIFF_UTIL_HOME
import com.stavro_xhardha.core_module.model.HomePrayerTime
import kotlinx.android.synthetic.main.single_item_prayer_time.view.*

class HomeAdapter(private val picasso: Picasso) :
    ListAdapter<HomePrayerTime, HomeAdapter.HomeViewHolder>(DIFF_UTIL_HOME) {

    private lateinit var context: Context


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {

        context = parent.context

        return HomeViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.single_item_prayer_time,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        getItem(position).let {
            holder.bind(it, picasso)
        }
    }


    class HomeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(
            prayerTiming: HomePrayerTime?,
            picasso: Picasso
        ) {
            with(itemView) {
                tvCurrentPrayerName.text = prayerTiming?.name
                tvCurrentPrayerTime.text = prayerTiming?.time
                cvCurrentHolder.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        prayerTiming?.backgroundColor!!
                    )
                )
                picasso.load(prayerTiming.icon).fit().into(ivCurrentIcon)
            }
        }

    }

}