package com.example.rodri.projectlist.common.util

import android.graphics.Bitmap
import android.widget.ImageView
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.rodrixan.projects.technicaltests.shieldherolist.R

fun ImageView.loadFromDrawable(drawableId: Int) {
    if (drawableId != -1) {
        Glide.with(context).load(drawableId).apply(RequestOptions().centerInside()).into(this)
    }
}

fun ImageView.loadFromUrl(url: String?,
                          placeHolderResId: Int? = null,
                          listener: ((Boolean) -> Unit)? = null,
                          useBackgroundColor: Boolean = false,
                          resizeSize: Pair<Int, Int>? = null) {
    //Not using let because of: https://medium.com/@elye.project/kotlin-dont-just-use-let-7e91f544e27f
    url?.run {
        Glide.with(context).asBitmap().load(this).apply {
            this.listener(object : RequestListener<Bitmap> {
                override fun onLoadFailed(e: GlideException?,
                                          model: Any?,
                                          target: Target<Bitmap>?,
                                          isFirstResource: Boolean): Boolean {
                    listener?.invoke(false)
                    return false
                }

                override fun onResourceReady(resource: Bitmap?,
                                             model: Any?,
                                             target: Target<Bitmap>?,
                                             dataSource: DataSource?,
                                             isFirstResource: Boolean): Boolean {
                    listener?.invoke(true)
                    resource?.run {
                        if (useBackgroundColor) {
                            //async to avoid UI processing
                            Palette.from(this).generate { palette ->
                                palette?.dominantSwatch?.run {
                                    this@loadFromUrl.setBackgroundColor(rgb)
                                }
                            }
                        }
                    }
                    return false
                }

            })
            if (resizeSize != null) {
                apply(RequestOptions().override(resizeSize.first, resizeSize.second))
            }
            into(this@loadFromUrl)
        }

    } ?: loadFromDrawable(placeHolderResId ?: R.drawable.ic_img_placeholder)
}