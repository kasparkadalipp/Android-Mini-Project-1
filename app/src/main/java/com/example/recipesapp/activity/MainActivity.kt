package com.example.recipesapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.recipesapp.ImageUtils
import com.example.recipesapp.RecipeViewModel
import com.example.recipesapp.RecipesAdapter
import com.example.recipesapp.activity.NewRecipeActivity.Companion.EXTRA_RECIPE_ID
import com.example.recipesapp.databinding.ActivityMainBinding
import com.example.recipesapp.room.RecipeEntity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var recipesAdapter: RecipesAdapter
    val model: RecipeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupRecyclerView()
        ImageUtils.loadDefaultThumbnail(applicationContext)
        binding.buttonAddNewRecipe.setOnClickListener{addNewRecipe()}
    }

    override fun onResume(){
        super.onResume()
        model.refresh()
        recipesAdapter.data = model.recipeArray
        recipesAdapter.notifyDataSetChanged()
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

    private fun openRecipeDetailsActivity(recipe: RecipeEntity) {
        val intent = Intent(this, RecipeDetailsActivity::class.java)
        intent.putExtra(EXTRA_RECIPE_ID, recipe.id)
        startActivity(intent)
    }
}