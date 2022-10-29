package com.example.recipesapp

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel

class NewRecipeViewModel(app: Application) : AndroidViewModel(app){
    var thumbnailURL: String = ""
    var thumbnail: Bitmap = ImageUtils.defaultThumbnail
}