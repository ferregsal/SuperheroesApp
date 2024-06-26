package com.example.superheroesapp.activities

import android.content.res.ColorStateList
import android.graphics.Color
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
                    binding.fullNameTextView.text = "Real Name: \n${result.biography.realName}"
                    binding.alignmentTextView.text = "Alignment: \n${result.biography.alignment}"
                   when (result.biography.alignment){
                       "good" -> binding.alignmentTextView.setTextColor(ColorStateList.valueOf(Color.BLUE))
                       "bad" -> binding.alignmentTextView.setTextColor(ColorStateList.valueOf(Color.RED))
                   }

                    binding.alterEgosTextView.text ="Alter Egos: \n${result.biography.alterEgos}"
                    binding.aliasesTextView.text = "Aliases: \n${result.biography.aliases.joinToString("\n")}"
                    binding.placeOfBirthTextView.text = "Place of Origin: \n${result.biography.placeOfBirth}"
                    binding.firstAppearanceTextView.text = "First Appearance: \n${result.biography.firstAppearance}"
                    binding.publisherTextView.text = "Publisher: \n${result.biography.publisher}"

                    Picasso.get().load(result.image.url).into(binding.avatarDetailImageView)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}