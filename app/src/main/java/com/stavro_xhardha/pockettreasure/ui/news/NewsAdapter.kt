//package com.stavro_xhardha.pockettreasure.ui.news
//
//import android.annotation.SuppressLint
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.paging.PagedListAdapter
//import androidx.recyclerview.widget.RecyclerView
//import com.squareup.picasso.Picasso
//import com.stavro_xhardha.pockettreasure.R
//import com.stavro_xhardha.pockettreasure.brain.DIFF_UTIL_NEWS
//import com.stavro_xhardha.pockettreasure.model.News
//import kotlinx.android.synthetic.main.single_item_news.view.*
//
//class NewsAdapter(private val newsAdapterContract: NewsAdapterContract) :
//    PagedListAdapter<News, NewsAdapter.NewsViewHolder>(DIFF_UTIL_NEWS) {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder =
//        NewsViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.single_item_news, parent, false))
//
//
//    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
//        getItem(position).let { holder.bind(it, newsAdapterContract) }
//    }
//
//    class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        @SuppressLint("SetTextI18n")
//        fun bind(
//            news: News?,
//            newsAdapterContract: NewsAdapterContract
//        ) {
//            with(itemView) {
//                tvTitle.text = news?.title
//                tvAuthor.text = "By ${news?.author ?: "Anonymous"}"
//                tvDescription.text = news?.description
//                tvSource.text = news?.newsSource?.name
//
//                Picasso.get().load(news?.urlOfImage).fit()
//                    .placeholder(R.drawable.no_img_available).error(R.drawable.no_img_available).into(ivPostImage)
//
//                cvCurrentNews.setOnClickListener {
//                    val currentUrl = news?.url
//                    if (currentUrl != null) {
//                        newsAdapterContract.onCurrentNewsClick(currentUrl)
//                    }
//                }
//
//                ivShare.setOnClickListener {
//                    val currentUrl = news?.url
//                    val currentTitle = news?.title
//                    if (currentUrl != null && currentTitle != null)
//                        newsAdapterContract.onShareClick(currentUrl, currentTitle)
//                }
//            }
//        }
//
//    }
//}