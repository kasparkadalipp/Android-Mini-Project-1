package com.example.recipesapp.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.recipesapp.ImageUtils.Companion.getThumbnailOrDefault
import com.example.recipesapp.RecipeViewModel
import com.example.recipesapp.activity.NewRecipeActivity.Companion.EXTRA_RECIPE_ID
import com.example.recipesapp.databinding.ActivityRecipeDetailsBinding
import com.example.recipesapp.room.LocalRecipeDb
import com.example.recipesapp.room.RecipeEntity

class RecipeDetailsActivity : AppCompatActivity() {

    private val viewModel: RecipeViewModel by viewModels()
    private lateinit var binding: ActivityRecipeDetailsBinding
    private lateinit var db: LocalRecipeDb

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Setup View Binding
        binding = ActivityRecipeDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = LocalRecipeDb.getInstance(applicationContext)
        loadAndShowRecipe()
    }

    private fun loadAndShowRecipe() {
        // Get recipe ID from intent, load recipe details  from DB and show it in the UI
        val id = intent.getIntExtra(EXTRA_RECIPE_ID, -1)
        val loadedRecipe = getRecipeFromDb(id)
        loadedRecipe?.let { showRecipe(it) }
    }

    private fun getRecipeFromDb(id: Int): RecipeEntity? {
        return db.getRecipeDao().loadRecipeByID(id)
    }

    private fun showRecipe(recipe: RecipeEntity) {
        recipe.apply {
            binding.detailsviewTextTitle.text = title
            binding.detailsviewTextDescription.text = description
            binding.detailsviewImage.setImageBitmap(getThumbnailOrDefault(thumbnail_url, 400))
        }
    }
}