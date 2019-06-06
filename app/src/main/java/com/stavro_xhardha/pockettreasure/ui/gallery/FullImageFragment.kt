package com.stavro_xhardha.pockettreasure.ui.gallery


import android.Manifest
import android.app.WallpaperManager
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
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
import com.stavro_xhardha.pockettreasure.R
import com.stavro_xhardha.pockettreasure.brain.REQUEST_STORAGE_PERMISSION
import kotlinx.android.synthetic.main.fragment_full_image.*
import kotlinx.coroutines.*
import java.io.IOException
import java.net.URL


class FullImageFragment : Fragment() {

    //todo please refactor this :)

    private val args: FullImageFragmentArgs by navArgs()
    private lateinit var expetedUrl: String

    private val completableJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + completableJob)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_full_image, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        expetedUrl = args.imageUrl

        if (expetedUrl.isNotEmpty()) {
            Picasso.get().load(expetedUrl)
                .into(ivFullImage, object : Callback {
                    override fun onSuccess() {
                        onSuccessFulImageLoad()
                    }

                    override fun onError(e: Exception?) {
                        loadErrorImage()
                    }
                })
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_STORAGE_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    writeImageToFile()
                } else {
                    Toast.makeText(activity, R.string.can_not_save_image_need_permission, Toast.LENGTH_LONG).show()
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
        Picasso.get().load(R.drawable.ic_error_in_connection).into(ivFullImage)
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
            item.itemId == R.id.action_set_wallpaper -> setImageAsWallPaper()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setImageAsWallPaper() {
        val wallPaperOptions = listOf("Home Screen", "Lock Screen", "Both")
        showWallpapaerDialog(wallPaperOptions)
    }

    private fun showWallpapaerDialog(wallPaperOptions: List<String>) {
        MaterialDialog(activity!!).show {
            title(R.string.set_as_wallpaper)
            listItemsSingleChoice(items = wallPaperOptions, initialSelection = 0) { dialog, index, _ ->
                setWallPaper(index)
                dialog.dismiss()
            }
        }
    }

    private fun setWallPaper(index: Int) {
        coroutineScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                pbFullImage.visibility = View.VISIBLE
            }

            initWallPaperSetting(index)

            withContext(Dispatchers.Main) {
                pbFullImage.visibility = View.GONE
                Snackbar.make(rlFullImageHolder, R.string.wallpaper_saved, Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun initWallPaperSetting(index: Int) {
        val result = Picasso.get().load(expetedUrl).get()
        val wallpaperManager = WallpaperManager.getInstance(activity)
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
            trySavingImage()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    @Throws(IOException::class)
    private fun trySavingImage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkForPermission()
        } else {
            writeImageToFile()
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
            withContext(Dispatchers.Main) {
                pbFullImage.visibility = View.VISIBLE
            }

            initImageSaving()

            withContext(Dispatchers.Main) {
                pbFullImage.visibility = View.GONE
                Snackbar.make(rlFullImageHolder, R.string.image_saved, Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun initImageSaving() {
        val url = URL(expetedUrl)
        val image = BitmapFactory.decodeStream(url.openConnection().getInputStream())
        MediaStore.Images.Media.insertImage(
            activity!!.contentResolver,
            image,
            System.currentTimeMillis().toString(),
            ""
        )
    }

    private fun shareImageUrl() {
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        sharingIntent.putExtra(Intent.EXTRA_TEXT, expetedUrl)
        startActivity(Intent.createChooser(sharingIntent, activity!!.resources.getString(R.string.share_via)))
    }
}
