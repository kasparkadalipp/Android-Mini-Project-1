package com.example.recipesapp.activity

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory

import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.example.recipesapp.RecipeViewModel
import com.example.recipesapp.databinding.ActivityNewRecipeBinding
import com.example.recipesapp.ImageUtils.Companion.fixOrientation
import com.example.recipesapp.ImageUtils.Companion.scaledBitmap
import com.example.recipesapp.room.LocalRecipeDb
import com.example.recipesapp.room.RecipeEntity
import java.io.File


class NewRecipeActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_RECIPE_ID = "recipeId"
        val TAG = "NewRecipeActivity"
    }

    private lateinit var binding: ActivityNewRecipeBinding
    private lateinit var db: LocalRecipeDb
    private var photoFile: File = File("")
    private val viewModel: RecipeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNewRecipeBinding.inflate(layoutInflater)
        db = LocalRecipeDb.getInstance(applicationContext)
        setContentView(binding.root)

        binding.newrecipeButtonTakePhoto.setOnClickListener { takePhoto() }

        setupSaveButton()
    }


    private fun setupSaveButton() {

        binding.newrecipeButtonSave.setOnClickListener {
            // Fetch the values from UI user input
            val title = binding.newrecipeEditTitle.text
            val description = binding.newrecipeEditDescription.text
            val imageURL = if(photoFile.exists()) photoFile.absolutePath else  ""

            if (title.isNullOrEmpty()) {
                Toast.makeText(this, "Title can't be empty", Toast.LENGTH_SHORT).show()
            } else if (description.isNullOrEmpty()) {
                Toast.makeText(this, "Description can't be empty", Toast.LENGTH_SHORT).show()
            } else {
                saveRecipeToDb(RecipeEntity(0, title.toString(), description.toString(), imageURL))
                finish()
            }
        }
    }

    private fun saveRecipeToDb(newRecipe: RecipeEntity) {
        db.getRecipeDao().insertRecipes(newRecipe)
    }

    private fun takePhoto() {
        photoFile = getPhotoFile(System.currentTimeMillis().toString())
        val fileUri =
            FileProvider.getUriForFile(this, "ee.ut.cs.recipeappp.fileprovider", photoFile)

        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
        launcher.launch(takePictureIntent)
    }

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                Log.w(TAG, "Image captured")
                val fullBitmap = BitmapFactory.decodeFile(photoFile.absolutePath)
                val scaledBitmap = fixOrientation(scaledBitmap(800, fullBitmap), photoFile)
                binding.newrecipeThumbnailImage.setImageBitmap(scaledBitmap)
            } else {
                Log.w(TAG, "Request cancelled or something else went wrong")
            }
        }

    /**
     * Returns the File for a photo stored on disk given the fileName.
     * Creating the storage directory if it does not exist:*/
     private fun getPhotoFile(fileName: String): File {
        // Get safe storage directory for photos. Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        val mediaStorageDir: File = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!

        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d(TAG, "failed to create directory")
        }
        return File(mediaStorageDir.path + File.separator + fileName)
    }
}