package com.example.recipesapp

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import androidx.exifinterface.media.ExifInterface
import androidx.lifecycle.AndroidViewModel
import java.io.File

class ImageUtils(application: Application) : AndroidViewModel(application) {

    companion object {

        lateinit var defaultThumbnail: Bitmap

        fun getThumbnailOrDefault(url: String, size: Int): Bitmap {
            if (url.isEmpty())
                return defaultThumbnail
            var bitmap = BitmapFactory.decodeFile(url)
            bitmap = scaledBitmap(size, bitmap)
            bitmap = fixOrientation(bitmap, url)
            return bitmap
        }

        private fun scaledBitmap(size: Int, fullBitmap: Bitmap): Bitmap {
            return Bitmap.createScaledBitmap(fullBitmap, size, size, false)
        }

        private fun fixOrientation(bitmap: Bitmap, url: String): Bitmap {
            val exif = ExifInterface(url)

            return when (exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            )) {
                ExifInterface.ORIENTATION_ROTATE_90 -> rotateBitmap(bitmap, 90)
                ExifInterface.ORIENTATION_ROTATE_180 -> rotateBitmap(bitmap, 180)
                ExifInterface.ORIENTATION_ROTATE_270 -> rotateBitmap(bitmap, 270)
                else -> bitmap
            }
        }

        private fun rotateBitmap(bitmap: Bitmap, degrees: Int): Bitmap {
            val matrix = Matrix()
            matrix.postRotate(degrees.toFloat())
            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, false)
        }

        fun loadDefaultThumbnail(context: Context) {
            if (!this::defaultThumbnail.isInitialized)
                defaultThumbnail = BitmapFactory.decodeResource(context.resources, R.drawable.missing_image)
        }
    }
}