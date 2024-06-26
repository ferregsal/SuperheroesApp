package com.example.superheroesapp.activities

import android.content.DialogInterface
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.superheroesapp.R
import com.example.superheroesapp.data.Superhero
import com.example.superheroesapp.databinding.ActivityDetailBinding
import com.example.superheroesapp.utils.RetrofitProvider
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.panpf.swsv.CircularLayout
import me.panpf.swsv.SpiderWebScoreView


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

        //SpiderChart
        val spiderWebScoreView1 =
            findViewById<View>(R.id.statsSpiderWeb) as SpiderWebScoreView
        val circularLayout1 =
            findViewById<View>(R.id.layout_mainActivity_circular1) as CircularLayout

        /*val scores = arrayOf<Score>(
            Score(7.0f, R.drawable.int_icon),
            Score(8.0f, R.drawable.strength_icon),
            Score(5.0f, R.drawable.speed_icon),
            Score(5.0f, R.drawable.durability_icon),
            Score(8.0f, R.drawable.power_icon),
            Score(7.0f, R.drawable.combat_icon),
            )

        setup(spiderWebScoreView1, circularLayout1, *scores)*/
    }

    private fun setup(
        spiderWebScoreView: SpiderWebScoreView,
        circularLayout: CircularLayout,
        vararg scores: Score
    ) {
        val scoreArray = FloatArray(scores.size)
        for (w in scores.indices) {
            scoreArray[w] = scores[w].score
        }
        spiderWebScoreView.setScores(10f, scoreArray)

        circularLayout.removeAllViews()
        for (score in scores) {
            val scoreTextView = LayoutInflater.from(baseContext)
                .inflate(R.layout.score, circularLayout, false) as TextView
            scoreTextView.text = score.score.toString() + ""
            if (score.iconId != 0) {
                scoreTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, score.iconId, 0)
            }
            circularLayout.addView(scoreTextView)
        }
    }

    private class Score(fl: Float, intIcon: Int) {
        var score: Float = 0f
        var iconId: Int = 0

        private fun Score(score: Float, iconId: Int) {
            this.score = score
            this.iconId = iconId
        }

        private fun Score(score: Float) {
            this.score = score
        }








    }
    private fun getSuperheroById(superheroId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val superheroApiService = RetrofitProvider.getSuperheroApiService()
                val result = superheroApiService.getSuperheroById(superheroId)

                runOnUiThread {
                    Log.d("DetailActivity", "UI thread")
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


                    val scores = arrayOf(
                        Score(result.stats.intelligence.toFloat(), R.drawable.int_icon),
                        Score(result.stats.strength.toFloat(), R.drawable.strength_icon),
                        Score(result.stats.speed.toFloat(), R.drawable.speed_icon),
                        Score(result.stats.durability.toFloat(), R.drawable.durability_icon),
                        Score(result.stats.power.toFloat(), R.drawable.power_icon),
                        Score(result.stats.combat.toFloat(), R.drawable.combat_icon)
                    )

                    // Configurar el SpiderWebScoreView y CircularLayout
                    val spiderWebScoreView1 = findViewById<View>(R.id.statsSpiderWeb) as SpiderWebScoreView
                    val circularLayout1 = findViewById<View>(R.id.layout_mainActivity_circular1) as CircularLayout

                    setup(spiderWebScoreView1, circularLayout1, *scores)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("DetailActivity", "Error fetching superhero data", e)
            }
        }
    }



}