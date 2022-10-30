package com.example.recipesapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.recipesapp.room.LocalRecipeDb

class RecipeViewModel(val app: Application) : AndroidViewModel(app) {

    enum class ChangeType {
        APPLICATION_OPENED,
        NEW_ELEMENT,
        UNCHANGED
    }

    var localDb = LocalRecipeDb.getInstance(app)

    var recipeArray: MutableList<Recipe> = ArrayList()

    fun updateData() : ChangeType{
        // Reload dataset from DB, put it in in-memory list
        val result = localDb.getRecipeDao().loadRecipes().map {
            Recipe(
                it.id,
                it.title,
                it.thumbnail_url,
                ImageUtils.defaultThumbnail
            )
        }

        if(recipeArray.isEmpty()) {
            recipeArray = result.toMutableList()
            return ChangeType.APPLICATION_OPENED
        }

        if(result.size > recipeArray.size){
            val new_value = result.last()
            new_value.thumbnail = ImageUtils.getThumbnailOrDefault(new_value.url, 400)
            recipeArray.add(new_value)
            return ChangeType.NEW_ELEMENT
        }

        return ChangeType.UNCHANGED
    }
}