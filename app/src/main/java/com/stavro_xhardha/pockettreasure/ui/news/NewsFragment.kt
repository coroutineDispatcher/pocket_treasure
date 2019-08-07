package com.stavro_xhardha.pockettreasure.ui.news


//Todo Uncoment when restart the feature
//class NewsFragment : BaseFragment(), NewsAdapterContract {
//
//    @Inject
//    lateinit var factory: ViewModelProvider.Factory
//
//    private lateinit var newsViewModel: NewsViewModel
//    private lateinit var newsAdapter: NewsAdapter
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        return inflater.inflate(R.layout.fragment_news, container, false)
//    }
//
//    override fun initializeComponents() {
//        newsAdapter = NewsAdapter(this)
//        btnRetry.setOnClickListener {
//            newsViewModel.retry()
//        }
//    }
//
//    private fun enterThankingDialog() {
//        MaterialDialog(activity!!).show {
//            title(R.string.app_name)
//            message(R.string.thanking_news_api_message)
//            cancelable(true)
//            onDismiss {
//                newsViewModel.turnOfNextVisibility()
//            }
//            positiveButton(R.string.dismiss) {
//                it.dismiss()
//            }
//        }
//    }
//
//    override fun initViewModel() {
//        newsViewModel = ViewModelProviders.of(this, factory).get(NewsViewModel::class.java)
//    }
//
//    override fun performDi() {
//        //component.inject(this)
//    }
//
//    override fun observeTheLiveData() {
//        newsViewModel.currentNetworkState().observe(this, Observer {
//            if (it.status == Status.FAILED) Snackbar.make(
//                rlNewsHolder,
//                R.string.failed_loading_more,
//                Snackbar.LENGTH_LONG
//            ).show()
//        })
//
//        newsViewModel.initialNetworkState().observe(this, Observer {
//            when (it.status) {
//                Status.FAILED -> {
//                    llError.visibility = View.VISIBLE
//                    rvNews.visibility = View.GONE
//                    pbNews.visibility = View.GONE
//                    if (isDebugMode)
//                        Log.d(APPLICATION_TAG, "INITIAL_FAILED")
//                }
//                Status.SUCCESS -> {
//                    llError.visibility = View.GONE
//                    rvNews.visibility = View.VISIBLE
//                    pbNews.visibility = View.GONE
//                    if (isDebugMode)
//                        Log.d(APPLICATION_TAG, "INITIAL_SUCCESS")
//                }
//                Status.RUNNING -> {
//                    llError.visibility = View.GONE
//                    rvNews.visibility = View.GONE
//                    pbNews.visibility = View.VISIBLE
//                    if (isDebugMode)
//                        Log.d(APPLICATION_TAG, "INITIAL_LOADING")
//                }
//            }
//        })
//
//        newsViewModel.newsDataList().observe(this, Observer {
//            newsAdapter.submitList(it)
//            rvNews.adapter = newsAdapter
//        })
//
//        newsViewModel.enterDialogVisibility.observe(this, Observer {
//            if (it)
//                enterThankingDialog()
//        })
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
//                view.findNavController().popBackStack(R.id.homeFragment, false)
//            }
//        })
//    }
//
//    override fun onCurrentNewsClick(url: String) {
//        if (isDebugMode)
//            Log.d(APPLICATION_TAG, url)
//        if (url.isNotEmpty()) {
//            val action = NewsFragmentDirections.actionNewsFragmentToArticleWebViewFragment(url)
//            findNavController().navigate(action)
//        } else {
//            Toast.makeText(activity, "Url is empty", Toast.LENGTH_LONG).show()
//        }
//    }
//
//    override fun onShareClick(url: String, title: String) {
//        val sharingIntent = Intent(Intent.ACTION_SEND)
//        sharingIntent.type = "text/plain"
//        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, title)
//        sharingIntent.putExtra(Intent.EXTRA_TEXT, url)
//        startActivity(Intent.createChooser(sharingIntent, activity!!.resources.getString(R.string.share_via)))
//    }
//
//    override fun showErrorMessage() {
//        Toast.makeText(activity, "Error Loading Data", Toast.LENGTH_LONG).show()
//    }
//}