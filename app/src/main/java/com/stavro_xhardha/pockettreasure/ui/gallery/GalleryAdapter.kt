package com.stavro_xhardha.pockettreasure.ui.gallery

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.squareup.picasso.Picasso
import com.stavro_xhardha.pockettreasure.R
import com.stavro_xhardha.pockettreasure.brain.DIFF_UTIL_GALLERY
import com.stavro_xhardha.pockettreasure.model.UnsplashResult
import kotlinx.android.synthetic.main.single_item_image.view.*

class GalleryAdapter(
    private val contract: GalleryContract,
    private val picasso: Picasso
) :
    PagedListAdapter<UnsplashResult, GalleryAdapter.GalleryViewHolder>(DIFF_UTIL_GALLERY) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder =
        GalleryViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.single_item_image, parent, false))

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        getItem(position).let { holder.bind(it, contract, picasso) }
    }

    class GalleryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bind(
            unsplashResult: UnsplashResult?,
            contract: GalleryContract,
            picasso: Picasso
        ) {
            with(itemView) {
                val currentImageView = ivUnsplashImage
                if (currentImageView != null) {
                    picasso.load(unsplashResult?.photoUrls?.thumbnailUrl)
                        .fit()
                        .error(R.drawable.img_placeholder)
                        .placeholder(R.drawable.img_placeholder)
                        .into(currentImageView)
//                    currentImageView.load(unsplashResult?.photoUrls?.thumbnailUrl) {
//                        error(R.drawable.img_placeholder)
//                        placeholder(R.drawable.img_placeholder)
//                    }
                    tvImageDescription.text = "By ${unsplashResult?.user?.userFullName ?: "Anonymous"}"
                    flImageHolder.setOnClickListener {
                        contract.onImageHolderClicked(unsplashResult?.photoUrls?.raw!!)
                    }
                }
            }
        }
    }
}