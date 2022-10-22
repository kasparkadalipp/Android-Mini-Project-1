package com.example.recipesapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.recipesapp.ImageUtils.Companion.getThumbnailOrDefault
import com.example.recipesapp.room.RecipeEntity

class RecipesAdapter(
    var data: Array<RecipeEntity> = arrayOf(),
    private var listener: RecipeClickListener
) : RecyclerView.Adapter<RecipesAdapter.RecipeViewHolder>() {

    fun interface RecipeClickListener {
        fun onRecipeClick(recipe: RecipeEntity)
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
            this.findViewById<ImageView>(R.id.listview_image_recipe_thumbnail)
                .setImageBitmap(getThumbnailOrDefault(recipe.thumbnail_url, 800))
            setOnClickListener { listener.onRecipeClick(recipe) }
        }
    }


}