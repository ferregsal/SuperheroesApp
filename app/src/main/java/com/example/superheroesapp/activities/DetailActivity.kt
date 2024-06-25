package com.example.superheroesapp.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.superheroesapp.R
import com.example.superheroesapp.data.Superhero
import com.example.superheroesapp.data.SuperheroResponse
import com.example.superheroesapp.databinding.ActivityDetailBinding
import com.example.superheroesapp.utils.RetrofitProvider
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailActivity : AppCompatActivity() {
    lateinit var binding: ActivityDetailBinding

    companion object {
        const val EXTRA_SUPERHERO_ID = "SUPERHERO_ID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtener el ID del superhéroe del Intent
        val superheroId = intent.getIntExtra(EXTRA_SUPERHERO_ID, -1)

        // Asegurarse de que el ID sea válido antes de llamar a getSuperheroById
        if (superheroId != -1) {
            getSuperheroById(superheroId)
        } else {
            // Manejar el error si el ID no es válido
        }
    }

    private fun getSuperheroById(superheroId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val superheroApiService = RetrofitProvider.getSuperheroApiService()
                val result = superheroApiService.getSuperheroById(superheroId)
                runOnUiThread {
                    binding.nameDetailTextView.text = result.name
                    binding.biographyTextView.text = result.biography.toString()
                    Picasso.get().load(result.image.url).into(binding.avatarDetailImageView)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}