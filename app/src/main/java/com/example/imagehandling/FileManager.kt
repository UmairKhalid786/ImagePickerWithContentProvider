package com.example.imagehandling

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File
import java.lang.ref.WeakReference

/**
 * Created by umair.khalid on 23,September,2021
 **/

object FileManager : FileUriManager {

    override fun getTmpFileUri(context: Context): Uri? {

        val contextRef = WeakReference(context)

        return contextRef.get()?.let {
            val tmpFile = File.createTempFile("tmp_image_file", ".png", context.cacheDir).apply {
                createNewFile()
                deleteOnExit()
            }

            FileProvider.getUriForFile(
                context,
                "${BuildConfig.APPLICATION_ID}.provider",
                tmpFile
            )
        }
    }
}

interface FileUriManager {
    fun getTmpFileUri(context: Context): Uri?
}