package com.example.imagehandling

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContract
import androidx.annotation.CallSuper
import java.lang.ref.WeakReference

/**
 * Created by umair.khalid on 22,September,2021
 **/

open class GetContentFileUri : ActivityResultContract<String, Uri?>() {

    //I'll keep context reference in Weak wrapper
    var contextRef: WeakReference<Context>? = null

    @CallSuper
    override fun createIntent(context: Context, input: String): Intent {
        contextRef = WeakReference(context)

        return Intent(Intent.ACTION_GET_CONTENT)
            .addCategory(Intent.CATEGORY_OPENABLE)
            .setType(input)
    }

    final override fun getSynchronousResult(
        context: Context,
        input: String
    ): SynchronousResult<Uri?>? = null

    override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
        return contextRef?.get()?.let {
            intent.takeIf { resultCode == Activity.RESULT_OK }?.data?.getFilePathContentResolverFromUri(it)
        }
    }
}