package com.example.recipesapp.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RecipeDao {

    @Query("SELECT title FROM Recipe")
    fun loadRecipeList(): Array<String>

    @Query("SELECT * FROM Recipe")
    fun loadRecipes(): Array<RecipeEntity>

    @Query("SELECT * FROM Recipe where id = :Id")
    fun loadRecipeByID(Id: Int): RecipeEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRecipes(vararg recipes: RecipeEntity)
}