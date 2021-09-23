package com.example.imagehandling

import android.net.Uri
import androidx.activity.result.ActivityResultRegistry
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import javax.inject.Inject

/**
 * Created by umair.khalid on 23,September,2021
 **/

class ImageContractHandler @Inject constructor (registry: ActivityResultRegistry) {

    private val contractUriResult : MutableLiveData<Uri>  = MutableLiveData(null)

    private val getPermission = registry.register(REGISTRY_KEY, GetContentFileUri()) { uri ->
        contractUriResult.value = uri
    }

    fun getImageFromGallery(): LiveData<Uri> {
        getPermission.launch("image/*")
        return contractUriResult
    }

    companion object {
        private const val REGISTRY_KEY = "Image Picker"
    }
}