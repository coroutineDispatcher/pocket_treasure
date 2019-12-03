package com.sxhardha.gallery_module.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.stavro_xhardha.core_module.brain.BaseFragment
import com.stavro_xhardha.core_module.brain.viewModel
import com.sxhardha.gallery_module.R
import com.sxhardha.gallery_module.di.DaggerGalleryComponent
import com.sxhardha.gallery_module.di.GalleryComponent
import com.sxhardha.gallery_module.di.GalleryNetworkModule
import com.sxhardha.gallery_module.utils.Status
import kotlinx.android.synthetic.main.fragment_gallery.*

class GalleryFragment : BaseFragment(), GalleryContract {

    private lateinit var galleryComponent: GalleryComponent
    private var btnRetry: Button? = null
    private var pbGallery: RelativeLayout? = null

    private val picasso by lazy {
        applicationComponent.picasso
    }

    private val galleryAdapter by lazy {
        GalleryAdapter(this, picasso)
    }

    private val galleryViewModel by viewModel {
        galleryComponent.galleryViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_gallery, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnRetry = view.findViewById(R.id.btnRetry)
        pbGallery = view.findViewById(R.id.pbGallery)
    }

    override fun initializeComponents() {
        galleryComponent = DaggerGalleryComponent.builder().coreComponent(applicationComponent)
            .galleryNetworkModule(GalleryNetworkModule(applicationComponent.retrofit))
            .build()
        rvGallery.layoutManager = GridLayoutManager(activity, 3)
        rvGallery.adapter = galleryAdapter
        btnRetry?.setOnClickListener {
            galleryViewModel.retry()
        }
    }

    override fun observeTheLiveData() {
        galleryViewModel.getGalleryLiveData().observe(viewLifecycleOwner, Observer {
            galleryAdapter.submitList(it)
        })
        galleryViewModel.getCurrentState().observe(viewLifecycleOwner, Observer {
            if (it.status == Status.FAILED)
                Snackbar.make(
                    rlGallery,
                    R.string.failed_loading_more, Snackbar.LENGTH_LONG
                ).show()
        })
        galleryViewModel.getInitialState().observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.FAILED -> {
                    pbGallery?.visibility = View.GONE
                    llError.visibility = View.VISIBLE
                    rvGallery.visibility = View.GONE
                }
                Status.RUNNING -> {
                    pbGallery?.visibility = View.VISIBLE
                    llError.visibility = View.GONE
                    rvGallery.visibility = View.GONE
                }
                Status.SUCCESS -> {
                    pbGallery?.visibility = View.GONE
                    llError.visibility = View.GONE
                    rvGallery.visibility = View.VISIBLE
                }
            }
        })
    }

    override fun onImageHolderClicked(url: String) {
        if (url.isNotEmpty()) {
            val action =
                GalleryFragmentDirections.actionGalleryFragmentToFullImageFragment(
                    url
                )
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        btnRetry = null
        rvGallery.adapter = null
        pbGallery = null
        super.onDestroyView()
    }
}