package com.example.imagehandling

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.OpenableColumns
import android.webkit.MimeTypeMap
import androidx.core.net.toUri
import java.io.*

/**
 * Created by umair.khalid on 22,September,2021
 **/

private const val COPYSTREAM_BUFFER_SIZE = 2 * 1024

fun copyStream(src: InputStream, dest: OutputStream): Boolean {
    val buffer = ByteArray(COPYSTREAM_BUFFER_SIZE)
    try { /*w  ww .j a v a 2  s  .  com*/
        var size: Int
        while (src.read(buffer).also { size = it } != -1) {
            dest.write(buffer, 0, size)
        }
    } catch (e: IOException) {
        return false
    }
    return true
}


@Throws(IOException::class)
fun Uri.getFilePathContentResolverFromUri(context: Context): Uri? {
    val fileName: String = this.getFileName(context = context)
    val file = File(context.externalCacheDir, fileName)
    file.createNewFile()
    FileOutputStream(file).use { outputStream ->
        context.contentResolver.openInputStream(this).use { inputStream ->
            inputStream?.let { copyStream(it, outputStream) } //Simply reads input to output stream
            outputStream.flush()
        }
    }
    return file.toUri()
}

fun Uri.getFileName(context: Context): String {
    var fileName: String? = this.getFileNameFromCursor(context)
    if (fileName == null) {
        val fileExtension: String? = this.getFileExtension(context)
        fileName = "temp_file" + if (fileExtension != null) ".$fileExtension" else ""
    } else if (!fileName.contains(".")) {
        val fileExtension: String? = this.getFileExtension(context)
        fileName = "$fileName.$fileExtension"
    }
    return fileName
}

fun Uri.getFileExtension(context: Context): String? {
    val fileType: String? = this.let { context.contentResolver.getType(it) }
    return MimeTypeMap.getSingleton().getExtensionFromMimeType(fileType)
}

fun Uri.getFileNameFromCursor(context: Context): String? {
    val fileCursor: Cursor? = this.let {
        context.contentResolver
            .query(it, arrayOf(OpenableColumns.DISPLAY_NAME), null, null, null)
    }
    var fileName: String? = null
    if (fileCursor != null && fileCursor.moveToFirst()) {
        val cIndex: Int = fileCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        if (cIndex != -1) {
            fileName = fileCursor.getString(cIndex)
        }
    }
    return fileName
}