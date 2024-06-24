package com.example.superheroesapp.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.superheroesapp.R
import com.example.superheroesapp.data.Superhero
import com.example.superheroesapp.data.SuperheroResponse
import com.example.superheroesapp.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    lateinit var binding: ActivityDetailBinding

    companion object {
        const val EXTRA_SUPERHERO_ID = "SUPERHERO_ID"
        const val EXTRA_SUPERHERO_NAME = "SUPERHERO_NAME"
        const val EXTRA_SUPERHERO_BIOGRAPHY = "SUPERHERO_BIOGRAPHY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
      //  binding.avatarDetailImageView.setImageResource(Superhero.image)
    }
}