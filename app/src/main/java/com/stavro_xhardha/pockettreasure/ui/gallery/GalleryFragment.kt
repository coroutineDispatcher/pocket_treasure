package com.stavro_xhardha.pockettreasure.ui.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.stavro_xhardha.core_module.brain.BaseFragment
import com.stavro_xhardha.pockettreasure.R
import com.stavro_xhardha.pockettreasure.brain.Status
import kotlinx.android.synthetic.main.error_layout.*
import kotlinx.android.synthetic.main.fragment_gallery.*

class GalleryFragment : BaseFragment(), GalleryContract {
    private val picasso by lazy {
        applicationComponent.picasso
    }

    private val galleryAdapter by lazy {
        GalleryAdapter(this, picasso)
    }

    private val galleryViewModel by viewModels<GalleryViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_gallery, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    view.findNavController().popBackStack(R.id.homeFragment, false)
                }
            })
    }

    override fun initializeComponents() {
        rvGallery.layoutManager = GridLayoutManager(activity, 3)
        rvGallery.adapter = galleryAdapter
        btnRetry.setOnClickListener {
            galleryViewModel.retry()
        }
    }

    override fun observeTheLiveData() {
        galleryViewModel.getGalleryLiveData().observe(viewLifecycleOwner, Observer {
            galleryAdapter.submitList(it)
        })
        galleryViewModel.getCurrentState().observe(viewLifecycleOwner, Observer {
            if (it.status == Status.FAILED)
                Snackbar.make(rlGallery, R.string.failed_loading_more, Snackbar.LENGTH_LONG).show()
        })
        galleryViewModel.getInitialState().observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.FAILED -> {
                    pbGallery.visibility = View.GONE
                    llError.visibility = View.VISIBLE
                    rvGallery.visibility = View.GONE
                }
                Status.RUNNING -> {
                    pbGallery.visibility = View.VISIBLE
                    llError.visibility = View.GONE
                    rvGallery.visibility = View.GONE
                }
                Status.SUCCESS -> {
                    pbGallery.visibility = View.GONE
                    llError.visibility = View.GONE
                    rvGallery.visibility = View.VISIBLE
                }
            }
        })
    }

    override fun onImageHolderClicked(url: String) {
        if (url.isNotEmpty()) {
            val action = GalleryFragmentDirections.actionGalleryFragmentToFullImageFragment(url)
            findNavController().navigate(action)
        }
    }
}