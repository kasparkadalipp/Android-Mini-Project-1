package com.example.recipesapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.recipesapp.room.LocalRecipeDb
import com.example.recipesapp.room.RecipeEntity

class RecipeViewModel(application: Application): AndroidViewModel(application) {
    var localDb = LocalRecipeDb.getInstance(application)

    var recipeArray: Array<RecipeEntity> = arrayOf()

    fun refresh(){
        // Reload dataset from DB, put it in in-memory list
        recipeArray = localDb.getRecipeDao().loadRecipes()
    }

}