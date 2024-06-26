package com.example.superheroesapp.activities

import android.content.DialogInterface
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.superheroesapp.R
import com.example.superheroesapp.data.Superhero
import com.example.superheroesapp.data.SuperheroResponse
import com.example.superheroesapp.databinding.ActivityDetailBinding
import com.example.superheroesapp.databinding.CustomViewLayoutBinding
import com.example.superheroesapp.utils.RetrofitProvider
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailActivity : AppCompatActivity() {

    lateinit var binding: ActivityDetailBinding


    fun showAlertDialogButtonClicked() {
        // Create an alert builder
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Attributes")

        // set the custom layout
        val customLayout: View = layoutInflater.inflate(R.layout.custom_view_layout, null)
        builder.setView(customLayout)

        // add a button
        builder.setPositiveButton("OK") { dialog: DialogInterface?, which: Int ->
            // send data from the AlertDialog to the Activity
            val textView = customLayout.findViewById<TextView>(R.id.statsTextview)
            sendDialogDataToActivity(textView.text.toString())
        }
        // create and show the alert dialog
        val dialog = builder.create()
        dialog.show()
    }

    // Do something with the data coming from the AlertDialog
    private fun sendDialogDataToActivity(data: String) {
        Toast.makeText(this, data, Toast.LENGTH_SHORT).show()
    }



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
                    binding.statsButton.setOnClickListener{
                        showAlertDialogButtonClicked() }
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