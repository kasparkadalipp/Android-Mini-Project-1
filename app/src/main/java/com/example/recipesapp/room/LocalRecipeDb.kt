package com.example.recipesapp.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [RecipeEntity::class], version = 3)
abstract class LocalRecipeDb : RoomDatabase() {

    companion object {
        private lateinit var recipeDb: LocalRecipeDb

        @Synchronized
        fun getInstance(context: Context): LocalRecipeDb {

            if (!this::recipeDb.isInitialized) {
                recipeDb = Room.databaseBuilder(
                    context, LocalRecipeDb::class.java, "myRecipes")
                    .fallbackToDestructiveMigration() // each time schema changes, data is lost!
                    .allowMainThreadQueries() // if possible, use background thread instead
                    .build()
            }
            return recipeDb

        }
    }

    abstract fun getRecipeDao(): RecipeDao
}