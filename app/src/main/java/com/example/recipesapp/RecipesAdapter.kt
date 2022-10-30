package com.example.recipesapp

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecipesAdapter(
    var data: MutableList<Recipe> = ArrayList(),
    private var listener: RecipeClickListener
) : RecyclerView.Adapter<RecipesAdapter.RecipeViewHolder>() {

    fun interface RecipeClickListener {
        fun onRecipeClick(recipe: Recipe)
    }

    inner class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.single_recipe, parent, false)
        return RecipeViewHolder(view)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = data[position]

        holder.itemView.apply {
            this.findViewById<TextView>(R.id.listview_text_recipe_title).text = recipe.title
            this.findViewById<ImageView>(R.id.listview_image_recipe_thumbnail).setImageBitmap(recipe.thumbnail)
            setOnClickListener { listener.onRecipeClick(recipe) }
        }
    }
}

data class Recipe(
    val id: Int,
    val title: String,
    val url: String,
    var thumbnail: Bitmap
)
