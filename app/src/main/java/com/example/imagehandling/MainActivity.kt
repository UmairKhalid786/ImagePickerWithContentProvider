package com.example.imagehandling

import android.app.Activity
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.lang.ref.WeakReference
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var contractHandler : ImageContractHandler

    private lateinit var latestTmpUri: Uri

    private val getImageBtn: View by lazy { findViewById(R.id.get_iv_btn) }
    private val backgroundImageView: ImageView by lazy { findViewById(R.id.bg_iv) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getImageBtn.setOnClickListener {
            showImagesBottomSheet(
                onGalleryClick = {
                    onGalleryClick()
                },
                onCameraClick = {
                    onCameraClick()
                }, MainActivity@this
            )
        }
    }

    private fun onCameraClick() {
        lifecycleScope.launchWhenStarted {
            FileManager.getTmpFileUri(applicationContext)?.let { uri ->
                latestTmpUri = uri
                takeImageResult.launch(uri)
            }
        }
    }

    private fun onGalleryClick() {
        contractHandler.getImageFromGallery().observe(this, {
            it?.let { u ->
                backgroundImageView.setImageURI(u)
                val file = File(u.path)
            }
        })
    }

    fun onGalleryClickOld() {
        getContent.launch("image/*")
    }

    private val getContent = registerForActivityResult(GetContentFileUri()) { uri: Uri? ->
        uri?.let { u ->
            backgroundImageView.setImageURI(u)
            val file = File(u.path)
        }
    }

    private val takeImageResult = registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
        if (isSuccess) {
            latestTmpUri.let { uri ->
                backgroundImageView.setImageURI(uri)
            }
        }
    }

    // I haven't directly called onCameraClick and onGalleryClick because
    // I want to make this function self sufficient
    private fun showImagesBottomSheet(
        onGalleryClick: (() -> Unit),
        onCameraClick: (() -> Unit),
        context: Activity
    ) {
        val contextRef = WeakReference(context)

        val bottomSheetDialog = contextRef.get()?.let { BottomSheetDialog(it) }
        bottomSheetDialog?.setContentView(R.layout.image_picker_bottom_sheet)

        bottomSheetDialog?.findViewById<View>(R.id.from_camera_btn)
            ?.setOnClickListener {
                onCameraClick()
                bottomSheetDialog.hide()
            }
        bottomSheetDialog?.findViewById<View>(R.id.from_gallery_btn)
            ?.setOnClickListener {
                onGalleryClick()
                bottomSheetDialog.hide()
            }

        bottomSheetDialog?.show()
    }
}


