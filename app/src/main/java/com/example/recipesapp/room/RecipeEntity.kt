package com.example.recipesapp.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*
//  text title and may also show an image thumbnail.
@Entity(tableName = "recipe")
data class RecipeEntity (
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var title: String,
    var description: String,
    var thumbnail_url: String,
){

}
