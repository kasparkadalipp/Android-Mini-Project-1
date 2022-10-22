package com.example.recipesapp

import android.graphics.Bitmap
import android.graphics.Matrix
import androidx.exifinterface.media.ExifInterface
import java.io.File

class ImageUtils {
    companion object {
        fun fixOrientation(bitmap: Bitmap, pictureFile: File): Bitmap {
            val exif = ExifInterface(pictureFile.absolutePath)

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
            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        }

        fun scaledBitmap(size:Int, fullBitmap: Bitmap): Bitmap {
            val ratio = fullBitmap.width.toDouble() / fullBitmap.height
            return Bitmap.createScaledBitmap(fullBitmap, (size * ratio).toInt(), size, false)
        }
    }
}