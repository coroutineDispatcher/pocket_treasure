package com.stavro_xhardha.pockettreasure.ui.gallery.full_image

import android.Manifest
import android.annotation.SuppressLint
import android.app.WallpaperManager
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
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
import com.stavro_xhardha.core_module.brain.FULL_IMAGE_SAVED_STATE
import com.stavro_xhardha.core_module.brain.REQUEST_STORAGE_PERMISSION
import com.stavro_xhardha.pockettreasure.brain.decrementIdlingResource
import com.stavro_xhardha.pockettreasure.brain.incrementIdlingResource
import kotlinx.android.synthetic.main.fragment_full_image.*
import kotlinx.coroutines.*
import java.io.File
import java.io.IOException


class FullImageFragment : Fragment() {

    private val args: FullImageFragmentArgs by navArgs()
    private lateinit var expetedUrl: String

    private val completableJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + completableJob)
    private lateinit var picasso: Picasso
    private lateinit var wallpaperManager: WallpaperManager
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
        Snackbar.make(rlFullImageHolder, R.string.error_saving_image, Snackbar.LENGTH_LONG).show()
    }


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
            item.itemId == R.id.action_set_wallpaper -> showWallpaperDialog()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showWallpaperDialog() {
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
        coroutineScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
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

    @SuppressLint("InlinedApi", "ObsoleteSdkInt")
    private fun initImageSaving() {
        val relativeLocation = Environment.DIRECTORY_PICTURES + File.pathSeparator + "PocketDeen"
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, System.currentTimeMillis().toString())
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.MediaColumns.RELATIVE_PATH, relativeLocation)
                put(MediaStore.MediaColumns.IS_PENDING, 1)
            }
        }

        val resolver = requireActivity().contentResolver
        val bitmap = picasso.load(expetedUrl).get()

        val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        try {

            uri?.let { uri ->
                val stream = resolver.openOutputStream(uri)

                stream?.let { stream ->
                    if (!bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream)) {
                        throw IOException("Failed to save bitmap.")
                    }
                } ?: throw IOException("Failed to get output stream.")

            } ?: throw IOException("Failed to create new MediaStore record")

        } catch (e: IOException) {
            if (uri != null) {
                resolver.delete(uri, null, null)
            }
            throw IOException(e)
        } finally {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                contentValues.put(MediaStore.MediaColumns.IS_PENDING, 0)
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
