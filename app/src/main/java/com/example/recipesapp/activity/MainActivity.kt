package com.example.recipesapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
    lateinit var  model: RecipeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        model = ViewModelProvider(this).get(RecipeViewModel::class.java)
        ImageUtils.loadDefaultThumbnail(applicationContext)
        setupRecyclerView()
        binding.buttonAddNewRecipe.setOnClickListener{addNewRecipe()}
    }

    override fun onResume(){
        super.onResume()

        if(model.updateData()){
            recipesAdapter.data = model.recipeArray
            recipesAdapter.notifyItemRangeChanged(0, model.recipeArray.size)
            updateThumbnails() // TODO if new element is inserted only update its thumbnail
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

    private fun addNewRecipe() {
        startActivity(Intent(this, NewRecipeActivity::class.java))
    }

    private fun setupRecyclerView() {
        val recipeClickListener = RecipesAdapter.RecipeClickListener { p -> openRecipeDetailsActivity(p) }
        recipesAdapter = RecipesAdapter(model.recipeArray, recipeClickListener)
        binding.recyclerviewRecipelist.adapter = recipesAdapter
        binding.recyclerviewRecipelist.layoutManager = GridLayoutManager(applicationContext, 2)
    }

    private fun openRecipeDetailsActivity(recipe: Recipe) {
        val intent = Intent(this, RecipeDetailsActivity::class.java)
        intent.putExtra(EXTRA_RECIPE_ID, recipe.id)
        startActivity(intent)
    }
}