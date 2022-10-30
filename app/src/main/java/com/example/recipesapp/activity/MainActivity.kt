package com.example.recipesapp.activity

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.recipesapp.ImageUtils
import com.example.recipesapp.Recipe
import com.example.recipesapp.RecipeViewModel
import com.example.recipesapp.RecipesAdapter
import com.example.recipesapp.activity.NewRecipeActivity.Companion.EXTRA_RECIPE_ID
import com.example.recipesapp.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var recipesAdapter: RecipesAdapter
    lateinit var model: RecipeViewModel
    var arraylen = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        model = ViewModelProvider(this).get(RecipeViewModel::class.java)
        ImageUtils.loadDefaultThumbnail(applicationContext)
        setupRecyclerView()
        binding.buttonAddNewRecipe.setOnClickListener { addNewRecipe() }
    }

    override fun onResume() {
        super.onResume()
        var x=model.recipeArray.size

        arraylen=model.recipeArray.size

        if (model.updateData()) {
            if (arraylen - model.recipeArray.size == -1){
                recipesAdapter.data = model.recipeArray
                recipesAdapter.notifyItemRangeChanged(arraylen, model.recipeArray.size)
                updateNewThumbnail()

                arraylen=model.recipeArray.size
            }
            else{
            recipesAdapter.data = model.recipeArray
            recipesAdapter.notifyItemRangeChanged(0, model.recipeArray.size)
            updateThumbnails()

            arraylen=model.recipeArray.size}
        }
    }

    private fun updateThumbnails() {
        CoroutineScope(Dispatchers.IO).launch {
            model.recipeArray.forEachIndexed { index, it ->
                if (it.url.isNotEmpty()) {
                    it.thumbnail = ImageUtils.getThumbnailOrDefault(it.url, 400)
                    withContext(Dispatchers.Main) {
                        recipesAdapter.notifyItemChanged(index)
                    }
                }
            }
        }
    }

    private fun updateNewThumbnail() {
        CoroutineScope(Dispatchers.IO).launch {
            var it = model.recipeArray.last()
            if (it.url.isNotEmpty()) {
                it.thumbnail = ImageUtils.getThumbnailOrDefault(it.url, 400)
                withContext(Dispatchers.Main) {
                    recipesAdapter.notifyItemChanged(model.recipeArray.size-1)
                }
            }
        }
    }

    private fun addNewRecipe() {
        startActivity(Intent(this, NewRecipeActivity::class.java))
    }

    private fun setupRecyclerView() {
        val recipeClickListener =
            RecipesAdapter.RecipeClickListener { p -> openRecipeDetailsActivity(p) }
        recipesAdapter = RecipesAdapter(model.recipeArray, recipeClickListener)
        binding.recyclerviewRecipelist.adapter = recipesAdapter
        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.recyclerviewRecipelist.layoutManager = GridLayoutManager(applicationContext, 3)
        } else {
            binding.recyclerviewRecipelist.layoutManager = GridLayoutManager(applicationContext, 2)
        }

    }

    private fun openRecipeDetailsActivity(recipe: Recipe) {
        val intent = Intent(this, RecipeDetailsActivity::class.java)
        intent.putExtra(EXTRA_RECIPE_ID, recipe.id)
        startActivity(intent)
    }
}