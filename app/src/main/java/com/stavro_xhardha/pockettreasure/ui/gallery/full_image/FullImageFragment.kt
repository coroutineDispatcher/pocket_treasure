package com.stavro_xhardha.pockettreasure.ui.gallery.full_image

import android.Manifest
import android.annotation.SuppressLint
import android.app.WallpaperManager
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItemsSingleChoice
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.stavro_xhardha.PocketTreasureApplication
import com.stavro_xhardha.pockettreasure.R
import com.stavro_xhardha.pockettreasure.brain.*
import kotlinx.android.synthetic.main.fragment_full_image.*
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.Request
import okio.buffer
import okio.sink
import java.io.IOException


class FullImageFragment : Fragment() {

    private val args: FullImageFragmentArgs by navArgs()
    private lateinit var expetedUrl: String

    private val completableJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + completableJob)
    private lateinit var picasso: Picasso
    private lateinit var wallpaperManager: WallpaperManager


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_full_image, container, false)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(FULL_IMAGE_SAVED_STATE, args.imageUrl)
        super.onSaveInstanceState(outState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        picasso = PocketTreasureApplication.getPocketTreasureComponent().picasso
        wallpaperManager = PocketTreasureApplication.getPocketTreasureComponent().wallpaperManager

        if (savedInstanceState == null) {
            expetedUrl = args.imageUrl
            loadImage(expetedUrl)
        } else {
            expetedUrl = savedInstanceState.getString(FULL_IMAGE_SAVED_STATE)!!
            loadImage(expetedUrl)
        }
    }

    private fun loadImage(url: String) {
        picasso.load(url)
            .into(ivFullImage, object : Callback {
                override fun onSuccess() {
                    onSuccessFulImageLoad()
                }

                override fun onError(e: Exception?) {
                    loadErrorImage()
                }
            })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_STORAGE_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    writeImageToFile()
                } else {
                    Toast.makeText(
                        activity,
                        R.string.can_not_save_image_need_permission,
                        Toast.LENGTH_LONG
                    ).show()
                }
                return
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        completableJob.cancel()
    }

    private fun loadErrorImage() {
        pbFullImage.visibility = View.GONE
        picasso.load(R.drawable.ic_error_in_connection).into(ivFullImage)
    }

    private fun onSuccessFulImageLoad() {
        pbFullImage.visibility = View.GONE
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.image_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when {
            item.itemId == R.id.action_download -> saveImageToUrl()
            item.itemId == R.id.action_share -> shareImageUrl()
            item.itemId == R.id.action_set_wallpaper -> showWallpapaerDialog()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showWallpapaerDialog() {
        MaterialDialog(activity!!).show {
            title(R.string.set_as_wallpaper)
            listItemsSingleChoice(
                items = listOf("Home Screen", "Lock Screen", "Both"),
                initialSelection = 0
            ) { dialog, index, _ ->
                setWallPaper(index)
                dialog.dismiss()
            }
        }
    }

    private fun setWallPaper(index: Int) {
        coroutineScope.launch(Dispatchers.IO) {
            incrementIdlingResource()
            withContext(Dispatchers.Main) {
                pbFullImage.visibility = View.VISIBLE
                decrementIdlingResource()
            }

            incrementIdlingResource()
            initWallPaperSetting(index)

            withContext(Dispatchers.Main) {
                decrementIdlingResource()
                pbFullImage.visibility = View.GONE
                Snackbar.make(rlFullImageHolder, R.string.wallpaper_saved, Snackbar.LENGTH_LONG)
                    .show()
            }
        }
    }

    private fun initWallPaperSetting(index: Int) {
        val result = picasso.load(expetedUrl).get()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            when (index) {
                0 -> wallpaperManager.setBitmap(result, null, true, WallpaperManager.FLAG_SYSTEM)
                1 -> wallpaperManager.setBitmap(result, null, true, WallpaperManager.FLAG_LOCK)
                else -> wallpaperManager.setBitmap(result)
            }
        } else {
            wallpaperManager.setBitmap(result)
        }
    }

    private fun saveImageToUrl() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                checkForPermission()
            } else {
                writeImageToFile()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun checkForPermission() {
        if (ContextCompat.checkSelfPermission(
                activity!!.applicationContext,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                REQUEST_STORAGE_PERMISSION
            )
        } else {
            writeImageToFile()
        }
    }

    private fun writeImageToFile() {
        coroutineScope.launch(Dispatchers.IO) {
            incrementIdlingResource()
            withContext(Dispatchers.Main) {
                decrementIdlingResource()
                pbFullImage.visibility = View.VISIBLE
            }

            incrementIdlingResource()
            initImageSaving()

            withContext(Dispatchers.Main) {
                decrementIdlingResource()
                pbFullImage.visibility = View.GONE
                Snackbar.make(rlFullImageHolder, R.string.image_saved, Snackbar.LENGTH_LONG).show()
            }
        }
    }

    @SuppressLint("InlinedApi")
    private fun initImageSaving() {
        val contentValues = ContentValues().apply {
            put(
                MediaStore.Images.Media.TITLE,
                "PD-${System.currentTimeMillis()}"
            )
            put(
                MediaStore.Images.Media.DISPLAY_NAME,
                System.currentTimeMillis().toString()
            )
            put(MediaStore.Images.Media.BUCKET_ID, "/pocket_deen")
            put(MediaStore.Images.Media.BUCKET_DISPLAY_NAME, "PocketDeen")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.IS_PENDING, 1)
        }

        val resolver = requireActivity().contentResolver
        val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        val imageResponse =
            //todo use singleton OkHttpInstance
            OkHttpClient().newCall(Request.Builder().url(expetedUrl).build()).execute()

        if (imageResponse.isSuccessful) {
            uri?.let {

                resolver.openOutputStream(uri)?.use {
                    val sink = it.sink().buffer()
                    sink.writeAll(imageResponse.body!!.source())
                    sink.close()
                }

                contentValues.clear()
                contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
                resolver.update(uri, contentValues, null, null)
            } ?: Log.d(APPLICATION_TAG, "Sth went wrong")
        } else {
            Toast.makeText(requireActivity(), R.string.image_error_saved, Toast.LENGTH_LONG).show()
        }
    }

    private fun shareImageUrl() {
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        sharingIntent.putExtra(Intent.EXTRA_TEXT, expetedUrl)
        startActivity(
            Intent.createChooser(
                sharingIntent,
                activity!!.resources.getString(R.string.share_via)
            )
        )
    }
}
