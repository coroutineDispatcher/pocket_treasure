//package com.stavro_xhardha.pockettreasure.ui.news
//
//
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.webkit.WebViewClient
//import androidx.fragment.app.Fragment
//import androidx.navigation.fragment.navArgs
//import com.stavro_xhardha.pockettreasure.R
//import kotlinx.android.synthetic.main.fragment_article_web_view.*
//
//class ArticleWebViewFragment : Fragment() {
//
//    private val arguments: ArticleWebViewFragmentArgs by navArgs()
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        return inflater.inflate(R.layout.fragment_article_web_view, container, false)
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        val urlToExpect = arguments.url
//        wvArticle.webViewClient = WebViewClient()
//        if (urlToExpect.isNotEmpty()) {
//            wvArticle.loadUrl(urlToExpect)
//        }
//    }
//}